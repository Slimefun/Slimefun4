package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import io.github.thebusybiscuit.slimefun4.implementation.items.gps.ElevatorPlate;
import io.github.thebusybiscuit.slimefun4.implementation.items.gps.Teleporter;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class TeleporterListener implements Listener {

	private final BlockFace[] faces = {BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};

	public TeleporterListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onPressurePlateEnter(PlayerInteractEvent e) {
		if (e.getAction() != Action.PHYSICAL || e.getClickedBlock() == null) return;

		String id = BlockStorage.checkID(e.getClickedBlock());
		if (id == null) return;

		if (id.equals("GPS_ACTIVATION_DEVICE_SHARED") || (id.equals("GPS_ACTIVATION_DEVICE_PERSONAL") && BlockStorage.getLocationInfo(e.getClickedBlock().getLocation(), "owner").equals(e.getPlayer().getUniqueId().toString()))) {
			SlimefunItem teleporter = BlockStorage.check(e.getClickedBlock().getRelative(BlockFace.DOWN));

			if (teleporter instanceof Teleporter && checkForPylons(e.getClickedBlock().getRelative(BlockFace.DOWN))) {
				Block block = e.getClickedBlock().getRelative(BlockFace.DOWN);
				UUID owner = UUID.fromString(BlockStorage.getLocationInfo(block.getLocation(), "owner"));
				SlimefunPlugin.getGPSNetwork().getTeleleportationService().openTeleporterGUI(e.getPlayer(), owner, block, SlimefunPlugin.getGPSNetwork().getNetworkComplexity(owner));
			}
		}
		else if (id.equals("ELEVATOR_PLATE")) {
			((ElevatorPlate) SlimefunItem.getByID("ELEVATOR_PLATE")).open(e.getPlayer(), e.getClickedBlock());
		}
	}

	private boolean checkForPylons(Block teleporter) {
		for (BlockFace face : faces) {
			if (!BlockStorage.check(teleporter.getRelative(face), "GPS_TELEPORTER_PYLON")) {
				return false;
			}
		}
		
		return true;
	}

}
