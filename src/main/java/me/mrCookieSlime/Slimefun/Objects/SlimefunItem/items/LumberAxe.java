package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.blocks.Vein;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class LumberAxe extends SimpleSlimefunItem<BlockBreakHandler> implements NotPlaceable {

	public LumberAxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
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
				if (MaterialCollections.getAllLogs().contains(e.getBlock().getType())) {
					List<Block> logs = Vein.find(e.getBlock(), 100, b -> MaterialCollections.getAllLogs().contains(b.getType()));
					
					if (logs.contains(e.getBlock())) {
						logs.remove(e.getBlock());
					}
					
					for (Block b: logs) {
						if (SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), b, ProtectableAction.BREAK_BLOCK)) {
							b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
							
							for (ItemStack drop : b.getDrops(getItem())) {
								b.getWorld().dropItemNaturally(b.getLocation(), drop);
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
