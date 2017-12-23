package me.mrCookieSlime.Slimefun.listeners;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.GPS.Elevator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.Teleporter;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TeleporterListener implements Listener {
	
	BlockFace[] faces = {BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};
	
	public TeleporterListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onStarve(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.PHYSICAL)) return;
		
		if (e.getClickedBlock() == null) return;
		SlimefunItem item = BlockStorage.check(e.getClickedBlock());
		if (item == null) return;
		
		if (item.getID().equals("GPS_ACTIVATION_DEVICE_SHARED")) {
			SlimefunItem teleporter = BlockStorage.check(e.getClickedBlock().getRelative(BlockFace.DOWN));
			
			if (teleporter instanceof Teleporter) {
				for (BlockFace face: faces) {
					if (!BlockStorage.check(e.getClickedBlock().getRelative(BlockFace.DOWN).getRelative(face), "GPS_TELEPORTER_PYLON")) return;
				}
				
				try {
					((Teleporter) teleporter).onInteract(e.getPlayer(), e.getClickedBlock().getRelative(BlockFace.DOWN));
				} catch (Exception x) {
					x.printStackTrace();
				}
			}
		}
		else if (item.getID().equals("GPS_ACTIVATION_DEVICE_PERSONAL")) {
			if (BlockStorage.getBlockInfo(e.getClickedBlock(), "owner").equals(e.getPlayer().getUniqueId().toString())) {
				SlimefunItem teleporter = BlockStorage.check(e.getClickedBlock().getRelative(BlockFace.DOWN));
				
				if (teleporter instanceof Teleporter) {
					for (BlockFace face: faces) {
						if (!BlockStorage.check(e.getClickedBlock().getRelative(BlockFace.DOWN).getRelative(face), "GPS_TELEPORTER_PYLON")) return;
					}
					
					try {
						((Teleporter) teleporter).onInteract(e.getPlayer(), e.getClickedBlock().getRelative(BlockFace.DOWN));
					} catch (Exception x) {
						x.printStackTrace();
					}
				}
			}
			else e.setCancelled(true);
		}
		else if (item.getID().equals("ELEVATOR_PLATE")) {
			Elevator.openDialogue(e.getPlayer(), e.getClickedBlock());
		}
	}

}
