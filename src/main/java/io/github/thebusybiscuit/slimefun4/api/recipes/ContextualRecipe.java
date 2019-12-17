package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.function.Predicate;

import org.bukkit.inventory.ItemStack;

public interface ContextualRecipe extends Predicate<ItemStack[]> {
	
	boolean isShapeless();
	
	ItemStack getOutput(ItemStack[] input);

}
