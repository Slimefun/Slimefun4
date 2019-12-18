package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.Collection;
import java.util.Optional;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.recipes.MinecraftRecipe;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SmeltersPickaxe extends SimpleSlimefunItem<BlockBreakHandler> {

	public SmeltersPickaxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
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
				if (BlockStorage.hasBlockInfo(e.getBlock())) return true;
				if (e.getBlock().getType() == Material.PLAYER_HEAD) return true;
				
				Collection<ItemStack> blockDrops = e.getBlock().getDrops(getItem());
				for (ItemStack drop : blockDrops) {
					if (drop != null) {
						ItemStack output = drop;
						
						if (MaterialCollections.getAllOres().contains(e.getBlock().getType())) {
							output.setAmount(fortune);
							
							Optional<ItemStack> furnaceOutput = SlimefunPlugin.getMinecraftRecipes().getRecipeOutput(MinecraftRecipe.FURNACE, drop);
							if (furnaceOutput.isPresent()) {
								e.getBlock().getWorld().playEffect(e.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
								output.setType(furnaceOutput.get().getType());
							}
						}
						
						drops.add(output);
					}
				}

				return true;
			}
			else return false;
		};
	}

}
