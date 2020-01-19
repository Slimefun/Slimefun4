package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

/**
 * Represents a vanilla item that is overridden by Slimefun (like {@code ELYTRA}).
 * <p>
 * A VanillaItem uses a non-modified ItemStack (no display name neither lore).
 * When a VanillaItem gets disabled, its {@link SlimefunItem.State} goes on {@code State.VANILLA} which automatically
 * replace it in the recipes by its vanilla equivalent.
 *
 * @since 4.1.6
 */
public class VanillaItem extends SlimefunItem {

	/**
	 * Constructs a VanillaItem.
	 *
	 * @param  category    the category to bind this VanillaItem to
	 * @param  item        the item corresponding to this VanillaItem
	 * @param  id          the id of this VanillaItem
	 * @param  recipeType  the type of the recipe to obtain this VanillaItem
	 * @param  recipe      the recipe to obtain this VanillaItem
	 */
	public VanillaItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
		
		useableInWorkbench = true;
	}
}
