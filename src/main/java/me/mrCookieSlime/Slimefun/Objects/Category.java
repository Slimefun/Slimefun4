package me.mrCookieSlime.Slimefun.Objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.SlimefunRegistry;
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
	
	private final ItemStack item;
	private final List<SlimefunItem> items;
	private final int tier;

	/**
	 * Constructs a Category with the given display item.
     * The tier is set to a default value of {@code 3}.
	 * 
	 * @param item the display item for this category
	 * 
	 * @since 4.0
	 */
	public Category(ItemStack item) {
		this(item, 3);
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
		
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		this.item.setItemMeta(meta);
		
		this.items = new ArrayList<>();
		this.tier = tier;
	}
	
	/**
	 * Gets the list of the registered categories.
	 * 
	 * @deprecated Use {@link SlimefunRegistry#getEnabledCategories()}
	 * 
	 * @return the list of the registered categories
	 * 
	 * @since 4.0
	 * @see Categories
	 */
	@Deprecated
	public static List<Category> list() {
		return SlimefunPlugin.getRegistry().getEnabledCategories();
	}

	/**
	 * Registers this category.
	 * <p>
	 * By default, a category is automatically registered when a {@link SlimefunItem} is bound to it.
	 * 
	 * @since 4.0
	 */
	public void register() {
		if (this instanceof SeasonalCategory) {
			if (((SeasonalCategory) this).isUnlocked()) {
				SlimefunPlugin.getRegistry().getEnabledCategories().add(this);
				Collections.sort(SlimefunPlugin.getRegistry().getEnabledCategories(), Comparator.comparingInt(Category::getTier));
			}
		}
		else {
			SlimefunPlugin.getRegistry().getEnabledCategories().add(this);
			Collections.sort(SlimefunPlugin.getRegistry().getEnabledCategories(), Comparator.comparingInt(Category::getTier));
		}
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
	
	public ItemStack getItem() {
		return item.clone();
	}
	
	public ItemStack getItem(Player p) {
        return new CustomItem(item, meta -> meta.setLore(Arrays.asList("", ChatColor.GRAY + "\u21E8 " + ChatColor.GREEN + SlimefunPlugin.getLocal().getMessage(p, "guide.tooltips.open-category"))));
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
	 * Returns the tier of this category.
	 * 
	 * @return the tier of this category
	 * 
	 * @since 4.0
	 */
	public int getTier() {
		return tier;
	}
	
	@Override
	public String toString() {
		return "Slimefun Category {" + item.getItemMeta().getDisplayName() + ",tier=" + tier + "}";
	}

}
