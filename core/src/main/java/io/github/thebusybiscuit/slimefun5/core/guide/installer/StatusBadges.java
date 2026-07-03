package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Computes the one-line status badge shown for an entry, from local state only (no network).
 * The "update available" comparison against the latest release happens in the detail menu, which
 * fetches live; the grid shows installed/loaded/working/restart-pending from local knowledge.
 */
final class StatusBadges {

    private StatusBadges() {}

    @Nonnull
    static String badge(@Nonnull Player p, @Nonnull AddonInstaller inst, @Nonnull AddonCatalog.Entry entry) {
        if (inst.isInProgress(entry.getId())) {
            return Slimefun.getLocalization().getMessage(p, "guide.installer.badge.working");
        }

        InstallState.Record record = inst.getState().get(entry.getId());

        if (record != null && record.isRestartPending()) {
            return Slimefun.getLocalization().getMessage(p, "guide.installer.badge.restart").replace("%version%", record.getVersion());
        }

        // Core and addons alike show a plain "Installed" badge here; the version is rendered once,
        // in the detail menu's version line, so it never appears twice.
        if (inst.isLoaded(entry)) {
            if (inst.isUpdateAvailable(entry.getId())) {
                return Slimefun.getLocalization().getMessage(p, "guide.installer.badge.update").replace("%version%", inst.getLatestVersionLabel(entry.getId()));
            }

            return Slimefun.getLocalization().getMessage(p, "guide.installer.badge.installed");
        }

        // Local-only "requires X": list hard dependencies that aren't loaded yet. (They are auto-
        // installed on click, but surfacing the requirement up front matches the spec's badge table.)
        List<String> missing = new ArrayList<>();

        for (AddonCatalog.Entry dep : AddonCatalog.resolveDependencies(entry)) {
            if (!inst.isLoaded(dep)) {
                missing.add(dep.getDisplayName());
            }
        }

        if (!missing.isEmpty()) {
            return Slimefun.getLocalization().getMessage(p, "guide.installer.badge.not-installed-deps").replace("%deps%", String.join(", ", missing));
        }

        return Slimefun.getLocalization().getMessage(p, "guide.installer.badge.not-installed");
    }

    /**
     * The one-line install-source badge for a loaded entry: a published release, a from-source
     * branch build, or an unofficial/local jar the installer never staged (no {@link InstallState.Record}).
     */
    @Nonnull
    static String sourceLine(@Nonnull Player p, @Nonnull AddonInstaller inst, @Nonnull AddonCatalog.Entry entry) {
        // Ground truth first: a loaded jar with a -UNOFFICIAL/-EXPERIMENTAL build suffix is an unofficial
        // build, even if a stale InstallState.Record left over from an earlier install still claims RELEASE.
        if (inst.isUnofficialBuild(entry)) {
            return Slimefun.getLocalization().getMessage(p, "guide.installer.source.unofficial");
        }

        InstallState.Record record = inst.getState().get(entry.getId());

        if (record != null && record.getMethod() == InstallState.Method.BRANCH) {
            return Slimefun.getLocalization().getMessage(p, "guide.installer.source.branch").replace("%branch%", record.getVersion());
        }

        if (record != null && record.getMethod() == InstallState.Method.RELEASE) {
            return Slimefun.getLocalization().getMessage(p, "guide.installer.source.release");
        }

        return Slimefun.getLocalization().getMessage(p, "guide.installer.source.unofficial");
    }
}
