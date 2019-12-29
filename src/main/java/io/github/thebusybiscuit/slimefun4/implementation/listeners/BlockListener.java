package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
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
}
