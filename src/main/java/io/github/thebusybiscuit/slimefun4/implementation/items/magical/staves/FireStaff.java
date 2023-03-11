package io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves;

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
 * The {@link FireStaff} is a magical {@link SlimefunItem}, enchanted with Fire Aspect 5.
 * 
 * @author Fury_Phoenix
 *
 */
public class FireStaff extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public FireStaff(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        disenchantable = false;
    }

}
