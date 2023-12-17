package io.github.thebusybiscuit.slimefun4.storage.backend.legacy;

import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.api.gps.Waypoint;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.storage.Storage;
import io.github.thebusybiscuit.slimefun4.storage.data.PlayerData;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

import com.google.common.annotations.Beta;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

@Beta
public class LegacyStorage implements Storage {

    @Override
    public PlayerData loadPlayerData(@Nonnull UUID uuid) {
        Config playerFile = new Config("data-storage/Slimefun/Players/" + uuid + ".yml");
        // Not too sure why this is it's own file
        Config waypointsFile = new Config("data-storage/Slimefun/waypoints/" + uuid + ".yml");

        // Load research
        Set<Research> researches = new HashSet<>();
        for (Research research : Slimefun.getRegistry().getResearches()) {
            if (playerFile.contains("researches." + research.getID())) {
                researches.add(research);
            }
        }

        // Load waypoints
        Set<Waypoint> waypoints = new HashSet<>();
        for (String key : waypointsFile.getKeys()) {
            try {
                if (waypointsFile.contains(key + ".world") && Bukkit.getWorld(waypointsFile.getString(key + ".world")) != null) {
                    String waypointName = waypointsFile.getString(key + ".name");
                    Location loc = waypointsFile.getLocation(key);
                    waypoints.add(new Waypoint(uuid, key, loc, waypointName));
                }
            } catch (Exception x) {
                Slimefun.logger().log(Level.WARNING, x, () -> "Could not load Waypoint \"" + key + "\" for Player \"" + uuid + '"');
            }
        }

        // TODO:
        // * Backpacks

        return new PlayerData(researches, waypoints);
    }

    @Override
    public void savePlayerData(@Nonnull UUID uuid, @Nonnull PlayerData data) {
        Config playerFile = new Config("data-storage/Slimefun/Players/" + uuid + ".yml");
        // Not too sure why this is it's own file
        Config waypointsFile = new Config("data-storage/Slimefun/waypoints/" + uuid + ".yml");

        // Save research
        for (Research research : data.getResearches()) {
            // Legacy data uses IDs
            playerFile.setValue("researches." + research.getID(), true);
        }

        // Save waypoints
        for (Waypoint waypoint : data.getWaypoints()) {
            // Legacy data uses IDs
            waypointsFile.setValue(waypoint.getId(), waypoint.getLocation());
            waypointsFile.setValue(waypoint.getId() + ".name", waypoint.getName());
        }

        // Save files
        playerFile.save();
        waypointsFile.save();
    }
}
