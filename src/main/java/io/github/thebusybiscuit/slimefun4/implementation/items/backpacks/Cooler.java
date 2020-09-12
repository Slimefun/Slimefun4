package io.github.thebusybiscuit.slimefun4.implementation.items.backpacks;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.items.food.Juice;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.CoolerListener;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link Cooler} is a special variant of the {@link SlimefunBackpack}.
 * It can only hold {@link Juice Juices} and auto-consumes those when a {@link Player}
 * loses hunger while carrying a {@link Cooler} filled with {@link Juice}.
 * 
 * @author TheBusyBiscuit
 *
 * @see Juice
 * @see CoolerListener
 *
 */
public class Cooler extends SlimefunBackpack {

    public Cooler(int size, Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(size, category, item, recipeType, recipe);
    }

    @Override
    public boolean isItemAllowed(ItemStack item, SlimefunItem itemAsSlimefunItem) {
        // A Cooler only allows Juices
        return itemAsSlimefunItem instanceof Juice;
    }

}
