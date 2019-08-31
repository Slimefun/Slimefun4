package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.AutonomousMachineHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class BlockPlacer extends SlimefunItem {
	
	public BlockPlacer(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
		super(category, item, id, recipeType, recipe, keys, values);
	}

	@Override
	public void register(boolean slimefun) {
		Object value = Slimefun.getItemValue(getID(), "unplaceable-blocks");
		String[] blacklist = ((List<?>) value).stream().toArray(String[]::new);
		
		addItemHandler(new AutonomousMachineHandler() {

			@Override
			public boolean onBlockDispense(final BlockDispenseEvent e, Block dispenser, final Dispenser d, Block block, Block chest, SlimefunItem machine) {
				if (machine.getID().equalsIgnoreCase(getID())) {
					e.setCancelled(true);
					
					if ((block.getType() == null || block.getType() == Material.AIR) && e.getItem().getType().isBlock()) {
						for (String blockType : blacklist) {
							if (e.getItem().getType().toString().equals(blockType)) {
								return false;
							}
						}
						
						SlimefunItem sfItem = SlimefunItem.getByItem(e.getItem());
						if (sfItem != null) {
							if (!SlimefunItem.blockhandler.containsKey(sfItem.getID())) {
								block.setType(e.getItem().getType());
								BlockStorage.store(block, sfItem.getID());
								block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, e.getItem().getType());
								if (d.getInventory().containsAtLeast(e.getItem(), 2)) d.getInventory().removeItem(new CustomItem(e.getItem(), 1));
								else {
									Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance, () -> d.getInventory().removeItem(e.getItem()), 2L);
								}
							}
						}
						else {
							block.setType(e.getItem().getType());
							block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, e.getItem().getType());
							if (d.getInventory().containsAtLeast(e.getItem(), 2)) d.getInventory().removeItem(new CustomItem(e.getItem(), 1));
							else {
								Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance, () -> d.getInventory().removeItem(e.getItem()), 2L);
							}
						}
					}
					return true;
				}
				else return false;
			}
		});
		
		super.register(slimefun);
	}
}
