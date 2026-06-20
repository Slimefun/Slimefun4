package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Computes the one-line status badge shown for an entry, from local state only (no network).
 * The "update available" comparison against the latest release happens in the detail menu, which
 * fetches live; the grid shows installed/loaded/working/restart-pending from local knowledge.
 */
final class StatusBadges {

    private StatusBadges() {}

    @Nonnull
    static String badge(@Nonnull AddonInstaller inst, @Nonnull AddonCatalog.Entry entry) {
        if (inst.isInProgress(entry.getId())) {
            return "&e⚙ Working…";
        }

        InstallState.Record record = inst.getState().get(entry.getId());

        if (record != null && record.isRestartPending()) {
            return "&b↻ Restart to apply (" + record.getVersion() + ")";
        }

        if (entry.isCore()) {
            return "&a✔ Installed v" + io.github.thebusybiscuit.slimefun5.implementation.Slimefun.getVersion();
        }

        if (inst.isLoaded(entry)) {
            return "&a✔ Installed";
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
            return "&7＋ Not installed &8(needs " + String.join(", ", missing) + ")";
        }

        return "&7＋ Not installed";
    }
}
