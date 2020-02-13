package me.mrCookieSlime.Slimefun.api.item_transport;

import org.bukkit.inventory.ItemStack;

public class ItemAndInt {
	
	private final ItemStack item;
	private int number;
	
	public ItemAndInt(ItemStack item, int amount) {
		this.number = amount;
		this.item = item;
	}
	
	public int getInt() {
		return number;
	}
	
	public ItemStack getItem() {
		return item;
	}

	public void add(int amount) {
		number += amount;
	}

}
