package me.mrCookieSlime.Slimefun.ancient_altar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public final class Pedestals {
	
	private Pedestals() {}

	public static List<Block> getPedestals(Block altar) {
		String pedestal = "ANCIENT_PEDESTAL";
		List<Block> list = new ArrayList<>();

		if (BlockStorage.check(altar.getRelative(2, 0, -2), pedestal)) {
			list.add(altar.getRelative(2, 0, -2));
		}
		if (BlockStorage.check(altar.getRelative(3, 0, 0), pedestal)) {
			list.add(altar.getRelative(3, 0, 0));
		}
		if (BlockStorage.check(altar.getRelative(2, 0, 2), pedestal)) {
			list.add(altar.getRelative(2, 0, 2));
		}
		if (BlockStorage.check(altar.getRelative(0, 0, 3), pedestal)) {
			list.add(altar.getRelative(0, 0, 3));
		}
		if (BlockStorage.check(altar.getRelative(-2, 0, 2), pedestal)) {
			list.add(altar.getRelative(-2, 0, 2));
		}
		if (BlockStorage.check(altar.getRelative(-3, 0, 0), pedestal)) {
			list.add(altar.getRelative(-3, 0, 0));
		}
		if (BlockStorage.check(altar.getRelative(-2, 0, -2), pedestal)) {
			list.add(altar.getRelative(-2, 0, -2));
		}
		if (BlockStorage.check(altar.getRelative(0, 0, -3), pedestal)) {
			list.add(altar.getRelative(0, 0, -3));
		}

		return list;
	}

	public static ItemStack getRecipeOutput(ItemStack catalyst, List<ItemStack> input) {
		if (input.size() != 8) return null;
		
		if (SlimefunManager.isItemSimilar(catalyst, SlimefunItems.BROKEN_SPAWNER, false)) {
            if (checkRecipe(SlimefunItems.BROKEN_SPAWNER, input) == null) return null;
            
			ItemStack spawner = SlimefunItems.REPAIRED_SPAWNER.clone();
			ItemMeta im = spawner.getItemMeta();
			im.setLore(Arrays.asList(catalyst.getItemMeta().getLore().get(0)));
			spawner.setItemMeta(im);
			return spawner;
		}

		return checkRecipe(catalyst, input);
	}

	private static ItemStack checkRecipe(ItemStack catalyst, List<ItemStack> items) {
        for (AltarRecipe recipe : SlimefunPlugin.getUtilities().altarRecipes) {
        	if (SlimefunManager.isItemSimilar(catalyst, recipe.getCatalyst(), true)) {
        		for (int i = 0; i < 8; i++) {
            		if (SlimefunManager.isItemSimilar(items.get(i), recipe.getInput().get(0), true)) {
            			for (int j = 1; j < 8; j++) {
        					if (!SlimefunManager.isItemSimilar(items.get((i + j) % items.size()), recipe.getInput().get(j), true)) {
        						break;
        					}
        					else if (j == 7) {
        						return recipe.getOutput();
        					}
        				}
        			}
            	}
    		}
        }
        
        return null;
	}
}
