package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.inventory.ItemStack;

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
	 * @param  id      the id of this Alloy
	 * @param  recipe  the recipe to obtain this Alloy in the Smeltery
	 */
	public Alloy(ItemStack item, String id, ItemStack[] recipe) {
		super(Categories.RESOURCES, item, id, RecipeType.SMELTERY, recipe);
	}

	/**
	 * Constructs an Alloy with a definable {@link Category}.
	 *
	 * @param  category  the category to bind this Alloy to
	 * @param  item      the item corresponding to this Alloy
	 * @param  id        the id of this Alloy
	 * @param  recipe    the recipe to obtain this Alloy in the Smeltery
	 */
	public Alloy(Category category, ItemStack item, String id, ItemStack[] recipe) {
		super(category, item, id, RecipeType.SMELTERY, recipe);
	}

}
