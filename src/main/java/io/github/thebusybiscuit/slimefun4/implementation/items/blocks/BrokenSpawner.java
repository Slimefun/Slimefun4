package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This implementation of {@link SlimefunItem} represents a Broken Spawner.
 * A {@link BrokenSpawner} can be repaired into a {@link RepairedSpawner}.
 * Without repairing, the block will be unplaceable.
 * 
 * @author TheBusyBiscuit
 * 
 * @see RepairedSpawner
 *
 */
public class BrokenSpawner extends UnplaceableBlock {

    public BrokenSpawner(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

}
