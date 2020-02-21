package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

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
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockDispenseHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class BlockPlacer extends SimpleSlimefunItem<BlockDispenseHandler> {
	
	private String[] blacklist;
	
	public BlockPlacer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
		super(category, item, recipeType, recipe, keys, values);
	}
	
	@Override
	protected boolean areItemHandlersPrivate() {
		return false;
	}
	
	@Override
	public BlockDispenseHandler getItemHandler() {
		return (e, dispenser, facedBlock, machine) -> {
			e.setCancelled(true);
			
			if ((facedBlock.getType() == null || facedBlock.getType() == Material.AIR) && e.getItem().getType().isBlock()) {
				for (String blockType : blacklist) {
					if (e.getItem().getType().toString().equals(blockType)) {
						return;
					}
				}
				
				SlimefunItem sfItem = SlimefunItem.getByItem(e.getItem());
				
				if (sfItem != null) {
					if (!SlimefunPlugin.getRegistry().getBlockHandlers().containsKey(sfItem.getID())) {
						facedBlock.setType(e.getItem().getType());
						BlockStorage.store(facedBlock, sfItem.getID());
						facedBlock.getWorld().playEffect(facedBlock.getLocation(), Effect.STEP_SOUND, e.getItem().getType());
						
						if (dispenser.getInventory().containsAtLeast(e.getItem(), 2)) {
							dispenser.getInventory().removeItem(new CustomItem(e.getItem(), 1));
						}
						else {
							Slimefun.runSync(() -> dispenser.getInventory().removeItem(e.getItem()), 2L);
						}
					}
				}
				else {
					facedBlock.setType(e.getItem().getType());
					
					if (e.getItem().hasItemMeta() && e.getItem().getItemMeta() instanceof BlockStateMeta) {
						BlockState itemBlockState = ((BlockStateMeta) e.getItem().getItemMeta()).getBlockState();
						BlockState blockState = facedBlock.getState();
						
						if ((blockState instanceof Nameable) && e.getItem().getItemMeta().hasDisplayName()) {
							((Nameable) blockState).setCustomName(e.getItem().getItemMeta().getDisplayName());
						}
						
						//Update block state after changing name
						blockState.update();
						
						//Changing the inventory of the block based on the inventory of the block's itemstack (Currently only applies to shulker boxes)
						//Inventory has to be changed after blockState.update() as updating it will create a different Inventory for the object
						if (facedBlock.getState() instanceof BlockInventoryHolder) {
							((BlockInventoryHolder) facedBlock.getState()).getInventory().setContents(((BlockInventoryHolder) itemBlockState).getInventory().getContents());
						}
						
					}
					
					facedBlock.getWorld().playEffect(facedBlock.getLocation(), Effect.STEP_SOUND, e.getItem().getType());
					
					if (dispenser.getInventory().containsAtLeast(e.getItem(), 2)) {
						dispenser.getInventory().removeItem(new CustomItem(e.getItem(), 1));
					}
					else {
						Slimefun.runSync(() -> dispenser.getInventory().removeItem(e.getItem()), 2L);
					}
				}
			}
		};
	}

	@Override
	public void postRegister() {
		List<?> list = (List<?>) Slimefun.getItemValue(getID(), "unplaceable-blocks");
		blacklist = list.toArray(new String[0]);
	}
}
