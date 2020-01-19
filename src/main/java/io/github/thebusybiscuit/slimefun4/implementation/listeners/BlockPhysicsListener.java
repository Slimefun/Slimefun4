package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class BlockPhysicsListener implements Listener {

	public BlockPhysicsListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onBlockFall(EntityChangeBlockEvent e) {
		if (e.getEntity() instanceof FallingBlock) {
			if (SlimefunPlugin.getUtilities().blocks.contains(e.getEntity().getUniqueId())) {
				e.setCancelled(true);
				e.getEntity().remove();
			}
			else if (BlockStorage.hasBlockInfo(e.getBlock())) {
				e.setCancelled(true);
				FallingBlock fb = (FallingBlock) e.getEntity();

				if (fb.getDropItem()) {
					fb.getWorld().dropItemNaturally(fb.getLocation(), new ItemStack(fb.getBlockData().getMaterial(), 1));
				}
			}
		}
	}

	@EventHandler
	public void onPistonExtend(BlockPistonExtendEvent e) {
		for (Block b : e.getBlocks()) {
			if (BlockStorage.hasBlockInfo(b) || b.getRelative(e.getDirection()).getType() == Material.AIR && BlockStorage.hasBlockInfo(b.getRelative(e.getDirection()))) {
				e.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onPistonRetract(BlockPistonRetractEvent e) {
		if (e.isSticky()) {
			for (Block b : e.getBlocks()) {
				if (BlockStorage.hasBlockInfo(b) || b.getRelative(e.getDirection()).getType() == Material.AIR && BlockStorage.hasBlockInfo(b.getRelative(e.getDirection()))) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler
	public void onLiquidFlow(BlockFromToEvent e) {
		Block block = e.getToBlock();
		String item = BlockStorage.checkID(block);
		if (item != null) e.setCancelled(true);
	}

	@EventHandler
	public void onBucketUse(PlayerBucketEmptyEvent e) {
		// Fix for placing water on player heads
		Location l = e.getBlockClicked().getRelative(e.getBlockFace()).getLocation();
		if (BlockStorage.hasBlockInfo(l)) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onWitherDestroy(EntityChangeBlockEvent e) {
		if (e.getEntity() instanceof Wither) {
			String id = BlockStorage.checkID(e.getBlock());

			if (id != null && id.startsWith("WITHER_PROOF_")) {
				e.setCancelled(true);
			}
		}
	}
}
