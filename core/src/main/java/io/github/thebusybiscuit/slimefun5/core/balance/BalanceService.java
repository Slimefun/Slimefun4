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
    private final Map<String, BalanceOverrides> overridesByAddon = new HashMap<>();

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

    /** Final score for a non-trivial item: override wins, else heuristic. */
    @Nonnull
    public BalanceScore scoreOf(@Nonnull SlimefunItem item) {
        Integer override = overridesFor(item.getAddon()).getScore(item.getId());
        return BalanceScore.of(override != null ? override.intValue() : heuristic.estimate(item));
    }

    /** Live summary for one addon by its {@link SlimefunAddon#getName()}. */
    @Nonnull
    public AddonBalanceSummary summarize(@Nonnull String addonName) {
        List<Integer> scores = new ArrayList<>();

        for (SlimefunItem item : Slimefun.getRegistry().getAllSlimefunItems()) {
            if (item.getAddon().getName().equals(addonName) && !isTrivial(item)) {
                scores.add(scoreOf(item).getScore());
            }
        }

        return AddonBalanceSummary.of(scores);
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
