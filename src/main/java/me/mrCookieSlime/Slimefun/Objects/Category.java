package me.mrCookieSlime.Slimefun.Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.URID.URID;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import org.bukkit.inventory.ItemStack;

public class Category {
	
	public static List<Category> list = new ArrayList<Category>();
	
	ItemStack item;
	List<SlimefunItem> items;
	URID urid;
	int month;
	int tier;
	
	public Category(ItemStack item) {
		this.item = item;
		this.items = new ArrayList<SlimefunItem>();
		this.urid = URID.nextURID(this, false);
		this.tier = 3;
	}
	
	public Category(ItemStack item, int tier) {
		this.item = item;
		this.items = new ArrayList<SlimefunItem>();
		this.urid = URID.nextURID(this, false);
		this.tier = tier;
	}
	
	public void register() {
		list.add(this);
		Collections.sort(list, new CategorySorter());
		
		if (this instanceof SeasonCategory) {
			if (((SeasonCategory) this).isUnlocked()) Slimefun.current_categories.add(this);
		}
		else Slimefun.current_categories.add(this);
		Collections.sort(Slimefun.current_categories, new CategorySorter());
	}

	public static List<Category> list() {
		return list;
	}
	
	public void add(SlimefunItem item) {
		this.items.add(item);
	}
	
	public ItemStack getItem()				{		return this.item;		}
	public List<SlimefunItem> getItems()	{		return this.items;		}
	
	public static Category getByItem(ItemStack item) {
		for (Category c: list) {
			if (c.getItem().isSimilar(item)) return c;
		}
		return null;
	}
	
	public URID getURID() {
		return urid;
	}

	public int getTier() {
		return this.tier;
	}
	
	class CategorySorter implements Comparator<Category> {

		@Override
		public int compare(Category c1, Category c2) {
			return c1.getTier() > c2.getTier() ? 1: c1.getTier() == c2.getTier() ? 0: -1;
		}
		
	}

}
