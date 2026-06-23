package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Builds an entry from a git branch into a jar, using a reusable source cache under
 * plugins/Slimefun/addon-sources/<repo>. First build clones; later builds fetch + hard-reset to
 * the chosen branch. Runs the repo's Gradle wrapper. Blocking — call off the main thread.
 */
public final class AddonSourceBuilder {

    /** The outcome of a build attempt. */
    public static final class Result {

        private final boolean success;
        private final File jar;
        private final File logFile;
        private final String describe;

        Result(boolean success, File jar, File logFile, String describe) {
            this.success = success;
            this.jar = jar;
            this.logFile = logFile;
            this.describe = describe;
        }

        public boolean isSuccess() {
            return success;
        }

        /** The produced jar (only when success). */
        @Nullable
        public File getJar() {
            return jar;
        }

        /** The full build log, always written. */
        @Nonnull
        public File getLogFile() {
            return logFile;
        }

        /** The built source's {@code git describe} string (last tag + commit sha, or short sha). */
        @Nonnull
        public String getDescribe() {
            return describe == null ? "" : describe;
        }
    }

    /**
     * Clones/updates the repo to the given branch and runs `gradlew assemble`.
     *
     * @param timestamp
     *            a caller-supplied timestamp string for the log file name (Date.now() is unavailable
     *            in some contexts; callers pass System.currentTimeMillis()).
     */
    @Nonnull
    public Result build(@Nonnull AddonCatalog.Entry entry, @Nonnull String branch, long timestamp) {
        File repoDir = new File(InstallTargets.sourcesDir(), entry.getRepo());
        File logFile = new File(InstallTargets.logsDir(), entry.getRepo() + "-" + timestamp + ".log");

        boolean prepared = prepareSource(entry, branch, repoDir, logFile);

        if (!prepared) {
            return new Result(false, null, logFile, "");
        }

        String describe = describe(repoDir);
        boolean built = runGradle(repoDir, logFile);

        if (!built) {
            return new Result(false, null, logFile, describe);
        }

        File jar = findBuiltJar(repoDir);
        return new Result(jar != null, jar, logFile, describe);
    }

    /** Best-effort {@code git describe}: last reachable tag + commits + short sha, or just the short sha. */
    @Nonnull
    private String describe(@Nonnull File repoDir) {
        String out = capture(repoDir, "git", "describe", "--tags", "--always", "--abbrev=7");
        return out != null ? out.trim() : "";
    }

    private boolean prepareSource(AddonCatalog.Entry entry, String branch, File repoDir, File logFile) {
        String cloneUrl = "https://github.com/" + entry.getSlug() + ".git";

        if (new File(repoDir, ".git").isDirectory()) {
            return run(repoDir, logFile, "git", "fetch", "--depth", "1", "origin", branch)
                && run(repoDir, logFile, "git", "checkout", "-B", branch, "origin/" + branch)
                && run(repoDir, logFile, "git", "reset", "--hard", "origin/" + branch);
        }

        // Fresh clone (parent dir of repoDir is the sources root, which exists).
        return run(InstallTargets.sourcesDir(), logFile, "git", "clone", "--depth", "1", "--branch", branch, cloneUrl, entry.getRepo());
    }

    private boolean runGradle(File repoDir, File logFile) {
        // gradlew.bat is a batch script: ProcessBuilder needs cmd /c to run it on Windows.
        if (EnvironmentDetector.isWindows()) {
            return run(repoDir, logFile, "cmd", "/c", "gradlew.bat", "assemble", "--no-daemon");
        }

        return run(repoDir, logFile, "./gradlew", "assemble", "--no-daemon");
    }

    /**
     * Finds the built jar in build/libs, preferring a jar with no -sources/-javadoc classifier and
     * the newest modification time.
     */
    @Nullable
    private File findBuiltJar(File repoDir) {
        File libs = new File(repoDir, "build/libs");
        File[] jars = libs.listFiles((dir, name) -> name.endsWith(".jar") && !name.contains("-sources") && !name.contains("-javadoc"));

        if (jars == null || jars.length == 0) {
            return null;
        }

        File newest = jars[0];

        for (File jar : jars) {
            if (jar.lastModified() > newest.lastModified()) {
                newest = jar;
            }
        }

        return newest;
    }

    /** Runs a command in workingDir, appending combined stdout/stderr to logFile. */
    private boolean run(File workingDir, File logFile, String... command) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(workingDir);
            builder.redirectErrorStream(true);
            builder.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));
            Process process = builder.start();
            return process.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            return false;
        }
    }

    /** Runs a command in workingDir and returns its trimmed stdout, or null on failure. */
    @Nullable
    private String capture(File workingDir, String... command) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(workingDir);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            StringBuilder output = new StringBuilder();

            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream(), java.nio.charset.StandardCharsets.UTF_8))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }

            return process.waitFor() == 0 ? output.toString() : null;
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            return null;
        }
    }

    /** Reads the last n lines of a log file for a chat summary. */
    @Nonnull
    public static java.util.List<String> tail(@Nonnull File logFile, int n) {
        java.util.LinkedList<String> lines = new java.util.LinkedList<>();

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(logFile), java.nio.charset.StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);

                if (lines.size() > n) {
                    lines.removeFirst();
                }
            }
        } catch (IOException e) {
            // Best-effort; return whatever we have.
        }

        return lines;
    }
}
