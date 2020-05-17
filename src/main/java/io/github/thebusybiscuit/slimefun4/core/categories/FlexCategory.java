package io.github.thebusybiscuit.slimefun4.core.categories;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * A {@link FlexCategory} is a {@link Category} inside the {@link SlimefunGuide} that can
 * be completely modified.
 * It cannot hold any {@link SlimefunItem}.
 * It can be completely overridden to perform any action upon being opened.
 *
 * @author TheBusyBiscuit
 *
 */
public abstract class FlexCategory extends Category {

    public FlexCategory(NamespacedKey key, ItemStack item) {
        this(key, item, 3);
    }

    public abstract boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideLayout layout);

    public abstract void open(Player p, PlayerProfile profile, SlimefunGuideLayout layout);

    public FlexCategory(NamespacedKey key, ItemStack item, int tier) {
        super(key, item, tier);
    }

    @Override
    public final boolean isHidden(Player p) {
        // We can stop this method right here.
        // We provide a custom method for this. See isVisible(...)
        return false;
    }

    @Override
    public final void add(SlimefunItem item) {
        throw new UnsupportedOperationException("You cannot add items to a FlexCategory!");
    }

    @Override
    public final List<SlimefunItem> getItems() {
        throw new UnsupportedOperationException("A FlexCategory has no items!");
    }

    @Override
    public final boolean contains(SlimefunItem item) {
        throw new UnsupportedOperationException("A FlexCategory has no items!");
    }

    @Override
    public final void remove(SlimefunItem item) {
        throw new UnsupportedOperationException("A FlexCategory has no items, so there is nothing remove!");
    }

}