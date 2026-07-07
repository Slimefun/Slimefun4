package io.github.thebusybiscuit.slimefun5.core.balance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Runtime entry point for balance data. Resolves an item's final score (authored override else
 * heuristic; trivial items excluded) and aggregates per-addon summaries live from the item registry.
 * On-demand with a small per-addon override cache; no plugin-lifecycle wiring required.
 */
public final class BalanceService {

    private static final BalanceService INSTANCE = new BalanceService();

    private final ItemBalanceHeuristic heuristic = new ItemBalanceHeuristic();
    private final ItemEffortHeuristic effortHeuristic = new ItemEffortHeuristic();
    // Concurrent because the caches are pre-warmed on an async thread at boot (see Slimefun#onPluginStart)
    // while the main thread may also read them (installer open); ConcurrentHashMap makes that race-free.
    private final Map<String, BalanceOverrides> overridesByAddon = new java.util.concurrent.ConcurrentHashMap<>();
    // Effort walks the recipe tree (expensive) and per-addon summaries scan the whole registry; both are
    // static once items/recipes are registered, so memoize them. Without this, opening the installer
    // recomputed every item's recipe-tree effort on the main thread and froze the server for seconds.
    private final Map<String, Integer> effortCache = new java.util.concurrent.ConcurrentHashMap<>();
    private final Map<String, AddonBalanceSummary> summaryCache = new java.util.concurrent.ConcurrentHashMap<>();

    private BalanceService() {}

    @Nonnull
    public static BalanceService instance() {
        return INSTANCE;
    }

    private BalanceOverrides overridesFor(@Nonnull SlimefunAddon addon) {
        BalanceOverrides cached = overridesByAddon.get(addon.getName());

        if (cached == null) {
            cached = BalanceOverrides.load(addon);
            overridesByAddon.put(addon.getName(), cached);
        }

        return cached;
    }

    /** True if the item is a trivial resource (authored trivial flag or heuristic) and is skipped. */
    public boolean isTrivial(@Nonnull SlimefunItem item) {
        BalanceOverrides overrides = overridesFor(item.getAddon());

        if (overrides.isTrivial(item.getId())) {
            return true;
        }

        // An explicit score override forces the item to be scored even if the heuristic would skip it.
        if (overrides.getScore(item.getId()) != null) {
            return false;
        }

        return heuristic.isTrivial(item);
    }

    /** Final POWER score for a non-trivial item: override wins, else heuristic. */
    @Nonnull
    public BalanceScore scoreOf(@Nonnull SlimefunItem item) {
        Integer override = overridesFor(item.getAddon()).getScore(item.getId());
        return BalanceScore.of(override != null ? override.intValue() : heuristic.estimate(item));
    }

    /** Auto-computed EFFORT (0-100): how hard the item is to obtain, from its recipe tree/gates. Memoized. */
    public int effortOf(@Nonnull SlimefunItem item) {
        Integer cached = effortCache.get(item.getId());

        if (cached != null) {
            return cached.intValue();
        }

        int effort = effortHeuristic.estimate(item);
        effortCache.put(item.getId(), effort);
        return effort;
    }

    /** The balance verdict combining the item's power score with its effort to obtain. */
    @Nonnull
    public BalanceVerdict verdictOf(@Nonnull SlimefunItem item) {
        return BalanceVerdict.from(scoreOf(item).getScore(), effortOf(item));
    }

    /**
     * Pre-computes the effort + per-addon summary caches once (the heavy recipe-tree walks), so the admin
     * installer never has to do them on the main thread when a player opens it. Call from a delayed boot
     * task after all items have registered; safe to call again (cached calls are cheap).
     */
    public void warmCache() {
        Set<String> addons = new TreeSet<>();

        for (SlimefunItem item : Slimefun.getRegistry().getAllSlimefunItems()) {
            addons.add(item.getAddon().getName());
        }

        for (String addon : addons) {
            summarize(addon);
        }
    }

    /** Live summary for one addon by its {@link SlimefunAddon#getName()}. */
    @Nonnull
    public AddonBalanceSummary summarize(@Nonnull String addonName) {
        AddonBalanceSummary cached = summaryCache.get(addonName);

        if (cached != null) {
            return cached;
        }

        List<Integer> scores = new ArrayList<>();
        int overpowered = 0;

        for (SlimefunItem item : Slimefun.getRegistry().getAllSlimefunItems()) {
            if (item.getAddon().getName().equals(addonName) && !isTrivial(item)) {
                scores.add(scoreOf(item).getScore());

                // OP count is verdict-based (strong AND cheap), not merely power >= 70, so a hard-earned
                // endgame item no longer inflates the "overpowered" tally.
                if (verdictOf(item) == BalanceVerdict.OVERPOWERED) {
                    overpowered++;
                }
            }
        }

        AddonBalanceSummary summary = AddonBalanceSummary.of(scores, overpowered);
        summaryCache.put(addonName, summary);
        return summary;
    }

    /** The strongest non-trivial items of an addon, highest score first (for the drill-down menu). */
    @Nonnull
    public List<SlimefunItem> topItems(@Nonnull String addonName, int limit) {
        List<SlimefunItem> items = new ArrayList<>();

        for (SlimefunItem item : Slimefun.getRegistry().getAllSlimefunItems()) {
            if (item.getAddon().getName().equals(addonName) && !isTrivial(item)) {
                items.add(item);
            }
        }

        items.sort(new Comparator<SlimefunItem>() {
            @Override
            public int compare(SlimefunItem a, SlimefunItem b) {
                return Integer.compare(scoreOf(b).getScore(), scoreOf(a).getScore());
            }
        });

        return items.size() > limit ? new ArrayList<>(items.subList(0, limit)) : items;
    }

    /**
     * Names of loaded addons that registered items but are not in the given set of catalog plugin names
     * (nor Slimefun core) - i.e. custom/third-party addons to surface in the installer.
     */
    @Nonnull
    public Set<String> detectedAddonNames(@Nonnull Set<String> catalogPluginNames) {
        Set<String> detected = new TreeSet<>();

        for (SlimefunItem item : Slimefun.getRegistry().getAllSlimefunItems()) {
            String name = item.getAddon().getName();

            if (!name.equals("Slimefun") && !catalogPluginNames.contains(name)) {
                detected.add(name);
            }
        }

        return detected;
    }
}
