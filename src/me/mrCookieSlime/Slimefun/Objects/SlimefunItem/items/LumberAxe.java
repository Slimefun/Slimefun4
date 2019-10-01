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
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class LumberAxe extends SimpleSlimefunItem<BlockBreakHandler> implements NotPlaceable {

	public LumberAxe(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}

	@Override
	public BlockBreakHandler getItemHandler() {
		return (e, item, fortune, drops) -> {
			if (SlimefunManager.isItemSimiliar(e.getPlayer().getInventory().getItemInMainHand(), getItem(), true)) {
				if (MaterialCollections.contains(e.getBlock().getType(), MaterialCollections.getAllLogs())
					|| MaterialCollections.contains(e.getBlock().getType(), MaterialCollections.getAllWood())
				) {
					List<Block> logs = Vein.find(e.getBlock(), 150,
						b -> (
							MaterialCollections.contains(e.getBlock().getType(), MaterialCollections.getAllLogs())
							|| MaterialCollections.contains(e.getBlock().getType(), MaterialCollections.getAllWood())
						)
						&& SlimefunPlugin.getProtectionManager()
							.hasPermission(e.getPlayer(), b, ProtectableAction.BREAK_BLOCK)
						);

					logs.remove(e.getBlock());
					for (Block b : logs) {
						if (SlimefunPlugin.getProtectionManager()
							.hasPermission(e.getPlayer(), b, ProtectableAction.BREAK_BLOCK)
						) {
							b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

							for (ItemStack drop : b.getDrops()) {
								b.getWorld().dropItemNaturally(b.getLocation(), drop);
							}

							b.setType(Material.AIR);
						}
					}
				}
				return true;
			}
			else
				return false;
		};
	}
}
