package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.File;

/**
 * Detects whether the dev-only "build from a branch" mode is possible: git on PATH and a JDK
 * (a javac executable). The Gradle wrapper ships inside each cloned repo, so no separate Gradle
 * install is required. The result is cached for the JVM lifetime — these do not change at runtime.
 */
public final class EnvironmentDetector {

    private static volatile Boolean cached;

    private EnvironmentDetector() {}

    /** True when both git and a JDK are available, enabling Mode B. */
    public static boolean canBuildFromSource() {
        if (cached == null) {
            cached = Boolean.valueOf(hasGit() && hasJdk());
        }

        return cached.booleanValue();
    }

    private static boolean hasGit() {
        return canRun("git", "--version");
    }

    private static boolean hasJdk() {
        // Prefer JAVA_HOME/bin/javac (the server may run on a JRE whose PATH lacks javac), then PATH.
        String javaHome = System.getProperty("java.home");

        if (javaHome != null) {
            String exe = isWindows() ? "javac.exe" : "javac";
            File javac = new File(new File(javaHome, "bin"), exe);

            if (javac.isFile()) {
                return true;
            }

            // java.home may point at a JRE nested in a JDK; check the parent too.
            File parentJavac = new File(new File(new File(javaHome).getParentFile(), "bin"), exe);

            if (parentJavac.isFile()) {
                return true;
            }
        }

        return canRun("javac", "-version");
    }

    private static boolean canRun(String... command) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            // Drain output so the process can exit, then wait.
            try (java.io.InputStream in = process.getInputStream()) {
                byte[] buffer = new byte[1024];

                while (in.read(buffer) != -1) {
                    // discard
                }
            }

            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os != null && os.toLowerCase(java.util.Locale.ROOT).contains("win");
    }
}
