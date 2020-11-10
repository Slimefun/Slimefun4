package io.github.thebusybiscuit.slimefun4.core.guide;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

import java.util.List;

/**
 * This object holds a {@link SlimefunItem} for use in the {@link GuideHistory}
 * when opening the recipe uses for that {@link SlimefunItem} as well as the page of those uses
 */
public class SlimefunItemRecipeUses {

    private final SlimefunItem item;
    private final int page;
    private final List<SlimefunItem> uses;

    public SlimefunItemRecipeUses(SlimefunItem item, int page) {
        this.item = item;
        this.page = page;

        if (SlimefunPlugin.getRegistry().getSlimefunItemUses() == null) {
            SlimefunPlugin.getRegistry().loadRecipeUses();
        }

        this.uses = SlimefunPlugin.getRegistry().getSlimefunItemUses().get(item);
    }

    public SlimefunItem getItem() {
        return this.item;
    }

    public int getPage() {
        return this.page;
    }

    public List<SlimefunItem> getUses() {
        return this.uses;
    }
}
