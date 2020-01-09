package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.blocks.Vein;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class PickaxeOfVeinMining extends SimpleSlimefunItem<BlockBreakHandler> {

	public PickaxeOfVeinMining(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
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
				if (MaterialCollections.getAllOres().contains(e.getBlock().getType())) {
					List<Block> blocks = Vein.find(e.getBlock(), 16, MaterialCollections.getAllOres());
					
					for (Block b : blocks) {
						if (SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), b.getLocation(), ProtectableAction.BREAK_BLOCK)) {
							b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
							
							for (ItemStack drop : b.getDrops(getItem())) {
								b.getWorld().dropItemNaturally(b.getLocation(), drop.getType().isBlock() ? drop: new CustomItem(drop, fortune));
							}
							
							b.setType(Material.AIR);
						}
					}
				}
				
				return true;
			}
			else return false;
		};
	}

}
