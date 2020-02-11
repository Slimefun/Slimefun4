package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * Represents an alloy, a {@link SlimefunItem} obtainable using the {@code SMELTERY}.
 * <p>
 * An alloy is generally made up of several minerals.
 * As an example, {@code BRASS_INGOT} is made up of {@code COPPER_DUST}, {@code ZINC_DUST} and {@code COPPER_INGOT}.
 *
 * @since 4.0
 */
public class Alloy extends SlimefunItem {

	/**
	 * Constructs an Alloy bound to {@code Categories.RESOURCES}.
	 *
	 * @param  item    the item corresponding to this Alloy
	 * @param  recipe  the recipe to obtain this Alloy in the Smeltery
	 */
	public Alloy(SlimefunItemStack item, ItemStack[] recipe) {
		super(Categories.RESOURCES, item, RecipeType.SMELTERY, recipe);
	}

	/**
	 * Constructs an Alloy bound to the specified Category.
	 *
	 * @param  category the category for this Item
	 * @param  item    the item corresponding to this Alloy
	 * @param  recipe  the recipe to obtain this Alloy in the Smeltery
	 */
	public Alloy(Category category, SlimefunItemStack item, ItemStack[] recipe) {
		super(category, item, RecipeType.SMELTERY, recipe);
	}

}
