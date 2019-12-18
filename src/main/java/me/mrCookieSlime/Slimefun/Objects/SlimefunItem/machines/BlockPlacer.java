package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.AutonomousMachineHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class BlockPlacer extends SimpleSlimefunItem<AutonomousMachineHandler> {
	
	private String[] blacklist;
	
	public BlockPlacer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
		super(category, item, recipeType, recipe, keys, values);
	}
	
	@Override
	protected boolean areItemHandlersPrivate() {
		return false;
	}
	
	@Override
	public AutonomousMachineHandler getItemHandler() {
		return (e, dispenser, d, block, chest, machine) -> {
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
						if (!SlimefunPlugin.getUtilities().blockHandlers.containsKey(sfItem.getID())) {
							block.setType(e.getItem().getType());
							BlockStorage.store(block, sfItem.getID());
							block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, e.getItem().getType());
							if (d.getInventory().containsAtLeast(e.getItem(), 2)) {
								d.getInventory().removeItem(new CustomItem(e.getItem(), 1));
							}
							else {
								Slimefun.runSync(() -> d.getInventory().removeItem(e.getItem()), 2L);
							}
						}
					}
					else {
						block.setType(e.getItem().getType());
						if (e.getItem().hasItemMeta() && e.getItem().getItemMeta() instanceof BlockStateMeta) {
							BlockState itemBlockState = ((BlockStateMeta) e.getItem().getItemMeta()).getBlockState();
							BlockState blockState = block.getState();
							
							if ((blockState instanceof Nameable) && e.getItem().getItemMeta().hasDisplayName()) {
								((Nameable) blockState).setCustomName(e.getItem().getItemMeta().getDisplayName());
							}
							
							//Update block state after changing name
							blockState.update();
							
							//Changing the inventory of the block based on the inventory of the block's itemstack (Currently only applies to shulker boxes)
							//Inventory has to be changed after blockState.update() as updating it will create a different Inventory for the object
							if (block.getState() instanceof BlockInventoryHolder) {
								((BlockInventoryHolder) block.getState()).getInventory().setContents(((BlockInventoryHolder) itemBlockState).getInventory().getContents());
							}
							
						}
						block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, e.getItem().getType());
						if (d.getInventory().containsAtLeast(e.getItem(), 2)) {
							d.getInventory().removeItem(new CustomItem(e.getItem(), 1));
						}
						else {
							Slimefun.runSync(() -> d.getInventory().removeItem(e.getItem()), 2L);
						}
					}
				}
				return true;
			}
			else return false;
		};
	}

	@Override
	public void postRegister() {
		List<?> list = (List<?>) Slimefun.getItemValue(getID(), "unplaceable-blocks");
		blacklist = list.toArray(new String[0]);
	}
}
