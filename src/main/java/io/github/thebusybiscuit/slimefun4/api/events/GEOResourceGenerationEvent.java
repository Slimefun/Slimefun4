package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.api.geo.ResourceManager;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOScanner;

/**
 * This {@link Event} is fired whenever a {@link GEOResource} is being freshly generated.
 * This only occurs when a {@link GEOScanner} queries the {@link Chunk} for a {@link GEOResource}
 * but cannot find it.
 * 
 * You can modify this {@link Event} by listening to it.
 * 
 * @author TheBusyBiscuit
 * 
 * @see ResourceManager
 * @see GEOResource
 * @see GEOScanner
 *
 */
public class GEOResourceGenerationEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final World world;
    private final Biome biome;
    private final GEOResource resource;
    private final int x;
    private final int z;

    private int value;

    @ParametersAreNonnullByDefault
    public GEOResourceGenerationEvent(World world, Biome biome, int x, int z, GEOResource resource, int value) {
        this.world = world;
        this.biome = biome;
        this.resource = resource;
        this.x = x;
        this.z = z;

        this.value = value;
    }

    /**
     * This returns the amount that will be generated of this {@link GEOResource}.
     * 
     * @return The value aka the supply of this {@link GEOResource} to generate
     */
    public int getValue() {
        return value;
    }

    /**
     * This modifies the amount that will be generated.
     * 
     * @param value
     *            The new supply for this {@link GEOResource}
     */
    public void setValue(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("You cannot set a GEO-Resource supply to a negative value.");
        }

        this.value = value;
    }

    /**
     * This returns the {@link World} in which this event takes place.
     * 
     * @return The affected {@link World}
     */
    @Nonnull
    public World getWorld() {
        return world;
    }

    /**
     * This method returns the {@link GEOResource} that is being generated
     * 
     * @return The generated {@link GEOResource}
     */
    @Nonnull
    public GEOResource getResource() {
        return resource;
    }

    /**
     * This returns the X coordinate of the {@link Chunk} in which the {@link GEOResource}
     * is generated.
     * 
     * @return The x value of this {@link Chunk}
     */
    public int getChunkX() {
        return x;
    }

    /**
     * This returns the Z coordinate of the {@link Chunk} in which the {@link GEOResource}
     * is generated.
     * 
     * @return The z value of this {@link Chunk}
     */
    public int getChunkZ() {
        return z;
    }

    /**
     * This method returns the {@link Environment} in which the resource is generated.
     * It is equivalent to {@link World#getEnvironment()}.
     * 
     * @return The {@link Environment} of this generation
     */
    @Nonnull
    public Environment getEnvironment() {
        return world.getEnvironment();
    }

    /**
     * This returns the {@link Biome} at the {@link Location} at which the {@link GEOResource} is
     * generated.
     * 
     * @return The {@link Biome} of this generation
     */
    @Nonnull
    public Biome getBiome() {
        return biome;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

}
