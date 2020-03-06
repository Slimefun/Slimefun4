package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated Moved to {@code io.github.thebusybiscuit.slimefun4.implementation.items.food.Juice}
 */
@Deprecated
public class Juice extends io.github.thebusybiscuit.slimefun4.implementation.items.food.Juice {

    public Juice(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

}