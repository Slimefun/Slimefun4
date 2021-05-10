package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.data.type.Farmland;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link FarmerShoes} are just a simple pair of boots which allows a {@link Player} to walk
 * on {@link Farmland} without breaking it.
 * 
 * @author TheBusyBiscuit
 *
 */
public class FarmerShoes extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public FarmerShoes(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

}
