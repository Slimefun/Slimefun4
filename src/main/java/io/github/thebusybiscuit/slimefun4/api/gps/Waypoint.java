package io.github.thebusybiscuit.slimefun4.api.gps;

import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.WaypointCreateEvent;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.teleporter.Teleporter;

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

    private final UUID ownerId;
    private final String id;
    private final String name;
    private final Location location;

    /**
     * This constructs a new {@link Waypoint} object.
     *
     * @param profile
     *            The owning {@link PlayerProfile}
     * @param id
     *            The unique id for this {@link Waypoint}
     * @param loc
     *            The {@link Location} of the {@link Waypoint}
     * @param name
     *            The name of this {@link Waypoint}
     *
     * @deprecated Use {@link #Waypoint(UUID, String, Location, String)} instead
     */
    @Deprecated
    @ParametersAreNonnullByDefault
    public Waypoint(PlayerProfile profile, String id, Location loc, String name) {
        this(profile.getUUID(), id, loc, name);
    }

    /**
     * This constructs a new {@link Waypoint} object.
     * 
     * @param ownerId
     *            The owning {@link Player}'s {@link UUID}
     * @param id
     *            The unique id for this {@link Waypoint}
     * @param loc
     *            The {@link Location} of the {@link Waypoint}
     * @param name
     *            The name of this {@link Waypoint}
     */
    @ParametersAreNonnullByDefault
    public Waypoint(UUID ownerId, String id, Location loc, String name) {
        Validate.notNull(ownerId, "owner ID must never be null!");
        Validate.notNull(id, "id must never be null!");
        Validate.notNull(loc, "Location must never be null!");
        Validate.notNull(name, "Name must never be null!");

        this.ownerId = ownerId;
        this.id = id;
        this.location = loc;
        this.name = name;
    }

    /**
     * This returns the owner's {@link UUID} of the {@link Waypoint}.
     * 
     * @return The corresponding owner's {@link UUID}
     */
    @Nonnull
    public UUID getOwnerId() {
        return this.ownerId;
    }

    /**
     * This returns the owner of the {@link Waypoint}.
     *
     * @return The corresponding {@link PlayerProfile}
     *
     * @deprecated Use {@link #getOwnerId()} instead
     */
    @Nonnull
    @Deprecated
    public PlayerProfile getOwner() {
        // This is jank and should never actually return null
        return PlayerProfile.find(Bukkit.getOfflinePlayer(ownerId)).orElse(null);
    }

    /**
     * This method returns the unique identifier for this {@link Waypoint}.
     * 
     * @return The {@link Waypoint} id
     */
    @Nonnull
    public String getId() {
        return id;
    }

    /**
     * This returns the name of this {@link Waypoint}.
     * 
     * @return The name of this {@link Waypoint}
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * This returns the {@link Location} of this {@link Waypoint}
     * 
     * @return The {@link Waypoint} {@link Location}
     */
    @Nonnull
    public Location getLocation() {
        return location;
    }

    /**
     * This method returns whether this {@link Waypoint} is a Deathpoint.
     * 
     * @return Whether this is a Deathpoint
     */
    public boolean isDeathpoint() {
        return name.startsWith("player:death ");
    }

    /**
     * This method returns the {@link ItemStack} icon for this {@link Waypoint}.
     * The icon is dependent on the {@link Environment} the {@link Waypoint} is in
     * and whether it is a Deathpoint.
     * 
     * @return The {@link ItemStack} icon for this {@link Waypoint}
     */
    @Nonnull
    public ItemStack getIcon() {
        return Slimefun.getGPSNetwork().getIcon(name, location.getWorld().getEnvironment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.ownerId, this.id, this.name, this.location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Waypoint)) {
            return false;
        }

        Waypoint waypoint = (Waypoint) obj;
        return this.ownerId.equals(waypoint.getOwnerId())
            && id.equals(waypoint.getId())
            && location.equals(waypoint.getLocation())
            && name.equals(waypoint.getName());
    }
}
