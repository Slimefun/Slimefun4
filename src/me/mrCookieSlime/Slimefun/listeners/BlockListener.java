package me.mrCookieSlime.Slimefun.listeners;

import java.util.ArrayList;
import java.util.List;

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
		for (Block b : e.getBlocks()) {
			if (BlockStorage.hasBlockInfo(b) ||
					b.getRelative(e.getDirection()).getType() == Material.AIR && BlockStorage.hasBlockInfo(b.getRelative(e.getDirection()))) {

						e.setCancelled(true);
						return;
			}
		}
	}
	
	@EventHandler
	public void onPistonRetract(BlockPistonRetractEvent e) {
		if (e.isSticky()) {
			for (Block b : e.getBlocks()) {
				if (BlockStorage.hasBlockInfo(b) ||
						b.getRelative(e.getDirection()).getType() == Material.AIR && BlockStorage.hasBlockInfo(b.getRelative(e.getDirection()))) {

							e.setCancelled(true);
							return;
				}
			}
		}
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getHand() != EquipmentSlot.HAND) return;
				Player p = e.getPlayer();
				Block b = e.getClickedBlock();
				List<MultiBlock> multiblocks = new ArrayList<>();
				for (MultiBlock mb: MultiBlock.list()) {
					Block center = b.getRelative(mb.getTriggerBlock());
					if (compareMaterials(center, mb.getBuild(), mb.isSymmetric()))
						multiblocks.add(mb);	
				}
				
			if (!multiblocks.isEmpty()) {
				e.setCancelled(true);
				MultiBlock multiblock = multiblocks.get(multiblocks.size() - 1);
				
				for (ItemHandler handler: SlimefunItem.getHandlers("MultiBlockInteractionHandler")) {
					if (((MultiBlockInteractionHandler) handler).onInteract(p, multiblock, b)) break;
				}
				
				MultiBlockInteractEvent event = new MultiBlockInteractEvent(p, multiblock, b);
				Bukkit.getPluginManager().callEvent(event);
			}
		}
	}
	
	protected boolean compareMaterials(Block b, Material[] materials, boolean onlyTwoWay)
	{
		return compareMaterials(b, materials[0], materials[1], materials[2], materials[3], materials[4], materials[5], materials[6], materials[7], materials[8], onlyTwoWay);
	}
	
	protected boolean compareMaterials(Block b, Material mat1, Material mat2, Material mat3, Material mat4, Material mat5, Material mat6, Material mat7, Material mat8, Material mat9, boolean onlyTwoWay)
	{
		if (!compareMaterialsVertical(b, mat2, mat5, mat8))
			return false;
		
		BlockFace[] directions = onlyTwoWay ? new BlockFace[] {BlockFace.NORTH, BlockFace.EAST} : new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
		for (BlockFace direction : directions)
		{
			if (compareMaterialsVertical(b.getRelative(direction), mat1, mat4, mat7))
				if (compareMaterialsVertical(b.getRelative(direction.getOppositeFace()), mat3, mat6, mat9))
					return true;
		}
		return false;
	}
	
	protected boolean compareMaterialsVertical(Block b, Material mat1, Material mat2, Material mat3)
	{
		if (mat2 != null && b.getType() != mat2)
			return false;
		
		if (mat1 != null && b.getRelative(BlockFace.UP).getType() != mat1)
			return false;
		
		if (mat3 != null && b.getRelative(BlockFace.DOWN).getType() != mat3)
			return false;
					
		return true;
	}
}
