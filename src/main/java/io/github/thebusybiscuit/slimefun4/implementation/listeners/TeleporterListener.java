package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.elevator.ElevatorPlate;
import io.github.thebusybiscuit.slimefun4.implementation.items.gps.Teleporter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class TeleporterListener implements Listener {

    private final BlockFace[] faces = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };

    public TeleporterListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPressurePlateEnter(PlayerInteractEvent e) {
        if (e.getAction() != Action.PHYSICAL || e.getClickedBlock() == null) {
            return;
        }

        SlimefunItem item = BlockStorage.check(e.getClickedBlock());

        if (item == null || item.isDisabledIn(e.getPlayer().getWorld())) {
            return;
        }

        if (isTeleporterPad(item, e.getClickedBlock(), e.getPlayer().getUniqueId())) {
            SlimefunItem teleporter = BlockStorage.check(e.getClickedBlock().getRelative(BlockFace.DOWN));

            if (teleporter instanceof Teleporter && checkForPylons(e.getClickedBlock().getRelative(BlockFace.DOWN))) {
                Block block = e.getClickedBlock().getRelative(BlockFace.DOWN);
                UUID owner = UUID.fromString(BlockStorage.getLocationInfo(block.getLocation(), "owner"));
                SlimefunPlugin.getGPSNetwork().getTeleportationManager().openTeleporterGUI(e.getPlayer(), owner, block, SlimefunPlugin.getGPSNetwork().getNetworkComplexity(owner));
            }
        } else if (item instanceof ElevatorPlate) {
            ElevatorPlate elevator = ((ElevatorPlate) SlimefunItems.ELEVATOR_PLATE.getItem());
            elevator.openInterface(e.getPlayer(), e.getClickedBlock());
        }
    }

    @ParametersAreNonnullByDefault
    private boolean isTeleporterPad(SlimefunItem item, Block b, UUID uuid) {
        if (item.getId().equals(SlimefunItems.GPS_ACTIVATION_DEVICE_SHARED.getItemId())) {
            return true;
        } else if (item.getId().equals(SlimefunItems.GPS_ACTIVATION_DEVICE_PERSONAL.getItemId())) {
            return BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(uuid.toString());
        } else {
            return false;
        }
    }

    private boolean checkForPylons(@Nonnull Block teleporter) {
        for (BlockFace face : faces) {
            if (!BlockStorage.check(teleporter.getRelative(face), SlimefunItems.GPS_TELEPORTER_PYLON.getItemId())) {
                return false;
            }
        }

        return true;
    }

}
