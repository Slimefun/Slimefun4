package me.mrCookieSlime.Slimefun.Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * Statically handles categories.
 * Represents a category, which structure multiple {@link SlimefunItem} in the guide.
 * <p>
 * See {@link Categories} for the built-in categories.
 *
 * @since 4.0
 *
 * @see LockedCategory
 * @see SeasonCategory
 */
public class Category {
	/**
	 * List of the registered Categories.
	 * @since 4.0
	 * @see Categories
	 */
	public static List<Category> list = new ArrayList<>();

	private ItemStack item;
	private List<SlimefunItem> items;
	private int tier;

	/**
	 * Constructs a Category with the given display item.
     * The tier is set to a default value of {@code 3}.
	 * 
	 * @param item the display item for this category
	 * 
	 * @since 4.0
	 */
	public Category(ItemStack item) {
		this.item = item;
		this.items = new ArrayList<>();
		this.tier = 3;
	}

	/**
     * Constructs a Category with the given display item and the provided tier.
     * </br>
     * A lower tier results in this category being displayed first.
	 * 
	 * @param item the display item for this category
	 * @param tier the tier for this category
	 * 
	 * @since 4.0
	 */
	public Category(ItemStack item, int tier) {
		this.item = item;
		this.items = new ArrayList<>();
		this.tier = tier;
	}

	/**
	 * Registers this category.
	 * <p>
	 * By default, a category is automatically registered when a {@link SlimefunItem} is bound to it.
	 * 
	 * @since 4.0
	 */
	public void register() {
		list.add(this);
		Collections.sort(list, new CategorySorter());

		if (this instanceof SeasonCategory) {
			if (((SeasonCategory) this).isUnlocked()) Slimefun.current_categories.add(this);
		}
		else Slimefun.current_categories.add(this);
		Collections.sort(Slimefun.current_categories, new CategorySorter());
	}

	/**
	 * Gets the list of the registered categories.
	 * 
	 * @return the list of the registered categories
	 * 
	 * @since 4.0
	 * @see Categories
	 */
	public static List<Category> list() {
		return list;
	}

	/**
	 * Bounds the provided {@link SlimefunItem} to this category.
	 * 
	 * @param item the SlimefunItem to bound to this category
	 * 
	 * @since 4.0
	 */
	public void add(SlimefunItem item) {
		items.add(item);
	}

	/**
	 * Returns the display item of this category.
	 * 
	 * @return the display item of this category
     *
	 * @since 4.0
	 */
	public ItemStack getItem() {
		return item;
	}

	/**
	 * Returns the list of SlimefunItems bound to this category.
	 * 
	 * @return the list of SlimefunItems bound to this category
     *
	 * @since 4.0
	 */
	public List<SlimefunItem> getItems() {
		return this.items;
	}

	/**
	 * Attempts to get the category with the given display item.
	 * 
	 * @param item the display item of the category to get
	 * @return Category if found, or null
	 * 
	 * @since 4.0
	 */
	public static Category getByItem(ItemStack item) {
		for (Category c: list) {
			if (c.getItem().isSimilar(item)) return c;
		}
		return null;
	}

	/**
	 * Returns the tier of this category.
	 * 
	 * @return the tier of this category
	 * 
	 * @since 4.0
	 */
	public int getTier() {
		return tier;
	}

	/**
	 * @since 4.0
	 */
	class CategorySorter implements Comparator<Category> {

		/**
		 * @since 4.0
		 */
		@Override
		public int compare(Category c1, Category c2) {
		    return Integer.compare(c1.getTier(), c2.getTier());
		}

	}

}
