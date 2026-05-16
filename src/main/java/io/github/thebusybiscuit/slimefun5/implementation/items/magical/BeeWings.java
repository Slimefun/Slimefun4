package io.github.thebusybiscuit.slimefun5.implementation.items.magical;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.BeeWingsListener;
import io.github.thebusybiscuit.slimefun5.implementation.tasks.player.BeeWingsTask;

/**
 * The {@link BeeWings} are a special form of the elytra which gives you a slow falling effect
 * when you approach the ground.
 *
 * @author beSnow
 * @author TheBusyBiscuit
 * 
 * @see BeeWingsListener
 * @see BeeWingsTask
 * 
 */
public class BeeWings extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public BeeWings(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

}

