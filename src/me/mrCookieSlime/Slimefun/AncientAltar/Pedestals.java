package me.mrCookieSlime.Slimefun.AncientAltar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class Pedestals {

	public static List<AltarRecipe> recipes = new ArrayList<>();

	public static List<Block> getPedestals(Block altar) {
		List<Block> list = new ArrayList<>();

		if (BlockStorage.check(altar.getRelative(2, 0, -2), "ANCIENT_PEDESTAL")) {
			list.add(altar.getRelative(2, 0, -2));
		}
		if (BlockStorage.check(altar.getRelative(3, 0, 0), "ANCIENT_PEDESTAL")) {
			list.add(altar.getRelative(3, 0, 0));
		}
		if (BlockStorage.check(altar.getRelative(2, 0, 2), "ANCIENT_PEDESTAL")) {
			list.add(altar.getRelative(2, 0, 2));
		}
		if (BlockStorage.check(altar.getRelative(0, 0, 3), "ANCIENT_PEDESTAL")) {
			list.add(altar.getRelative(0, 0, 3));
		}
		if (BlockStorage.check(altar.getRelative(-2, 0, 2), "ANCIENT_PEDESTAL")) {
			list.add(altar.getRelative(-2, 0, 2));
		}
		if (BlockStorage.check(altar.getRelative(-3, 0, 0), "ANCIENT_PEDESTAL")) {
			list.add(altar.getRelative(-3, 0, 0));
		}
		if (BlockStorage.check(altar.getRelative(-2, 0, -2), "ANCIENT_PEDESTAL")) {
			list.add(altar.getRelative(-2, 0, -2));
		}
		if (BlockStorage.check(altar.getRelative(0, 0, -3), "ANCIENT_PEDESTAL")) {
			list.add(altar.getRelative(0, 0, -3));
		}

		return list;
	}

	public static ItemStack getRecipeOutput(ItemStack catalyst, List<ItemStack> input) {
		if (input.size() != 8) return null;
		if (SlimefunManager.isItemSimiliar(catalyst, SlimefunItems.BROKEN_SPAWNER, false)) {
            if (checkRecipe(SlimefunItems.BROKEN_SPAWNER, input) == null) return null;
			final ItemStack spawner = SlimefunItems.REPAIRED_SPAWNER.clone();
			ItemMeta im = spawner.getItemMeta();
			im.setLore(Arrays.asList(catalyst.getItemMeta().getLore().get(0)));
			spawner.setItemMeta(im);
			return spawner;
		}

		return checkRecipe(catalyst, input);
	}

	private static ItemStack checkRecipe(ItemStack catalyst, List<ItemStack> items) {
		loop:
        for (AltarRecipe recipe: recipes) {
        	if (!SlimefunManager.isItemSimiliar(catalyst, recipe.getCatalyst(), true)) {
    			continue;
    		}
        	
        	for (int i = 0; i < 8; i++) {
        		if (!SlimefunManager.isItemSimiliar(items.get(i), recipe.getInput().get(0), true)) {
        			continue;
    			}
        		
        		for (int j = 1; j < 8; j++) {
					if (!SlimefunManager.isItemSimiliar(items.get((i + j) % items.size()), recipe.getInput().get(j), true)) {
						continue loop;
					}
				}
				
				return recipe.getOutput();
        	}
        }
        
        return null;
	}
}
