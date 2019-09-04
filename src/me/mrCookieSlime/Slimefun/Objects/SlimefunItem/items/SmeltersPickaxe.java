package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Recipe.RecipeCalculator;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class SmeltersPickaxe extends SimpleSlimefunItem<BlockBreakHandler> {

	public SmeltersPickaxe(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}
	
	@Override
	public BlockBreakHandler getItemHandler() {
		return (e, item, fortune, drops) -> {
			if (SlimefunManager.isItemSimiliar(item, getItem(), true)) {
				if (BlockStorage.hasBlockInfo(e.getBlock())) return true;
				if (e.getBlock().getType() == Material.PLAYER_HEAD) return true;

				int j = -1;
				List<ItemStack> dropsList = (List<ItemStack>) e.getBlock().getDrops();
				for (int i = 0; i < dropsList.size(); i++) {
					if (dropsList.get(i) != null) {
						j++;
						drops.add(e.getBlock().getType().toString().endsWith("_ORE") ? new CustomItem(dropsList.get(i), fortune): dropsList.get(i));
						if (RecipeCalculator.getSmeltedOutput(drops.get(i).getType()) != null) {
							e.getBlock().getWorld().playEffect(e.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
							drops.set(j, new CustomItem(RecipeCalculator.getSmeltedOutput(drops.get(i).getType()), drops.get(i).getAmount()));
						}
					}
				}

				return true;
			}
			else return false;
		};
	}

}
