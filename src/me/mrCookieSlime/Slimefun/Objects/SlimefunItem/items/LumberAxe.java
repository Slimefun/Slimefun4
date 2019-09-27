package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Block.TreeCalculator;
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
				if (MaterialCollections.contains(e.getBlock().getType(), MaterialCollections.getAllLogs())) {
					List<Location> logs = new ArrayList<>();
					TreeCalculator.getTree(e.getBlock().getLocation(), e.getBlock().getLocation(), logs);

					if (logs.contains(e.getBlock().getLocation())) logs.remove(e.getBlock().getLocation());
					for (Location b: logs) {
						if (SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), b, ProtectableAction.BREAK_BLOCK)) {
							b.getWorld().playEffect(b, Effect.STEP_SOUND, b.getBlock().getType());
							
							for (ItemStack drop: b.getBlock().getDrops()) {
								b.getWorld().dropItemNaturally(b, drop);
							}
							
							b.getBlock().setType(Material.AIR);
						}
					}
				}
				return true;
			}
			else return false;
		};
	}

}
