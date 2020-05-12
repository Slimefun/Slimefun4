package me.mrCookieSlime.Slimefun.Objects;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated Moved to io.github.thebusybiscuit.slimefun4.core.categories.LockedCategory
 * 
 */
@Deprecated
public class LockedCategory extends io.github.thebusybiscuit.slimefun4.core.categories.LockedCategory {

    public LockedCategory(NamespacedKey key, ItemStack item, NamespacedKey... parents) {
        this(key, item, 3, parents);
    }

    public LockedCategory(NamespacedKey key, ItemStack item, int tier, NamespacedKey... parents) {
        super(key, item, tier, parents);
    }
}
