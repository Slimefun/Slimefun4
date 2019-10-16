package me.mrCookieSlime.Slimefun.ancient_altar;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class AltarRecipe {
	
	private ItemStack catalyst;
	private List<ItemStack> input;
	private ItemStack output;
	
	public AltarRecipe(List<ItemStack> input, ItemStack output) {
		this.catalyst = input.get(4);
		this.input = new ArrayList<>();
		
		this.input.add(input.get(0));
		this.input.add(input.get(1));
		this.input.add(input.get(2));
		this.input.add(input.get(5));
		
		this.input.add(input.get(8));
		this.input.add(input.get(7));
		this.input.add(input.get(6));
		this.input.add(input.get(3));
		
		this.output = output;
		
		SlimefunPlugin.getUtilities().altarRecipes.add(this);
	}
	
	public ItemStack getCatalyst() {
		return this.catalyst;
	}
	
	public ItemStack getOutput() {
		return this.output;
	}
	
	public List<ItemStack> getInput() {
		return this.input;
	}

}
