package me.mrCookieSlime.Slimefun.Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.MenuItem;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.URID.URID;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import org.bukkit.inventory.ItemStack;

/**
 * Statically handles categories. Represents a category, which structure
 * multiple {@link SlimefunItem} in the Slimefun Guide.
 * <p>
 * See {@link #Category(ItemStack)} to create a category.
 * <p>
 * See {@link Categories} for the built-in categories.
 * 
 * @author TheBusyBiscuit
 * @since 4.0
 * 
 * @see LockedCategory
 * @see SeasonCategory
 */
public class Category {
	/**
	 * Contains all the registered Categories.
	 * @since 4.0
	 * @see Categories
	 */
	public static List<Category> list = new ArrayList<Category>();

	private ItemStack item;
	private List<SlimefunItem> items;
	private URID urid;
	private int tier;

	/**
	 * The basic constructor for a Category.
	 * <p>
	 * Create a new category by calling {@link #Category(ItemStack)}.
	 * <br>
	 * The display item can be created using {@link ItemStack}, however using CSCoreLib's
	 * {@link CustomItem} or even {@link MenuItem} is highly recommended.
	 * <br>
	 * Instead of {@link #Category(ItemStack, int)}, where the tier is customizable,
	 * it will here automatically be set to 3.
	 * <p>
	 * Major part of category declaration is handled by Slimefun itself.
	 * <br>
	 * See {@link #add(SlimefunItem)} and {@link #register()} for more information.
	 * 
	 * @param item The display item for this category
	 * 
	 * @since 4.0
	 * @see #Category(ItemStack, int)
	 */
	public Category(ItemStack item) {
		this.item = item;
		this.items = new ArrayList<SlimefunItem>();
		this.urid = URID.nextURID(this, false);
		this.tier = 3;
	}

	/**
	 * The constructor for a Category.
	 * <p>
	 * Create a new category by calling {@link #Category(ItemStack, int)}.
	 * <br>
	 * The display item can be created using {@link ItemStack}, however using CSCoreLib's
	 * {@link CustomItem} or even {@link MenuItem} is highly recommended.
	 * <br>
	 * The tier defines where the category will be displayed in the Slimefun Guide. 
	 * A lower tier will display the category first.
	 * <p>
	 * Major part of category declaration is handled by Slimefun itself.
	 * <br>
	 * See {@link #add(SlimefunItem)} and {@link #register()} for more information.
	 * 
	 * @param item The display item for this category
	 * @param tier The tier of this category
	 * 
	 * @since 4.0
	 * @see #Category(ItemStack)
	 */
	public Category(ItemStack item, int tier) {
		this.item = item;
		this.items = new ArrayList<SlimefunItem>();
		this.urid = URID.nextURID(this, false);
		this.tier = tier;
	}

	/**
	 * Registers the category.
	 * <p>
	 * By default, the category gets automatically registered when a {@link SlimefunItem}
	 * is bound to it.
	 * <br>
	 * See {@link SlimefunItem#register(boolean)} for more details.
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
	 * Gets the list of all registered categories.
	 * 
	 * @return the list of registered categories
	 * 
	 * @since 4.0
	 * @see Categories
	 */
	public static List<Category> list() {
		return list;
	}

	/**
	 * Bounds a {@link SlimefunItem} to the category.
	 * <p>
	 * By default, a SlimefunItem gets automatically bound to a Category when it gets
	 * registered.
	 * <br>
	 * See {@link SlimefunItem#register(boolean)} for more information.
	 * 
	 * @param item SlimefunItem to bound to this category
	 * 
	 * @since 4.0
	 * @see #getItems()
	 */
	public void add(SlimefunItem item) {
		items.add(item);
	}

	/**
	 * Gets the display item of the category.
	 * 
	 * @return the category's display item
	 * 
	 * @since 4.0
	 */
	public ItemStack getItem() {
		return item;
	}

	/**
	 * Gets the list of all items bound to the category.
	 * 
	 * @return the list of all items bound to this category
	 * 
	 * @since 4.0
	 */
	public List<SlimefunItem> getItems() {
		return this.items;
	}

	/**
	 * Attempts to get the category with the given display item.
	 * 
	 * @param item Display item of the category to get
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
	 * Gets the URID of the category.
	 * 
	 * @return the category's URID
	 * 
	 * @since 4.0
	 * @see URID
	 */
	public URID getURID() {
		return urid;
	}

	/**
	 * Gets the tier of the category.
	 * 
	 * @return the category's tier
	 * 
	 * @since 4.0
	 */
	public int getTier() {
		return tier;
	}

	/**
	 * /!\ Documentation needed here.
	 * 
	 * @author TheBusyBiscuit
	 * @since 4.0
	 */
	class CategorySorter implements Comparator<Category> {

		/**
		 * /!\ Documentation needed here.
		 * 
		 * @since 4.0
		 */
		@Override
		public int compare(Category c1, Category c2) {
			return c1.getTier() > c2.getTier() ? 1: c1.getTier() == c2.getTier() ? 0: -1;
		}

	}

}
