package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * @deprecated This class was moved to
 *             {@link io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SlimefunBackpack}.
 *
 */
@Deprecated
public class SlimefunBackpack extends io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SlimefunBackpack {

    public SlimefunBackpack(int size, Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(size, category, item, recipeType, recipe);
    }

}
