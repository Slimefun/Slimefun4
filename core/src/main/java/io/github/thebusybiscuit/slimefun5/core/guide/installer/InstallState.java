package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.configuration.file.YamlConfiguration;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Persists what the installer has staged: per entry id, the install method, the installed
 * version-or-branch, and whether a restart is pending. Stored as plugins/Slimefun/addon-installer.yml.
 *
 * This reflects what the installer DID, not what is currently loaded — "is it loaded" is asked
 * separately via Bukkit's plugin manager.
 */
public final class InstallState {

    public enum Method {
        RELEASE,
        BRANCH
    }

    /** An immutable record of one staged install. */
    public static final class Record {

        private final Method method;
        private final String version;
        private final String commit;
        private final boolean restartPending;

        public Record(Method method, String version, String commit, boolean restartPending) {
            this.method = method;
            this.version = version;
            this.commit = commit;
            this.restartPending = restartPending;
        }

        @Nonnull
        public Method getMethod() {
            return method;
        }

        /** Release tag (RELEASE) or branch name (BRANCH). */
        @Nonnull
        public String getVersion() {
            return version;
        }

        /** For a BRANCH build, the {@code git describe} string (last tag + commit sha); empty otherwise. */
        @Nonnull
        public String getCommit() {
            return commit;
        }

        public boolean isRestartPending() {
            return restartPending;
        }
    }

    private final File file;
    private final YamlConfiguration config;

    public InstallState() {
        File dataFolder = Slimefun.instance().getDataFolder();
        this.file = new File(dataFolder, "addon-installer.yml");
        this.config = file.exists() ? YamlConfiguration.loadConfiguration(file) : new YamlConfiguration();
    }

    @Nullable
    public synchronized Record get(@Nonnull String id) {
        if (!config.contains(id)) {
            return null;
        }

        String methodName = config.getString(id + ".method", Method.RELEASE.name());
        Method method;

        try {
            method = Method.valueOf(methodName);
        } catch (IllegalArgumentException e) {
            method = Method.RELEASE;
        }

        String version = config.getString(id + ".version", "");
        String commit = config.getString(id + ".commit", "");
        boolean restartPending = config.getBoolean(id + ".restart-pending", false);
        return new Record(method, version, commit, restartPending);
    }

    /** Records a staged release install and immediately persists. */
    public synchronized void set(@Nonnull String id, @Nonnull Method method, @Nonnull String version, boolean restartPending) {
        set(id, method, version, "", restartPending);
    }

    /** Records a staged install (with a {@code git describe} commit string for branch builds) and persists. */
    public synchronized void set(@Nonnull String id, @Nonnull Method method, @Nonnull String version, @Nonnull String commit, boolean restartPending) {
        config.set(id + ".method", method.name());
        config.set(id + ".version", version);
        config.set(id + ".commit", commit);
        config.set(id + ".restart-pending", restartPending);
        save();
    }

    /** All entry ids the installer has staged at least once. */
    @Nonnull
    public synchronized Set<String> getTrackedIds() {
        return config.getKeys(false);
    }

    /** Forgets an entry entirely. Used when the user deletes an addon through the installer. */
    public synchronized void remove(@Nonnull String id) {
        if (config.contains(id)) {
            config.set(id, null);
            save();
        }
    }

    /** Clears a pending restart for an entry while keeping its recorded method/version. */
    public synchronized void clearRestartPending(@Nonnull String id) {
        if (config.contains(id) && config.getBoolean(id + ".restart-pending", false)) {
            config.set(id + ".restart-pending", false);
            save();
        }
    }

    private void save() {
        try {
            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            config.save(file);
        } catch (IOException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to save addon-installer.yml: {0}", e.getMessage());
        }
    }
}
