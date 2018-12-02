package me.mrCookieSlime.Slimefun.AncientAltar;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class AltarRecipe {
	
	ItemStack catalyst;
	List<ItemStack> input;
	ItemStack output;
	
	public AltarRecipe(List<ItemStack> input, ItemStack output) {
		this.catalyst = input.get(4);
		this.input = new ArrayList<ItemStack>();
		for (int i = 0; i < input.size(); i++) {
			if (i != 4) this.input.add(input.get(i));
		}
		this.output = output;
		
		Pedestals.recipes.add(this);
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
