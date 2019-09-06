package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.ancient_altar.AncientAltarListener;

public class AncientPedestal extends SlimefunItem {

	public AncientPedestal(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		super(category, item, id, recipeType, recipe, recipeOutput);
		
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
