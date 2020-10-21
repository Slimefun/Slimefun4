package io.github.thebusybiscuit.slimefun4.implementation.items.backpacks;

import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This implementation of {@link SlimefunBackpack} is used for holding {@link Radioactive} items.
 *
 * @author TheSilentPro
 *
 */
public class HazmatBackpack extends SlimefunBackpack {

    @ParametersAreNonnullByDefault
    public HazmatBackpack(int size, Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(size, category, item, recipeType, recipe);
    }

}
