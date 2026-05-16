package io.github.thebusybiscuit.slimefun5.implementation.items.armor;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.items.electric.gadgets.Jetpack;
import io.github.thebusybiscuit.slimefun5.implementation.tasks.player.ParachuteTask;

/**
 * The {@link Parachute} is a {@link SlimefunItem} that can be equipped as a chestplate.
 * It allows you slowly glide to the ground while holding shift.
 * 
 * This class does not contain much code to see, check our the {@link ParachuteTask} class
 * for the actual logic behind this.
 * 
 * @author TheBusyBiscuit
 * 
 * @see ParachuteTask
 * @see Jetpack
 *
 */
public class Parachute extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public Parachute(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

}

