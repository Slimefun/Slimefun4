package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class HerculesPickaxe extends SimpleSlimefunItem<BlockBreakHandler> {

	public HerculesPickaxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}

	@Override
	protected boolean areItemHandlersPrivate() {
		return false;
	}

	@Override
	public BlockBreakHandler getItemHandler() {
		return (e, item, fortune, drops) -> {
			if (isItem(item) && e.getBlock().getType().toString().endsWith("_ORE")) {
				if (e.getBlock().getType() == Material.IRON_ORE) {
					drops.add(new CustomItem(SlimefunItems.IRON_DUST, 2));
				}
				else if (e.getBlock().getType() == Material.GOLD_ORE) {
					drops.add(new CustomItem(SlimefunItems.GOLD_DUST, 2));
				}
				else {
					for (ItemStack drop : e.getBlock().getDrops(getItem())) {
						drops.add(new CustomItem(drop, drop.getAmount() * 2));
					}
				}
				return true;
			}
			else return false;
		};
	}

}
