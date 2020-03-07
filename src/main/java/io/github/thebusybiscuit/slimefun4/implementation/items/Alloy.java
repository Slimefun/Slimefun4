package io.github.thebusybiscuit.slimefun4.implementation.items;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.Smeltery;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * Represents an alloy, a {@link SlimefunItem} obtainable using the {@link Smeltery}.
 * <p>
 * An alloy is generally made up of several minerals.
 * As an example, a {@code BRASS_INGOT} is made up of {@code COPPER_DUST}, {@code ZINC_DUST} and {@code COPPER_INGOT}.
 *
 * @author TheBusyBiscuit
 * 
 */
public class Alloy extends SlimefunItem {

    /**
     * Constructs an {@link Alloy} bound to {@code Categories.RESOURCES}.
     *
     * @param item
     *            the {@link SlimefunItemStack} corresponding to this {@link Alloy}
     * @param recipe
     *            the recipe to obtain this {@link Alloy} in the {@link Smeltery}
     */
    public Alloy(SlimefunItemStack item, ItemStack[] recipe) {
        this(Categories.RESOURCES, item, recipe);
    }

    /**
     * Constructs an {@link Alloy} bound to the specified {@link Category}.
     *
     * @param category
     *            the {@link Category} for this Item
     * @param item
     *            the {@link SlimefunItemStack} corresponding to this {@link Alloy}
     * @param recipe
     *            the recipe to obtain this {@link Alloy} in the {@link Smeltery}
     */
    public Alloy(Category category, SlimefunItemStack item, ItemStack[] recipe) {
        super(category, item, RecipeType.SMELTERY, recipe);
    }

}
