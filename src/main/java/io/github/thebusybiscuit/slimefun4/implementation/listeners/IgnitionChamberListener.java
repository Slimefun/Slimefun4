package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.InventoryHolder;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class IgnitionChamberListener implements Listener {

	public IgnitionChamberListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onIgnitionChamberItemMove(InventoryMoveItemEvent e) {
		InventoryHolder holder = e.getInitiator().getHolder();
		
		if (holder instanceof Hopper && BlockStorage.check(((Hopper) holder).getBlock(), "IGNITION_CHAMBER")) {
			e.setCancelled(true);
		}
	}
}
