package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockInteractEvent;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.MultiBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.MultiBlockInteractionHandler;

public class MultiBlockListener implements Listener {
	
	public MultiBlockListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getHand() != EquipmentSlot.HAND) return;
		
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		LinkedList<MultiBlock> multiblocks = new LinkedList<>();
		
		for (MultiBlock mb : MultiBlock.list()) {
			Block center = b.getRelative(mb.getTriggerBlock());
			
			if (compareMaterials(center, mb.getBuild(), mb.isSymmetric())) {
				multiblocks.add(mb);
			}
		}
		
		if (!multiblocks.isEmpty()) {
			e.setCancelled(true);
			
			MultiBlock mb = multiblocks.getLast();
			mb.getSlimefunItem().callItemHandler(MultiBlockInteractionHandler.class, handler -> handler.onInteract(p, mb, b));
			Bukkit.getPluginManager().callEvent(new MultiBlockInteractEvent(p, mb, b));
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
		if (center != null && !equals(b.getType(), center)) {
			return false;
		}
		
		if (top != null && !equals(b.getRelative(BlockFace.UP).getType(), top)) {
			return false;
		}
		
		if (bottom != null && !equals(b.getRelative(BlockFace.DOWN).getType(), bottom)) {
			return false;
		}
					
		return true;
	}
	
	private boolean equals(Material a, Material b) {
		if (a == b) return true;
		
		for (Tag<Material> tag : MultiBlock.SUPPORTED_TAGS) {
			if (tag.isTagged(a) && tag.isTagged(b)) {
				return true;
			}
		}
		
		return false;
	}
}
