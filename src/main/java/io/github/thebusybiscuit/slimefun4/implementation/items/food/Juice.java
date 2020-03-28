package io.github.thebusybiscuit.slimefun4.implementation.items.food;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.listeners.CoolerListener;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This class represents a {@link SlimefunItem} that can be stored inside
 * of a {@link Cooler}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Cooler
 * @see CoolerListener
 *
 */
public class Juice extends SlimefunItem {

    public Juice(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

}
