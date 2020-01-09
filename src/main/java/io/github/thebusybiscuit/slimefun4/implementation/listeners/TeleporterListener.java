package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.GPS.Elevator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.Teleporter;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import java.util.logging.Level;

import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TeleporterListener implements Listener {
	
	private final BlockFace[] faces = {BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};
	
	public TeleporterListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onStarve(PlayerInteractEvent e) {
		if (e.getAction() != Action.PHYSICAL || e.getClickedBlock() == null) return;
		
		String id = BlockStorage.checkID(e.getClickedBlock());
		if (id == null) return;
		
		if (id.equals("GPS_ACTIVATION_DEVICE_SHARED") || (id.equals("GPS_ACTIVATION_DEVICE_PERSONAL") && BlockStorage.getLocationInfo(e.getClickedBlock().getLocation(), "owner").equals(e.getPlayer().getUniqueId().toString()))) {
			SlimefunItem teleporter = BlockStorage.check(e.getClickedBlock().getRelative(BlockFace.DOWN));
			
			if (teleporter instanceof Teleporter) {
				for (BlockFace face : faces) {
					if (!BlockStorage.check(e.getClickedBlock().getRelative(BlockFace.DOWN).getRelative(face), "GPS_TELEPORTER_PYLON")) return;
				}
				
				try {
					((Teleporter) teleporter).onInteract(e.getPlayer(), e.getClickedBlock().getRelative(BlockFace.DOWN));
				} catch (Exception x) {
					Slimefun.getLogger().log(Level.SEVERE, "An Error occured while interacting with a Teleporter", x);
				}
			}
		}
		else if (id.equals("ELEVATOR_PLATE")) {
			Elevator.openDialogue(e.getPlayer(), e.getClickedBlock());
		}
	}

}
