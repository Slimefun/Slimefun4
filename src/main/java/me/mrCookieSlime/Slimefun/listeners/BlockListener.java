package me.mrCookieSlime.Slimefun.listeners;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Events.MultiBlockInteractEvent;
import me.mrCookieSlime.Slimefun.Objects.MultiBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.MultiBlockInteractionHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class BlockListener implements Listener {
	
	public BlockListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onBlockFall(EntityChangeBlockEvent event) {
		if (event.getEntity() instanceof FallingBlock && BlockStorage.hasBlockInfo(event.getBlock())) {
			event.setCancelled(true);
			FallingBlock fb = (FallingBlock) event.getEntity();
			
			if (fb.getDropItem()) {
				fb.getWorld().dropItemNaturally(fb.getLocation(), new ItemStack(fb.getBlockData().getMaterial(), 1));
			}
		}
	}

	@EventHandler
	public void onPistonExtend(BlockPistonExtendEvent e) {
		for (Block b: e.getBlocks()) {
			if (BlockStorage.hasBlockInfo(b) || b.getRelative(e.getDirection()).getType() == Material.AIR && BlockStorage.hasBlockInfo(b.getRelative(e.getDirection()))) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onPistonRetract(BlockPistonRetractEvent e) {
		if (e.isSticky()) {
			for (Block b: e.getBlocks()) {
				if (BlockStorage.hasBlockInfo(b) || b.getRelative(e.getDirection()).getType() == Material.AIR && BlockStorage.hasBlockInfo(b.getRelative(e.getDirection()))) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if (e.getHand() != EquipmentSlot.HAND) return;
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player p = e.getPlayer();
			Block b = e.getClickedBlock();
			LinkedList<MultiBlock> multiblocks = new LinkedList<>();
			
			for (MultiBlock mb: MultiBlock.list()) {
				Block center = b.getRelative(mb.getTriggerBlock());
				
				if (compareMaterials(center, mb.getBuild(), mb.isSymmetric())) {
					multiblocks.add(mb);	
				}
			}
			
			if (!multiblocks.isEmpty()) {
				e.setCancelled(true);
				MultiBlock multiblock = multiblocks.getLast();
				
				for (ItemHandler handler: SlimefunItem.getHandlers("MultiBlockInteractionHandler")) {
					if (((MultiBlockInteractionHandler) handler).onInteract(p, multiblock, b)) break;
				}
				
				MultiBlockInteractEvent event = new MultiBlockInteractEvent(p, multiblock, b);
				Bukkit.getPluginManager().callEvent(event);
			}
		}
	}
	
	protected boolean compareMaterials(Block b, Material[] blocks, boolean onlyTwoWay) {
		if (!compareMaterialsVertical(b, blocks[1], blocks[4], blocks[7])) {
			return false;
		}
		
		BlockFace[] directions = onlyTwoWay ? new BlockFace[] {BlockFace.NORTH, BlockFace.EAST} : new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
		
		for (BlockFace direction : directions) {
			if (compareMaterialsVertical(b.getRelative(direction), blocks[0], blocks[3], blocks[6]) 
				&& compareMaterialsVertical(b.getRelative(direction.getOppositeFace()), blocks[2], blocks[5], blocks[8])) {
				return true;
			}
		}
		
		return false;
	}
	
	protected boolean compareMaterialsVertical(Block b, Material top, Material center, Material bottom) {
		if (center != null && b.getType() != center) {
			return false;
		}
		
		if (top != null && b.getRelative(BlockFace.UP).getType() != top) {
			return false;
		}
		
		if (bottom != null && b.getRelative(BlockFace.DOWN).getType() != bottom) {
			return false;
		}
					
		return true;
	}
}
