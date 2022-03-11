package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoNode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * This {@link Listener} is solely responsible for preventing Cargo Nodes from being placed
 * on the top or bottom of a block.
 *
 * @author TheBusyBiscuit
 */
public class CargoNodeListener implements Listener {
	
	public CargoNodeListener(@Nonnull Slimefun plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onCargoNodePlace(BlockPlaceEvent e) {
		Block b = e.getBlock();
		
		if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
			if (e.getBlockReplacedState().getType() == Material.LIGHT) return;
		}
		if ((b.getY() != e.getBlockAgainst().getY() || !e.getBlockReplacedState().getType().isAir()) && isCargoNode(e.getItemInHand())) {
			
			Slimefun.getLocalization().sendMessage(e.getPlayer(), "machines.CARGO_NODES.must-be-placed", true);
			e.setCancelled(true);
		}
	}
	
	private boolean isCargoNode(@Nonnull ItemStack item) {
		return SlimefunItem.getByItem(item) instanceof CargoNode;
	}
}
