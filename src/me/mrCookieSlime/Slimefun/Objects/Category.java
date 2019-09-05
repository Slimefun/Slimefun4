package me.mrCookieSlime.Slimefun.Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * Statically handles categories.
 * Represents a category, which structure multiple {@link SlimefunItem} in the guide.
 * <p>
 * See {@link Categories} for the built-in categories.
 *
 * @since 4.0
 *
 * @see LockedCategory
 * @see SeasonalCategory
 */
public class Category {
	
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
		
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		this.item.setItemMeta(meta);
		
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
		SlimefunPlugin.getUtilities().allCategories.add(this);
		Collections.sort(list(), SlimefunPlugin.getUtilities().categorySorter);

		if (this instanceof SeasonalCategory) {
			if (((SeasonalCategory) this).isUnlocked()) {
				SlimefunPlugin.getUtilities().enabledCategories.add(this);
			}
		}
		else {
			SlimefunPlugin.getUtilities().enabledCategories.add(this);
		}
		
		Collections.sort(SlimefunPlugin.getUtilities().enabledCategories, SlimefunPlugin.getUtilities().categorySorter);
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
		return SlimefunPlugin.getUtilities().allCategories;
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
	@Deprecated
	public static Category getByItem(ItemStack item) {
		for (Category c: list()) {
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

}
