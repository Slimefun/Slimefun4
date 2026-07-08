package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
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

    /** Live download progress in [0,1] per in-flight entry id, or -1 for an indeterminate stage. */
    private final java.util.Map<String, Double> progress = new ConcurrentHashMap<>();

    /** Cached update-check results, refreshed lazily when an entry's detail menu opens. */
    private final java.util.Map<String, String> updateLabels = new ConcurrentHashMap<>();
    private final java.util.Map<String, Long> lastChecked = new ConcurrentHashMap<>();

    /** Don't re-hit GitHub for the same entry more than once per this window (rate-limit friendly). */
    private static final long UPDATE_CHECK_THROTTLE_MS = 10 * 60 * 1000L;

    /** Admins already shown the startup update summary this server session (so we tell them once). */
    private final Set<java.util.UUID> notifiedPlayers = ConcurrentHashMap.newKeySet();

    /** "Networks 1.0.2, …" summary built by the startup check, or null when nothing is pending. */
    private volatile String pendingUpdateSummary = null;

    /** A snapshot of one entry's install identity, captured on the main thread before checking async. */
    private static final class UpdateProbe {
        private final AddonCatalog.Entry entry;
        private final InstallState.Method method;
        private final String installed;
        private final String commit;

        UpdateProbe(AddonCatalog.Entry entry, InstallState.Method method, String installed, String commit) {
            this.entry = entry;
            this.method = method;
            this.installed = installed;
            this.commit = commit;
        }
    }

    public AddonInstaller(@Nonnull InstallState state) {
        this.state = state;
        loadVersionCache();
    }

    @Nonnull
    public InstallState getState() {
        return state;
    }

    public boolean isInProgress(@Nonnull String id) {
        return inProgress.contains(id);
    }

    /**
     * The download progress for an in-flight entry: a fraction in [0,1], or -1 when the stage is
     * indeterminate (resolving the release, or a server that sent no content length). Returns -1
     * when nothing is in flight for this id.
     */
    public double getProgress(@Nonnull String id) {
        Double value = progress.get(id);
        return value != null ? value : -1;
    }

    /**
     * Clears stale "restart pending" flags. An entry flagged restart-pending whose plugin is now
     * loaded was activated by the restart that just happened, so the badge should no longer nag.
     * Must run after all plugins have enabled (e.g. once the server has finished loading).
     */
    public void reconcileRestartFlags() {
        // This runs once per startup — i.e. AFTER a (re)start. Any "restart to apply" flag was set in a
        // PREVIOUS session, so the restart it was waiting for has now happened: clear it unconditionally.
        // If the addon loaded, its badge becomes "Installed"; if it didn't (failed/removed jar), it becomes
        // "Not installed" — either way "restart to apply" is stale and must not nag on a fresh boot.
        for (String id : state.getTrackedIds()) {
            InstallState.Record record = state.get(id);

            if (record != null && record.isRestartPending()) {
                state.clearRestartPending(id);
            }
        }
    }

    /** True when a plugin matching the entry's plugin name is currently loaded. */
    public boolean isLoaded(@Nonnull AddonCatalog.Entry entry) {
        if (entry.isCore()) {
            return true;
        }

        // Libraries (e.g. InfinityLib) ship no plugin.yml and so never appear as loaded plugins. Treat
        // one as present when any addon that depends on it is loaded - its classes are then on the path.
        if (entry.isLibrary()) {
            for (AddonCatalog.Entry candidate : AddonCatalog.getEntries()) {
                if (candidate.getDependencies().contains(entry.getId()) && isLoaded(candidate)) {
                    return true;
                }
            }

            return false;
        }

        for (Plugin plugin : Slimefun.instance().getServer().getPluginManager().getPlugins()) {
            if (plugin.getName().equalsIgnoreCase(entry.getPluginName()) || plugin.getName().equalsIgnoreCase(entry.getRepo())) {
                return true;
            }
        }

        return false;
    }

    /** The loaded Bukkit {@link Plugin} matching this entry, or null when not loaded / not a plugin. */
    @javax.annotation.Nullable
    public Plugin getLoadedPlugin(@Nonnull AddonCatalog.Entry entry) {
        if (entry.isCore()) {
            return Slimefun.instance();
        }

        for (Plugin plugin : Slimefun.instance().getServer().getPluginManager().getPlugins()) {
            if (plugin.getName().equalsIgnoreCase(entry.getPluginName()) || plugin.getName().equalsIgnoreCase(entry.getRepo())) {
                return plugin;
            }
        }

        return null;
    }

    /**
     * Whether the currently-loaded jar for this entry is an unofficial (from-source / orchestrator)
     * build rather than a published release, judged from the jar's own version suffix (ground truth).
     * A stale {@link InstallState.Record} can wrongly claim RELEASE after the orchestrator overwrote a
     * once-installed jar with a source build, so the loaded version wins over the record.
     */
    public boolean isUnofficialBuild(@Nonnull AddonCatalog.Entry entry) {
        Plugin plugin = getLoadedPlugin(entry);

        if (plugin == null) {
            return false;
        }

        String version = plugin.getDescription().getVersion();
        return version.contains("-UNOFFICIAL") || version.contains("-EXPERIMENTAL");
    }

    /** True when the last update-check found a newer release/branch head for this entry. */
    public boolean isUpdateAvailable(@Nonnull String id) {
        return updateLabels.containsKey(id);
    }

    /** The available version/commit label for an entry with a pending update, or "" if none. */
    @Nonnull
    public String getLatestVersionLabel(@Nonnull String id) {
        String label = updateLabels.get(id);
        return label != null ? label : "";
    }

    /**
     * Refreshes the cached update status for installer-managed entries (UI only, no chat). Bukkit-API
     * state is read on the calling thread; the network calls run async, throttled per entry.
     */
    public void refreshUpdateStatusAsync(@Nonnull List<AddonCatalog.Entry> entries) {
        refreshUpdateStatusAsync(entries, null);
    }

    public void refreshUpdateStatusAsync(@Nonnull List<AddonCatalog.Entry> entries, @javax.annotation.Nullable Runnable onComplete) {
        long now = System.currentTimeMillis();
        List<UpdateProbe> probes = new ArrayList<>();

        for (AddonCatalog.Entry entry : entries) {
            if (entry.isLibrary() || !isLoaded(entry)) {
                continue;
            }

            InstallState.Record record = state.get(entry.getId());

            // Only the installer can judge updates for what IT installed. A custom/local build (e.g.
            // core, or any orchestrator-deployed addon) has no record and no known upstream ref, so
            // comparing it to release tags gives false positives — skip those entirely. Also skip when
            // the LOADED jar is an unofficial/source build (its version carries a -UNOFFICIAL suffix):
            // a stale record may still claim RELEASE, but a from-source build must never nag about a
            // release that it is already newer than.
            if (record == null || isUnofficialBuild(entry)) {
                continue;
            }

            Long last = lastChecked.get(entry.getId());

            if (last != null && now - last < UPDATE_CHECK_THROTTLE_MS) {
                continue;
            }

            lastChecked.put(entry.getId(), now);
            probes.add(new UpdateProbe(entry, record.getMethod(), record.getVersion(), record.getCommit()));
        }

        if (probes.isEmpty()) {
            if (onComplete != null) {
                Slimefun.runSync(onComplete);
            }
            return;
        }

        runAsync(() -> {
            for (UpdateProbe probe : probes) {
                try {
                    computeAndStore(probe);
                } catch (RuntimeException ignored) {
                    // A single failed check must not abort the rest.
                }
            }

            if (onComplete != null) {
                Slimefun.runSync(onComplete);
            }
        });
    }

    private void computeAndStore(@Nonnull UpdateProbe probe) {
        boolean available;
        String label;

        if (probe.method == InstallState.Method.BRANCH) {
            // For a branch install, the head moving past the recorded commit means there's an update.
            String head = releaseService.fetchBranchHead(probe.entry, probe.installed);

            if (head == null) {
                return;
            }

            available = !probe.commit.contains(head);
            label = probe.installed + " @ " + head;
        } else {
            AddonReleaseService.ReleaseInfo info = releaseService.fetchLatest(probe.entry);

            if (info == null) {
                // No release published (or GitHub unreachable) — leave the cached state untouched.
                return;
            }

            latestTags.put(probe.entry.getId(), info.getTag());
            available = !normalizeVersion(info.getTag()).equals(normalizeVersion(probe.installed));
            label = info.getTag();
        }

        if (available) {
            updateLabels.put(probe.entry.getId(), label);
        } else {
            updateLabels.remove(probe.entry.getId());
        }
    }

    /**
     * Runs once after the server finishes loading: checks every managed entry, logs a console summary,
     * and messages any online admins. The same summary is shown to admins on their first join this
     * session (see {@link #announceTo(Player)}). Nothing is shown when no managed install has an update.
     */
    public void checkForUpdatesOnStartup() {
        notifiedPlayers.clear();
        refreshUpdateStatusAsync(AddonCatalog.getEntries(), () -> {
            List<String> updates = new ArrayList<>();

            for (AddonCatalog.Entry entry : AddonCatalog.getEntries()) {
                String label = updateLabels.get(entry.getId());

                if (label != null) {
                    updates.add(entry.getDisplayName() + " " + label);
                }
            }

            pendingUpdateSummary = updates.isEmpty() ? null : String.join(", ", updates);

            if (pendingUpdateSummary != null) {
                Slimefun.logger().log(java.util.logging.Level.INFO, "Addon/Slimefun updates available: {0}", pendingUpdateSummary);

                for (Player online : org.bukkit.Bukkit.getOnlinePlayers()) {
                    announceTo(online);
                }
            }
        });
    }

    /**
     * Shows the pending update summary to an admin the first time they are seen this session. No-op
     * when nothing is pending, the player lacks the installer permission, or they were already told.
     */
    public void announceTo(@Nonnull Player player) {
        String summary = pendingUpdateSummary;

        if (summary == null || !player.hasPermission(AddonCatalog.PERMISSION)) {
            return;
        }

        if (!notifiedPlayers.add(player.getUniqueId())) {
            return;
        }

        player.sendMessage(ChatColor.GOLD + "⬆ Updates available: " + ChatColor.YELLOW + summary);
        player.sendMessage(ChatColor.GRAY + "Open the Slimefun guide → Settings → Addon Installer to update.");
    }

    /** Strips gh-/v prefixes and any -UNOFFICIAL/-MC build suffix so two version strings compare cleanly. */
    @Nonnull
    private static String normalizeVersion(@Nonnull String raw) {
        String v = raw.trim();
        v = v.replaceFirst("^gh-", "").replaceFirst("^v", "");
        int suffix = v.indexOf("-UNOFFICIAL");

        if (suffix < 0) {
            suffix = v.indexOf("-MC");
        }

        return suffix >= 0 ? v.substring(0, suffix) : v;
    }

    /**
     * Installs (or updates) an entry from its latest release, plus any missing hard dependencies.
     * Runs entirely off the main thread; messages the player on completion.
     */
    public void installRelease(@Nonnull Player player, @Nonnull AddonCatalog.Entry entry) {
        installRelease(player, entry, null);
    }

    /**
     * As {@link #installRelease(Player, AddonCatalog.Entry)}, but invokes {@code onComplete} on the main
     * thread when finished (true = success), so a menu can re-render with in-GUI feedback.
     */
    public void installRelease(@Nonnull Player player, @Nonnull AddonCatalog.Entry entry, @javax.annotation.Nullable java.util.function.Consumer<Boolean> onComplete) {
        installRelease(player, entry, onComplete, null);
    }

    /**
     * As {@link #installRelease(Player, AddonCatalog.Entry, java.util.function.Consumer)}, but also
     * invokes {@code onProgress} on the main thread as bytes arrive, with a fraction in [0,1] (or -1
     * for an indeterminate stage), so a menu can render a live progress bar for the clicked entry.
     */
    public void installRelease(@Nonnull Player player, @Nonnull AddonCatalog.Entry entry, @javax.annotation.Nullable java.util.function.Consumer<Boolean> onComplete, @javax.annotation.Nullable Runnable onProgress) {
        installRelease(player, entry, onComplete, onProgress, null);
    }

    /**
     * As {@link #installRelease(Player, AddonCatalog.Entry, java.util.function.Consumer, Runnable)}, but
     * installs a specific release {@code primaryOverride} for {@code entry} (rather than the latest),
     * powering the version picker / downgrade. Dependencies still resolve to their latest release.
     */
    public void installRelease(@Nonnull Player player, @Nonnull AddonCatalog.Entry entry, @javax.annotation.Nullable java.util.function.Consumer<Boolean> onComplete, @javax.annotation.Nullable Runnable onProgress, @javax.annotation.Nullable AddonReleaseService.ReleaseInfo primaryOverride) {
        // Resolve everything that touches the Bukkit API (isLoaded -> getPlugins, jar file names) on the
        // calling (main) thread, then reserve all ids before going async.
        List<AddonCatalog.Entry> targets = new ArrayList<>();

        for (AddonCatalog.Entry dep : AddonCatalog.resolveDependencies(entry)) {
            // Libraries (InfinityLib) are shaded into the addons that need them, so they must never be
            // installed as a separate jar — doing so downloads a plugin.yml-less jar that can't load.
            if (!dep.isLibrary() && !isLoaded(dep)) {
                targets.add(dep);
            }
        }

        targets.add(entry);

        Set<String> loadedIds = new HashSet<>();
        java.util.Map<String, String> loadedJarNames = new java.util.HashMap<>();

        for (AddonCatalog.Entry target : targets) {
            if (isLoaded(target)) {
                loadedIds.add(target.getId());
                File jar = locateJar(target);

                if (jar != null) {
                    loadedJarNames.put(target.getId(), jar.getName());
                }
            }
        }

        // In-game updates rely on Bukkit's update folder, which only swaps a jar that lives in /plugins.
        // Some dev/launcher layouts (e.g. paperweight's runServer) load a plugin from elsewhere; staging an
        // update into /plugins/update would then never be applied - and the user would see "restart to
        // apply" followed by nothing. Detect that up front and say so plainly.
        if (isLoaded(entry)) {
            File loadedJar = locateJar(entry);

            if (loadedJar != null && !isInPluginsDir(loadedJar)) {
                message(player, ChatColor.RED + "✖ " + entry.getDisplayName() + " is loaded from outside /plugins"
                    + " (" + loadedJar.getParent() + "), so it can't be updated from in-game on this setup.");
                message(player, ChatColor.GRAY + "Update it through your build/launcher instead. (Real servers with the jar in /plugins update normally.)");
                return;
            }
        }

        if (!reserve(targets)) {
            // A dependency (or this entry) is already being installed. Say so instead of no-op'ing —
            // a silent return here is what made a grid right-click look like it did nothing.
            message(player, ChatColor.YELLOW + "⏳ " + entry.getDisplayName() + " is already installing…");
            return;
        }

        // The entry the player clicked is the one whose progress bar we surface in the menu.
        String progressId = entry.getId();
        progress.put(progressId, -1.0);

        runAsync(() -> {
            List<String> staged = new ArrayList<>();
            boolean failure = false;

            try {
                for (AddonCatalog.Entry target : targets) {
                    // The clicked entry may pin a specific release (version picker / downgrade); deps
                    // always resolve to their latest.
                    AddonReleaseService.ReleaseInfo info = (primaryOverride != null && target.getId().equals(entry.getId()))
                        ? primaryOverride
                        : releaseService.fetchLatest(target);

                    if (info == null) {
                        message(player, ChatColor.RED + "✖ " + target.getDisplayName() + " has no published release yet.");
                        failure = true;
                        break;
                    }

                    boolean loaded = loadedIds.contains(target.getId());
                    File dir = InstallTargets.targetDir(loaded);
                    // One naming scheme everywhere: "<Repo>-<version>.jar" with NO v/gh- prefix, matching the
                    // release asset "Slimefun-5.2.4.3.jar". A fresh install uses that; an update must reuse the
                    // loaded jar's own file name so Bukkit's (filename-matched) update folder swaps it in.
                    String fileName = loaded
                        ? loadedJarNames.getOrDefault(target.getId(), target.getRepo() + ".jar")
                        : target.getRepo() + "-" + stripVersionPrefix(info.getTag()) + ".jar";
                    boolean ok = releaseService.downloadJar(info.getJarUrl(), dir, fileName, fraction -> {
                        progress.put(progressId, fraction);

                        if (onProgress != null) {
                            Slimefun.runSync(onProgress);
                        }
                    });

                    if (!ok) {
                        message(player, ChatColor.RED + "✖ Failed to download " + target.getDisplayName() + ".");
                        failure = true;
                        break;
                    }

                    // Make the downloaded jar run on this fork: strip any bundled core classes
                    // (same repair run.ps1 does). Never on core (it IS the core).
                    if (!target.isCore()) {
                        AddonJarProcessor.repair(new File(dir, fileName));
                    }

                    latestTags.put(target.getId(), info.getTag());
                    state.set(target.getId(), InstallState.Method.RELEASE, info.getTag(), true);
                    staged.add(target.getDisplayName() + " " + info.getTag());
                }
            } catch (AddonReleaseService.RateLimitException e) {
                failure = true;
                message(player, ChatColor.RED + "✖ GitHub rate limit reached (60 requests/hour without a token).");
                message(player, ChatColor.GRAY + "Set " + ChatColor.YELLOW + "installer.github-token" + ChatColor.GRAY + " in config.yml (raises it to 5000/hour), or wait ~an hour.");
            }

            release(targets);
            progress.remove(progressId);
            saveVersionCache(); // persist any tags learned during this install
            boolean success = !failure;

            if (success) {
                message(player, ChatColor.GREEN + "✔ Staged: " + String.join(", ", staged) + ChatColor.GRAY + " — restart the server to apply.");
            }

            if (onComplete != null) {
                Slimefun.runSync(() -> onComplete.accept(success));
            }
        });
    }

    /** Latest release tag per entry, cached from update-checks and installs, for showing "Install v…" upfront. */
    private final java.util.Map<String, String> latestTags = new ConcurrentHashMap<>();

    /** Persisted so versions survive restarts (no re-fetching every boot). Refreshed at most per TTL. */
    private static final long WARM_TTL_MS = 6L * 60 * 60 * 1000;
    private volatile long lastWarm = 0L;

    /** The cached latest release tag for an entry, or "" if not yet fetched. */
    @Nonnull
    public String getCachedLatestTag(@Nonnull String id) {
        return latestTags.getOrDefault(id, "");
    }

    /** The version cache file (plugins/Slimefun/installer-versions.yml). */
    @Nonnull
    private File versionCacheFile() {
        return new File(Slimefun.instance().getDataFolder(), "installer-versions.yml");
    }

    /** Loads persisted latest-tags + the last-warm time. Called once on construction. */
    private void loadVersionCache() {
        File file = versionCacheFile();

        if (!file.exists()) {
            return;
        }

        org.bukkit.configuration.file.YamlConfiguration yaml = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(file);
        lastWarm = yaml.getLong("checked", 0L);
        org.bukkit.configuration.ConfigurationSection section = yaml.getConfigurationSection("versions");

        if (section != null) {
            for (String id : section.getKeys(false)) {
                String tag = section.getString(id);

                if (tag != null && !tag.isEmpty()) {
                    latestTags.put(id, tag);
                }
            }
        }
    }

    private synchronized void saveVersionCache() {
        org.bukkit.configuration.file.YamlConfiguration yaml = new org.bukkit.configuration.file.YamlConfiguration();
        yaml.set("checked", lastWarm);

        for (java.util.Map.Entry<String, String> entry : latestTags.entrySet()) {
            yaml.set("versions." + entry.getKey(), entry.getValue());
        }

        try {
            yaml.save(versionCacheFile());
        } catch (java.io.IOException ignored) {
            // A failed cache write is non-fatal — we just re-fetch next time.
        }
    }

    /**
     * Fills in any missing latest-tags (and refreshes everything once per {@link #WARM_TTL_MS}) so the grid
     * can show versions. Fetches sequentially with a small gap and STOPS immediately on a rate-limit, using
     * whatever it already had. Persists the result. Runs {@code onDone} on the main thread if anything
     * changed. A no-op (no network) when the cache is fresh and complete.
     */
    public void warmLatestTagsAsync(@Nonnull List<AddonCatalog.Entry> entries, @javax.annotation.Nullable Runnable onDone) {
        boolean stale = System.currentTimeMillis() - lastWarm > WARM_TTL_MS;
        List<AddonCatalog.Entry> todo = new ArrayList<>();

        for (AddonCatalog.Entry entry : entries) {
            if (entry.isLibrary() || entry.isCore()) {
                continue; // libraries ship no release; core self-updates via its own path
            }

            if (stale || !latestTags.containsKey(entry.getId())) {
                todo.add(entry);
            }
        }

        if (todo.isEmpty()) {
            return;
        }

        runAsync(() -> {
            boolean changed = false;

            try {
                for (AddonCatalog.Entry entry : todo) {
                    AddonReleaseService.ReleaseInfo info = releaseService.fetchLatest(entry);

                    if (info != null && !info.getTag().equals(latestTags.get(entry.getId()))) {
                        latestTags.put(entry.getId(), info.getTag());
                        changed = true;
                    }

                    try {
                        Thread.sleep(150L); // gentle pacing so we don't spike the rate limit
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                lastWarm = System.currentTimeMillis();
            } catch (AddonReleaseService.RateLimitException e) {
                // Stop here and keep whatever we managed to fetch; the grid still shows those.
            }

            if (changed) {
                saveVersionCache();
            }

            if (onDone != null) {
                Slimefun.runSync(onDone);
            }
        });
    }

    /**
     * Renders a 10-cell progress bar for a fraction in [0,1], e.g. "&a▰▰▰▰▰▱▱▱▱▱ &750%". A negative
     * fraction (indeterminate stage) yields an empty bar with no percentage.
     */
    @Nonnull
    static String progressBar(double fraction) {
        int cells = 10;
        int filled = fraction < 0 ? 0 : (int) Math.round(Math.min(1.0, fraction) * cells);
        StringBuilder bar = new StringBuilder(ChatColor.GREEN.toString());

        for (int i = 0; i < filled; i++) {
            bar.append('▰');
        }

        bar.append(ChatColor.GRAY);

        for (int i = filled; i < cells; i++) {
            bar.append('▱');
        }

        if (fraction >= 0) {
            bar.append(' ').append((int) Math.round(Math.min(1.0, fraction) * 100)).append('%');
        }

        return bar.toString();
    }

    /** Strips a leading gh-/v so a release tag becomes a bare version (v1.0.2 → 1.0.2), matching jar names. */
    @Nonnull
    static String stripVersionPrefix(@Nonnull String tag) {
        return tag.trim().replaceFirst("^gh-", "").replaceFirst("^v", "");
    }

    /**
     * Fetches (async, cached) the latest release tag for an entry so a menu can show the version it would
     * install. Runs {@code onDone} on the main thread once cached (only if the tag changed/first arrived).
     */
    public void fetchLatestTagAsync(@Nonnull AddonCatalog.Entry entry, @javax.annotation.Nullable Runnable onDone) {
        if (latestTags.containsKey(entry.getId())) {
            return; // already known — the menu shows it immediately, no refresh needed
        }

        runAsync(() -> {
            try {
                AddonReleaseService.ReleaseInfo info = releaseService.fetchLatest(entry);

                if (info != null) {
                    latestTags.put(entry.getId(), info.getTag());

                    if (onDone != null) {
                        Slimefun.runSync(onDone);
                    }
                }
            } catch (AddonReleaseService.RateLimitException ignored) {
                // Rate limited — just don't show the version upfront; the install button still works.
            }
        });
    }

    /**
     * Builds an entry from a branch and stages the resulting jar. Dependencies are NOT built from
     * source — they are installed from their latest release if missing.
     */
    public void buildFromBranch(@Nonnull Player player, @Nonnull AddonCatalog.Entry entry, @Nonnull String branch, long timestamp) {
        // Resolve Bukkit-API state on the main thread.
        List<AddonCatalog.Entry> missingDeps = new ArrayList<>();

        for (AddonCatalog.Entry dep : AddonCatalog.resolveDependencies(entry)) {
            // Libraries are shaded into their dependents — never install/build them standalone.
            if (!dep.isLibrary() && !isLoaded(dep)) {
                missingDeps.add(dep);
            }
        }

        boolean entryLoaded = isLoaded(entry);

        List<AddonCatalog.Entry> reserved = new ArrayList<>(missingDeps);
        reserved.add(entry);

        if (!reserve(reserved)) {
            return;
        }

        runAsync(() -> {
            // Release-install any missing hard dependencies first.
            for (AddonCatalog.Entry dep : missingDeps) {
                AddonReleaseService.ReleaseInfo info = releaseService.fetchLatest(dep);

                if (info != null) {
                    File dir = InstallTargets.targetDir(false);
                    String depName = dep.getRepo() + "-" + stripVersionPrefix(info.getTag()) + ".jar";

                    if (releaseService.downloadJar(info.getJarUrl(), dir, depName)) {
                        AddonJarProcessor.repair(new File(dir, depName));
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

                release(reserved);
                return;
            }

            File dir = InstallTargets.targetDir(entryLoaded);
            File dest = new File(dir, entry.getRepo() + ".jar");
            boolean copied = copy(result.getJar(), dest);

            if (copied) {
                AddonJarProcessor.repair(dest); // strip bundled core classes the addon must not carry
            }

            release(reserved);

            if (copied) {
                state.set(entry.getId(), InstallState.Method.BRANCH, branch, result.getDescribe(), true);
                message(player, ChatColor.GREEN + "✔ Built " + entry.getDisplayName() + " (" + branch + ") — restart the server to apply.");
            } else {
                message(player, ChatColor.RED + "✖ Build succeeded but staging the jar failed.");
            }
        });
    }

    /**
     * Deletes a loaded addon's jar from the plugins folder and forgets its install state. The plugin
     * stays in memory until the next restart, so the player is told to restart to fully unload it.
     * Core is never deletable here.
     */
    public void deleteAddon(@Nonnull Player player, @Nonnull AddonCatalog.Entry entry) {
        if (entry.isCore()) {
            message(player, ChatColor.RED + "✖ Slimefun core cannot be deleted here.");
            return;
        }

        File jar = locateJar(entry);

        if (jar == null || !jar.exists()) {
            message(player, ChatColor.RED + "✖ Could not locate the jar for " + entry.getDisplayName() + ".");
            return;
        }

        if (jar.delete()) {
            state.remove(entry.getId());
            updateLabels.remove(entry.getId());
            lastChecked.remove(entry.getId());
            message(player, ChatColor.GREEN + "✔ Deleted " + entry.getDisplayName() + ChatColor.GRAY + " — restart the server to unload it.");
        } else {
            message(player, ChatColor.RED + "✖ Failed to delete " + entry.getDisplayName() + "'s jar (is the file locked?).");
        }
    }

    /**
     * Whether the jar is managed out of /plugins, so Bukkit's (filename-matched) update folder can swap it.
     * True when /plugins is the jar's directory OR any ancestor of it — the latter accepts Paper's remapped
     * layout ({@code plugins/.paper-remapped/<jar>}): the real jar still lives in /plugins and updates
     * normally, only the loaded copy is remapped. A jar loaded from a wholly separate location (e.g. a dev
     * launcher's build dir) has no /plugins ancestor and is correctly rejected.
     */
    private static boolean isInPluginsDir(@Nonnull File jar) {
        File pluginsDir;
        File dir;

        try {
            pluginsDir = InstallTargets.pluginsDir().getCanonicalFile();
            dir = jar.getCanonicalFile().getParentFile();
        } catch (java.io.IOException e) {
            pluginsDir = InstallTargets.pluginsDir().getAbsoluteFile();
            dir = jar.getAbsoluteFile().getParentFile();
        }

        while (dir != null) {
            if (dir.equals(pluginsDir)) {
                return true;
            }

            dir = dir.getParentFile();
        }

        return false;
    }

    /** Best-effort location of an entry's jar: the loaded plugin's own file, else a staged repo.jar. */
    @javax.annotation.Nullable
    private File locateJar(@Nonnull AddonCatalog.Entry entry) {
        Plugin plugin = getLoadedPlugin(entry);

        if (plugin instanceof org.bukkit.plugin.java.JavaPlugin) {
            try {
                java.lang.reflect.Method getFile = org.bukkit.plugin.java.JavaPlugin.class.getDeclaredMethod("getFile");
                getFile.setAccessible(true);
                Object file = getFile.invoke(plugin);

                if (file instanceof File && ((File) file).exists()) {
                    return (File) file;
                }
            } catch (ReflectiveOperationException ignored) {
                // Fall through to the staged-name guess below.
            }
        }

        File staged = new File(InstallTargets.pluginsDir(), entry.getRepo() + ".jar");
        return staged.exists() ? staged : null;
    }

    /** Reserves all ids in inProgress atomically; returns false (and rolls back) if any is already in flight. */
    private boolean reserve(List<AddonCatalog.Entry> entries) {
        List<String> added = new ArrayList<>();

        for (AddonCatalog.Entry e : entries) {
            if (inProgress.add(e.getId())) {
                added.add(e.getId());
            } else {
                inProgress.removeAll(added);
                return false;
            }
        }

        return true;
    }

    private void release(List<AddonCatalog.Entry> entries) {
        for (AddonCatalog.Entry e : entries) {
            inProgress.remove(e.getId());
        }
    }

    private static boolean copy(File from, File to) {
        File tmp = new File(to.getParentFile(), to.getName() + ".tmp");

        try {
            try (java.io.InputStream in = new java.io.FileInputStream(from); java.io.OutputStream out = new java.io.FileOutputStream(tmp)) {
                byte[] buffer = new byte[8192];
                int read;

                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            }

            if (to.exists() && !to.delete()) {
                tmp.delete();
                return false;
            }

            return tmp.renameTo(to);
        } catch (java.io.IOException e) {
            tmp.delete();
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
