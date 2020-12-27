package io.github.thebusybiscuit.slimefun4.core.categories;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.Category;

/**
 * The {@link SubCategory} is a child {@link Category} of the
 * {@link MultiCategory}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see MultiCategory
 *
 */
public class SubCategory extends Category {

    @ParametersAreNonnullByDefault
    public SubCategory(NamespacedKey key, ItemStack item) {
        this(key, item, 3);
    }

    @ParametersAreNonnullByDefault
    public SubCategory(NamespacedKey key, ItemStack item, int tier) {
        super(key, item, tier);
    }

    @Override
    public final boolean isHidden(Player p) {
        /*
         * Sub Categories are always hidden,
         * they won't show up in the normal guide view.
         */
        return true;
    }

}
