package io.github.thebusybiscuit.slimefun4.implementation.items;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * The {@link EnchantedItem} is an enchanted {@link SlimefunItem}.
 * By default, this class sets items to be not disenchantable.
 * 
 * @author Fury_Phoenix
 *
 */
public class EnchantedItem extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public EnchantedItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        disenchantable = false;
    }

}
