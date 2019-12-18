package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.gps;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GPS.Elevator;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class ElevatorPlate extends SimpleSlimefunItem<ItemInteractionHandler> {

	public ElevatorPlate(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		super(category, item, recipeType, recipe, recipeOutput);
		
		SlimefunItem.registerBlockHandler(getID(), new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "floor", "&rFloor #0");
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				return true;
			}
		});
	}
	
	@Override
	protected boolean areItemHandlersPrivate() {
		return false;
	}

	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, item) -> {
			if (e.getClickedBlock() == null) return false;
			
			String id = BlockStorage.checkID(e.getClickedBlock());
			if (id == null || !id.equals(getID())) return false;

			if (BlockStorage.getLocationInfo(e.getClickedBlock().getLocation(), "owner").equals(p.getUniqueId().toString())) {
				Elevator.openEditor(p, e.getClickedBlock());
			}
			
			return true;
		};
	}

}
