package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems;

import org.bukkit.inventory.ItemStack;

public class MachineFuel {
	
	private int seconds;
	private ItemStack fuel;
	private ItemStack output;
	
	public MachineFuel(int seconds, ItemStack fuel) {
		this.seconds = seconds * 2;
		this.fuel = fuel;
		this.output = null;
	}
	
	public MachineFuel(int seconds, ItemStack fuel, ItemStack output) {
		this.seconds = seconds * 2;
		this.fuel = fuel;
		this.output = output;
	}
	
	public ItemStack getInput() {
		return this.fuel;
	}
	
	public ItemStack getOutput() {
		return this.output;
	}
	
	public int getTicks() {
		return seconds;
	}

}
