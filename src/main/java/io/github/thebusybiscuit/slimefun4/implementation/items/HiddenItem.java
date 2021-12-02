package io.github.thebusybiscuit.slimefun4.implementation.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotConfigurable;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

/**
 * The {@link HiddenItem} is a {@link NotConfigurable} {@link SlimefunItem}
 * that is hidden from the Slimefun guide.
 *
 */
public class HiddenItem extends SlimefunItem implements NotConfigurable {

    @ParametersAreNonnullByDefault
    public HiddenItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        this.setHidden(true);
    }

}
