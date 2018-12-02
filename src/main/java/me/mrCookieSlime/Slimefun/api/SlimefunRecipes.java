package me.mrCookieSlime.Slimefun.api;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

import org.bukkit.inventory.ItemStack;

public class SlimefunRecipes {

	public static void registerMachineRecipe(String machine, int seconds, ItemStack[] input, ItemStack[] output) {
		for (SlimefunItem item: SlimefunItem.all) {
			if (item instanceof AContainer) {
				if (((AContainer) item).getMachineIdentifier().equals(machine)) {
					((AContainer) item).registerRecipe(seconds, input, output);
				}
			}
		}
	}

}
