package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Orchestrates installs/updates/builds: dependency resolution, async download or build, state
 * updates, and player feedback. Menus call this; it never touches the UI directly beyond chat
 * messages.
 */
public final class AddonInstaller {

    private final AddonReleaseService releaseService = new AddonReleaseService();
    private final AddonSourceBuilder sourceBuilder = new AddonSourceBuilder();
    private final InstallState state;

    /** Entry ids with an install/build currently in flight, for the "working" badge. */
    private final Set<String> inProgress = ConcurrentHashMap.newKeySet();

    public AddonInstaller(@Nonnull InstallState state) {
        this.state = state;
    }

    @Nonnull
    public InstallState getState() {
        return state;
    }

    public boolean isInProgress(@Nonnull String id) {
        return inProgress.contains(id);
    }

    /** True when a plugin matching the entry's display/jar name is currently loaded. */
    public boolean isLoaded(@Nonnull AddonCatalog.Entry entry) {
        if (entry.isCore()) {
            return true;
        }

        for (Plugin plugin : Slimefun.instance().getServer().getPluginManager().getPlugins()) {
            if (plugin.getName().equalsIgnoreCase(entry.getRepo())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Installs (or updates) an entry from its latest release, plus any missing hard dependencies.
     * Runs entirely off the main thread; messages the player on completion.
     */
    public void installRelease(@Nonnull Player player, @Nonnull AddonCatalog.Entry entry) {
        if (!inProgress.add(entry.getId())) {
            return;
        }

        runAsync(() -> {
            List<AddonCatalog.Entry> toInstall = new ArrayList<>();

            for (AddonCatalog.Entry dep : AddonCatalog.resolveDependencies(entry)) {
                if (!isLoaded(dep)) {
                    toInstall.add(dep);
                }
            }

            toInstall.add(entry);

            List<String> staged = new ArrayList<>();
            boolean failure = false;

            for (AddonCatalog.Entry target : toInstall) {
                AddonReleaseService.ReleaseInfo info = releaseService.fetchLatest(target);

                if (info == null) {
                    message(player, ChatColor.RED + "✖ " + target.getDisplayName() + " is unavailable (no release / GitHub unreachable).");
                    failure = true;
                    break;
                }

                File dir = InstallTargets.targetDir(isLoaded(target));
                boolean ok = releaseService.downloadJar(info.getJarUrl(), dir, target.getRepo() + ".jar");

                if (!ok) {
                    message(player, ChatColor.RED + "✖ Failed to download " + target.getDisplayName() + ".");
                    failure = true;
                    break;
                }

                state.set(target.getId(), InstallState.Method.RELEASE, info.getTag(), true);
                staged.add(target.getDisplayName() + " " + info.getTag());
            }

            inProgress.remove(entry.getId());

            if (!failure) {
                message(player, ChatColor.GREEN + "✔ Staged: " + String.join(", ", staged) + ChatColor.GRAY + " — restart the server to apply.");
            }
        });
    }

    /**
     * Builds an entry from a branch and stages the resulting jar. Dependencies are NOT built from
     * source — they are installed from their latest release if missing.
     */
    public void buildFromBranch(@Nonnull Player player, @Nonnull AddonCatalog.Entry entry, @Nonnull String branch, long timestamp) {
        if (!inProgress.add(entry.getId())) {
            return;
        }

        runAsync(() -> {
            // Release-install any missing hard dependencies first.
            for (AddonCatalog.Entry dep : AddonCatalog.resolveDependencies(entry)) {
                if (!isLoaded(dep)) {
                    AddonReleaseService.ReleaseInfo info = releaseService.fetchLatest(dep);

                    if (info != null) {
                        File dir = InstallTargets.targetDir(false);
                        releaseService.downloadJar(info.getJarUrl(), dir, dep.getRepo() + ".jar");
                        state.set(dep.getId(), InstallState.Method.RELEASE, info.getTag(), true);
                    }
                }
            }

            message(player, ChatColor.YELLOW + "⚙ Building " + entry.getDisplayName() + " from " + branch + "… this can take a few minutes.");
            AddonSourceBuilder.Result result = sourceBuilder.build(entry, branch, timestamp);

            if (!result.isSuccess() || result.getJar() == null) {
                message(player, ChatColor.RED + "✖ Build failed. Log: " + result.getLogFile().getPath());

                for (String line : AddonSourceBuilder.tail(result.getLogFile(), 15)) {
                    message(player, ChatColor.DARK_GRAY + line);
                }

                inProgress.remove(entry.getId());
                return;
            }

            File dir = InstallTargets.targetDir(isLoaded(entry));
            File dest = new File(dir, entry.getRepo() + ".jar");
            boolean copied = copy(result.getJar(), dest);
            inProgress.remove(entry.getId());

            if (copied) {
                state.set(entry.getId(), InstallState.Method.BRANCH, branch, true);
                message(player, ChatColor.GREEN + "✔ Built " + entry.getDisplayName() + " (" + branch + ") — restart the server to apply.");
            } else {
                message(player, ChatColor.RED + "✖ Build succeeded but staging the jar failed.");
            }
        });
    }

    private static boolean copy(File from, File to) {
        try {
            if (to.exists() && !to.delete()) {
                return false;
            }

            try (java.io.InputStream in = new java.io.FileInputStream(from); java.io.OutputStream out = new java.io.FileOutputStream(to)) {
                byte[] buffer = new byte[8192];
                int read;

                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            }

            return true;
        } catch (java.io.IOException e) {
            return false;
        }
    }

    private static void runAsync(Runnable runnable) {
        Slimefun.instance().getServer().getScheduler().runTaskAsynchronously(Slimefun.instance(), runnable);
    }

    private static void message(Player player, String text) {
        Slimefun.instance().getServer().getScheduler().runTask(Slimefun.instance(), () -> {
            if (player.isOnline()) {
                player.sendMessage(text);
            }
        });
    }
}
