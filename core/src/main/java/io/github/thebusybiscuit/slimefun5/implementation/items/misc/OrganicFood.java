package io.github.thebusybiscuit.slimefun5.implementation.items.misc;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.implementation.items.electric.machines.FoodFabricator;
import io.github.thebusybiscuit.slimefun5.implementation.items.electric.machines.accelerators.AnimalGrowthAccelerator;

/**
 * {@link OrganicFood} is created using a {@link FoodFabricator} and can
 * be used to fuel an {@link AnimalGrowthAccelerator}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see AnimalGrowthAccelerator
 *
 */
public class OrganicFood extends SlimefunItem {

    public static final int OUTPUT = 2;

    @ParametersAreNonnullByDefault
    public OrganicFood(ItemGroup itemGroup, SlimefunItemStack item, Material ingredient) {
        // Java-8 universal port: the ingredient may not exist on a legacy server (null); substitute a
        // placeholder so the item still registers instead of crashing in new ItemStack(null).
        super(itemGroup, item, RecipeType.FOOD_FABRICATOR, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(ingredient != null ? ingredient : Material.PAPER), null, null, null, null, null, null, null }, new SlimefunItemStack(item, OUTPUT).item());
    }
}

