package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * 
 * @deprecated Just override the method {@link #useVanillaBlockBreaking()} instead.
 *
 */
@Deprecated
public class HandledBlock extends SlimefunItem {

    public HandledBlock(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public boolean useVanillaBlockBreaking() {
        return true;
    }

}
