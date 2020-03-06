package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated Moved to {@code io.github.thebusybiscuit.slimefun4.implementation.items.tools.SlimefunBackpack}
 */
@Deprecated
public class SlimefunBackpack extends io.github.thebusybiscuit.slimefun4.implementation.items.tools.SlimefunBackpack {

    public SlimefunBackpack(int size, Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(size, category, item, recipeType, recipe);
    }

}