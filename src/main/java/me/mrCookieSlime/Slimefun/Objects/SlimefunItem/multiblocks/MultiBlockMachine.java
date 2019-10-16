package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.Objects.handlers.MultiBlockInteractionHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public abstract class MultiBlockMachine extends SlimefunMachine {
	
	private static final BlockFace[] outputFaces = {
			BlockFace.UP,
		    BlockFace.NORTH,
		    BlockFace.EAST,
		    BlockFace.SOUTH,
		    BlockFace.WEST
	};
	
	@Deprecated
	public MultiBlockMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger) {
		super(category, item, id, recipe, machineRecipes, trigger);
	}
	
	@Deprecated
	public MultiBlockMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger, String[] keys, Object[] values) {
		super(category, item, id, recipe, machineRecipes, trigger, keys, values);
	}
	
	public MultiBlockMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, BlockFace trigger) {
		super(category, item, id, recipe, machineRecipes, trigger);
	}

	public MultiBlockMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, BlockFace trigger, String[] keys, Object[] values) {
		super(category, item, id, recipe, machineRecipes, trigger, keys, values);
	}
	
	@Override
	public void register() {
		register(true, getInteractionHandler());
	}
	
	protected MultiBlockInteractionHandler getInteractionHandler() {
		return (p, mb, b) -> {
			if (mb.isMultiBlock(this)) {
				if (!isDisabled() && SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES) && Slimefun.hasUnlocked(p, this, true)) {
					onInteract(p, b);
				}
				
				return true;
			}
			else return false;
		};
	}
	
	public abstract void onInteract(Player p, Block b);
	
	// Overloaded method for finding a potential output chest. Fallbacks to the old system of putting the adding back into the dispenser.
	// Optional last argument Inventory placeCheckerInv is for multiblock machines that create a dummy inventory to check if there's a space for the adding,
	// i.e. Enhanced crafting table
	protected Inventory findOutputInventory(ItemStack adding, Block dispBlock, Inventory dispInv) {
		return findOutputInventory(adding, dispBlock, dispInv, dispInv);
	}
	
	protected Inventory findOutputInventory(ItemStack product, Block dispBlock, Inventory dispInv, Inventory placeCheckerInv) {
		Inventory outputInv = null;
		
		for (BlockFace face : outputFaces) {
			Block potentialOutput = dispBlock.getRelative(face);
			String id = BlockStorage.checkID(potentialOutput);
			if (id != null && id.equals("OUTPUT_CHEST")) {
				// Found the output chest! Now, let's check if we can fit the product in it.
				Inventory inv = ((Container) potentialOutput.getState()).getInventory();
				if (InvUtils.fits(inv, product)) {
					// It fits! Let's set the inventory to that now.
					outputInv = inv;
					break;
				}
			}
		}
		// This if-clause will trigger if no suitable output chest was found. It's functionally the same as the old fit check for the dispenser, only refactored.
		if (outputInv == null && InvUtils.fits(placeCheckerInv, product)) outputInv = dispInv;	
		
		return outputInv;
	}

}
