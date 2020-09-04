package io.github.thebusybiscuit.slimefun4.api.gps;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.WaypointCreateEvent;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.gps.Teleporter;

/**
 * A {@link Waypoint} represents a named {@link Location} that was created by a {@link Player}.
 * It can be used via a {@link Teleporter}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see WaypointCreateEvent
 * @see GPSNetwork
 * @see TeleportationManager
 * @see Teleporter
 *
 */
public class Waypoint {

    private final PlayerProfile profile;
    private final String id;
    private final String name;
    private final Location location;

    @ParametersAreNonnullByDefault
    public Waypoint(PlayerProfile profile, String id, Location l, String name) {
        Validate.notNull(profile, "Profile must never be null!");
        Validate.notNull(id, "id must never be null!");
        Validate.notNull(l, "Location must never be null!");
        Validate.notNull(name, "Name must never be null!");

        this.profile = profile;
        this.id = id;
        this.location = l;
        this.name = name;
    }

    @Nonnull
    public PlayerProfile getOwner() {
        return profile;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public Location getLocation() {
        return location;
    }

    public boolean isDeathpoint() {
        return name.startsWith("player:death ");
    }

    @Nonnull
    public ItemStack getIcon() {
        return SlimefunPlugin.getGPSNetwork().getIcon(name, location.getWorld().getEnvironment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(profile.getUUID(), id, name, location);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Waypoint)) {
            return false;
        }

        Waypoint waypoint = (Waypoint) obj;
        return profile.getUUID().equals(waypoint.getOwner().getUUID()) && id.equals(waypoint.getId()) && location.equals(waypoint.getLocation()) && name.equals(waypoint.getName());
    }

}
