package io.github.thebusybiscuit.slimefun4.implementation.items.elevator;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * This represents an {@link ElevatorFloor} to which a {@link Player}
 * can travel to using an {@link ElevatorPlate}.
 * 
 * @author TheBusyBiscuit
 *
 */
class ElevatorFloor {

    /**
     * The name of this floor.
     */
    private final String name;

    /**
     * The {@link Location} of this floor.
     */
    private final Location location;

    /**
     * This constructs a new {@link ElevatorFloor} with the given name
     * and the {@link Location} of the provided {@link Block}.
     * 
     * @param name
     *            The name of this {@link ElevatorFloor}
     * @param block
     *            The {@link Block} of this floor
     */
    public ElevatorFloor(@Nonnull String name, @Nonnull Block block) {
        this.name = name;
        this.location = block.getLocation();
    }

    /**
     * This returns the name of this {@link ElevatorFloor}.
     * 
     * @return The name of this floor
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * This returns the {@link Location} of this {@link ElevatorFloor}.
     * 
     * @return The {@link Location} of this floor
     */
    @Nonnull
    public Location getLocation() {
        return location;
    }

}
