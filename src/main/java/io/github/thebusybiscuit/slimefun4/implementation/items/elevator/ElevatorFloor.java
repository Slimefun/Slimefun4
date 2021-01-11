package io.github.thebusybiscuit.slimefun4.implementation.items.elevator;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
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
     * The floor number.
     */
    private final int number;

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
    public ElevatorFloor(@Nonnull String name, int number, @Nonnull Block block) {
        Validate.notNull(name, "An ElevatorFloor must have a name");
        Validate.notNull(block, "An ElevatorFloor must have a block");

        this.name = name;
        this.number = number;
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

    /**
     * This returns the "altitude" of this floor.
     * This is equivalent to the Y level of {@link #getLocation()}.
     * 
     * @return The altitude of this floor
     */
    public int getAltitude() {
        return location.getBlockY();
    }

    /**
     * This returns the number of this floor.
     * The lowest floor will have the number 0 and it
     * increments from there.
     * 
     * @return The number of this floor.
     */
    public int getNumber() {
        return number;
    }

}