package io.github.thebusybiscuit.slimefun4.core.categories;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
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

    private final MultiCategory multiCategory;

    @ParametersAreNonnullByDefault
    public SubCategory(NamespacedKey key, MultiCategory parent, ItemStack item) {
        this(key, parent, item, 3);
    }

    @ParametersAreNonnullByDefault
    public SubCategory(NamespacedKey key, MultiCategory parent, ItemStack item, int tier) {
        super(key, item, tier);

        Validate.notNull(parent, "The parent category cannot be null");

        multiCategory = parent;
        parent.addSubCategory(this);
    }

    @Override
    public final boolean isHidden(@Nonnull Player p) {
        /*
         * Sub Categories are always hidden,
         * they won't show up in the normal guide view.
         */
        return true;
    }

    @Nonnull
    public final MultiCategory getParent() {
        return multiCategory;
    }

    @Override
    public final void register(@Nonnull SlimefunAddon addon) {
        super.register(addon);

        if (!multiCategory.isRegistered()) {
            multiCategory.register(addon);
        }
    }

}
