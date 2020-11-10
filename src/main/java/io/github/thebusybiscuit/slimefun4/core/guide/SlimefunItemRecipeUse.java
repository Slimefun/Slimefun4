package io.github.thebusybiscuit.slimefun4.core.guide;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.CSCoreLibPlugin.cscorelib2.collections.Pair;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

import java.util.List;

/**
 * This object holds a {@link SlimefunItem} for use in the {@link GuideHistory}
 * when opening the recipe uses for that {@link SlimefunItem} as well as the page of those uses
 */
public class SlimefunItemRecipeUse {

    private final SlimefunItem item;
    private final int page;
    private final List<Pair<SlimefunItem, Integer>> uses;

    public SlimefunItemRecipeUse(SlimefunItem item, int page) {
        this.item = item;
        this.page = page;
        this.uses = SlimefunPlugin.getRegistry().getSlimefunItemUses().get(item);
    }

    public SlimefunItem getItem() {
        return this.item;
    }

    public int getPage() {
        return this.page;
    }

    public List<Pair<SlimefunItem, Integer>> getUses() {
        return this.uses;
    }
}
