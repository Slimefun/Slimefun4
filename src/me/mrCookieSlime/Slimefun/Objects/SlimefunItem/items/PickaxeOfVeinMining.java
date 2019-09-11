package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Block.Vein;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class PickaxeOfVeinMining extends SimpleSlimefunItem<BlockBreakHandler> {

	public PickaxeOfVeinMining(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}
	
	@Override
	public BlockBreakHandler getItemHandler() {
		return (e, item, fortune, drops) -> {
			if (SlimefunManager.isItemSimiliar(item, getItem(), true)) {
				if (e.getBlock().getType().toString().endsWith("_ORE")) {
					List<Location> blocks = new ArrayList<>();
					Vein.calculate(e.getBlock().getLocation(), e.getBlock().getLocation(), blocks, 16);
					
					for (Location block: blocks) {
						if (SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), block, ProtectableAction.BREAK_BLOCK)) {
							Block b = block.getBlock();
							b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
							
							for (ItemStack drop: b.getDrops()) {
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
