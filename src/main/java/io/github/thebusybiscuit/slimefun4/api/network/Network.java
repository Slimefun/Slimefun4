package io.github.thebusybiscuit.slimefun4.api.network;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;

import io.github.thebusybiscuit.slimefun4.core.networks.NetworkManager;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.NetworkListener;

/**
 * An abstract Network class to manage networks in a stateful way
 * 
 * @author meiamsome
 * 
 * @see NetworkListener
 * @see NetworkManager
 *
 */
public abstract class Network {

    private final NetworkManager manager;
    protected Location regulator;
    private final Queue<Location> nodeQueue = new ArrayDeque<>();

    protected final Set<Location> connectedLocations = new HashSet<>();
    protected final Set<Location> regulatorNodes = new HashSet<>();
    protected final Set<Location> connectorNodes = new HashSet<>();
    protected final Set<Location> terminusNodes = new HashSet<>();

    /**
     * This constructs a new {@link Network} at the given {@link Location}.
     * 
     * @param manager
     *            The {@link NetworkManager} instance
     * @param regulator
     *            The {@link Location} marking the regulator of this {@link Network}.
     */
    protected Network(@Nonnull NetworkManager manager, @Nonnull Location regulator) {
        Validate.notNull(manager, "A NetworkManager must be provided");
        Validate.notNull(regulator, "No regulator was specified");

        this.manager = manager;
        this.regulator = regulator;

        connectedLocations.add(regulator);
        nodeQueue.add(regulator.clone());
    }

    /**
     * This method returns the range of the {@link Network}.
     * The range determines how far the {@link Network} will search for
     * nearby nodes from any given node.
     * 
     * It basically translates to the maximum distance between nodes.
     * 
     * @return the range of this {@link Network}
     */
    public abstract int getRange();

    /**
     * This method assigns the given {@link Location} a type of {@link NetworkComponent}
     * for classification.
     * 
     * @param l
     *            The {@link Location} to classify
     * @return The assigned type of {@link NetworkComponent} for this {@link Location}
     */
    @Nullable
    public abstract NetworkComponent classifyLocation(@Nonnull Location l);

    /**
     * This method is called whenever a {@link Location} in this {@link Network} changes
     * its classification.
     * 
     * @param l
     *            The {@link Location} that is changing its classification
     * @param from
     *            The {@link NetworkComponent} this {@link Location} was previously classified as
     * @param to
     *            The {@link NetworkComponent} this {@link Location} is changing to
     */
    public abstract void onClassificationChange(Location l, NetworkComponent from, NetworkComponent to);

    /**
     * This returns the size of this {@link Network}. It is equivalent to the amount
     * of {@link Location Locations} connected to this {@link Network}.
     * 
     * @return The size of this {@link Network}
     */
    public int getSize() {
        return regulatorNodes.size() + connectorNodes.size() + terminusNodes.size();
    }

    protected void addLocationToNetwork(@Nonnull Location l) {
        if (connectedLocations.contains(l)) {
            return;
        }

        connectedLocations.add(l.clone());
        markDirty(l);
    }

    /**
     * This method marks the given {@link Location} as dirty and adds it to a {@link Queue}
     * to handle this update.
     * 
     * @param l
     *            The {@link Location} to update
     */
    public void markDirty(@Nonnull Location l) {
        if (regulator.equals(l)) {
            manager.unregisterNetwork(this);
        } else {
            nodeQueue.add(l.clone());
        }
    }

    /**
     * This method checks whether the given {@link Location} is part of this {@link Network}.
     * 
     * @param l
     *            The {@link Location} to check for
     * @return Whether the given {@link Location} is part of this {@link Network}
     */
    public boolean connectsTo(@Nonnull Location l) {
        return connectedLocations.contains(l);
    }

    @Nullable
    private NetworkComponent getCurrentClassification(@Nonnull Location l) {
        if (regulatorNodes.contains(l)) {
            return NetworkComponent.REGULATOR;
        } else if (connectorNodes.contains(l)) {
            return NetworkComponent.CONNECTOR;
        } else if (terminusNodes.contains(l)) {
            return NetworkComponent.TERMINUS;
        }

        return null;
    }

    private void discoverStep() {
        int maxSteps = manager.getMaxSize();
        int steps = 0;

        while (nodeQueue.peek() != null) {
            Location l = nodeQueue.poll();
            NetworkComponent currentAssignment = getCurrentClassification(l);
            NetworkComponent classification = classifyLocation(l);

            if (classification != currentAssignment) {
                if (currentAssignment == NetworkComponent.REGULATOR || currentAssignment == NetworkComponent.CONNECTOR) {
                    // Requires a complete rebuild of the network, so we just throw the current one away.
                    manager.unregisterNetwork(this);
                    return;
                } else if (currentAssignment == NetworkComponent.TERMINUS) {
                    terminusNodes.remove(l);
                }

                if (classification == NetworkComponent.REGULATOR) {
                    regulatorNodes.add(l);
                    discoverNeighbors(l);
                } else if (classification == NetworkComponent.CONNECTOR) {
                    connectorNodes.add(l);
                    discoverNeighbors(l);
                } else if (classification == NetworkComponent.TERMINUS) {
                    terminusNodes.add(l);
                }

                onClassificationChange(l, currentAssignment, classification);
            }

            steps += 1;

            if (steps >= maxSteps) {
                break;
            }
        }
    }

    private void discoverNeighbors(@Nonnull Location l, double xDiff, double yDiff, double zDiff) {
        for (int i = getRange() + 1; i > 0; i--) {
            Location newLocation = l.clone().add(i * xDiff, i * yDiff, i * zDiff);
            addLocationToNetwork(newLocation);
        }
    }

    private void discoverNeighbors(@Nonnull Location l) {
        discoverNeighbors(l, 1.0, 0.0, 0.0);
        discoverNeighbors(l, -1.0, 0.0, 0.0);
        discoverNeighbors(l, 0.0, 1.0, 0.0);
        discoverNeighbors(l, 0.0, -1.0, 0.0);
        discoverNeighbors(l, 0.0, 0.0, 1.0);
        discoverNeighbors(l, 0.0, 0.0, -1.0);
    }

    /**
     * This method runs the network visualizer which displays a {@link Particle} on
     * every {@link Location} that this {@link Network} is connected to.
     */
    public void display() {
        SlimefunPlugin.runSync(() -> {
            DustOptions options = new DustOptions(Color.BLUE, 3F);

            for (Location l : connectedLocations) {
                Material type = l.getBlock().getType();

                if (type == Material.PLAYER_HEAD || type == Material.PLAYER_WALL_HEAD) {
                    l.getWorld().spawnParticle(Particle.REDSTONE, l.getX() + 0.5, l.getY() + 0.5, l.getZ() + 0.5, 1, 0, 0, 0, 1, options);
                }
            }
        });
    }

    /**
     * This returns the {@link Location} of the regulator block for this {@link Network}
     * 
     * @return The {@link Location} of our regulator
     */
    @Nonnull
    public Location getRegulator() {
        return regulator;
    }

    public void tick() {
        discoverStep();
    }
}
