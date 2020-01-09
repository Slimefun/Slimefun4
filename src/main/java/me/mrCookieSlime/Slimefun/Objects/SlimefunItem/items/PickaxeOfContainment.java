package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.core.utils.ChatUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class PickaxeOfContainment extends SimpleSlimefunItem<BlockBreakHandler> {

	public PickaxeOfContainment(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}

	@Override
	protected boolean areItemHandlersPrivate() {
		return false;
	}

	@Override
	public BlockBreakHandler getItemHandler() {
		return (e, item, fortune, drops) -> {
			if (isItem(item)) {
				// Refactored it into this so we don't need to call e.getBlock() all the time.
				Block b = e.getBlock(); 
				if (b.getType() != Material.SPAWNER) return true; 
				
				// If the spawner's BlockStorage has BlockInfo, then it's not a vanilla spawner and shouldn't give a broken spawner.
				ItemStack spawner = SlimefunItems.BROKEN_SPAWNER.clone();
				if (BlockStorage.hasBlockInfo(b)) {
					spawner = SlimefunItems.REPAIRED_SPAWNER.clone();
				}
				
				ItemMeta im = spawner.getItemMeta();
				List<String> lore = im.getLore();
				
				for (int i = 0; i < lore.size(); i++) {
					if (lore.get(i).contains("<Type>")) {
						lore.set(i, lore.get(i).replace("<Type>", ChatUtils.humanize(((CreatureSpawner) b.getState()).getSpawnedType().toString())));
					}
				}
				
				im.setLore(lore);
				spawner.setItemMeta(im);
				b.getLocation().getWorld().dropItemNaturally(b.getLocation(), spawner);
				e.setExpToDrop(0);
				e.setDropItems(false);
				return true;
			}
			else {
				if (e.getBlock().getType() == Material.SPAWNER) e.setDropItems(false);
				return false;
			}
		};
	}

}
