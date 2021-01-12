package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.EnderPearl;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * {@link EnderBoots} are a pair of boots which negate damage caused
 * by throwing an {@link EnderPearl}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class EnderBoots extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public EnderBoots(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

}
