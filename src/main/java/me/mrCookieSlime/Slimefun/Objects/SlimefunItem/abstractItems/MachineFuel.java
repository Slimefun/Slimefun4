package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems;

import org.bukkit.inventory.ItemStack;

public class MachineFuel {
	
	private final int ticks;
	private final ItemStack fuel;
	private final ItemStack output;
	
	public MachineFuel(int seconds, ItemStack fuel) {
		this.ticks = seconds * 2;
		this.fuel = fuel;
		this.output = null;
	}
	
	public MachineFuel(int seconds, ItemStack fuel, ItemStack output) {
		this.ticks = seconds * 2;
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
		return ticks;
	}

}
