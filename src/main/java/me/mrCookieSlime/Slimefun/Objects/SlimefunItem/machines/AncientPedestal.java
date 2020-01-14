package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.listeners.AncientAltarListener;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class AncientPedestal extends SlimefunItem {

	public AncientPedestal(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		super(category, item, recipeType, recipe, recipeOutput);
		
		SlimefunItem.registerBlockHandler(getID(), (p, b, tool, reason) -> {
			Item stack = AncientAltarListener.findItem(b);
			
			if (stack != null) { 
				stack.removeMetadata("item_placed", SlimefunPlugin.instance);
				b.getWorld().dropItem(b.getLocation(), AncientAltarListener.fixItemStack(stack.getItemStack(), stack.getCustomName()));
				stack.remove();
			}
			return true;
		});
	}
}
