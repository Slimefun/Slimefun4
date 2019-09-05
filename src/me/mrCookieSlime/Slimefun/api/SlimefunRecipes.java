package me.mrCookieSlime.Slimefun.api;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

public final class SlimefunRecipes {

	private SlimefunRecipes() {}
	
	public static void registerMachineRecipe(String machine, int seconds, ItemStack[] input, ItemStack[] output) {
		for (SlimefunItem item: SlimefunPlugin.getUtilities().allItems) {
			if (item instanceof AContainer && ((AContainer) item).getMachineIdentifier().equals(machine)) {
				((AContainer) item).registerRecipe(seconds, input, output);
			}
		}
	}

}
