package io.github.thebusybiscuit.slimefun4.core.networks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Server;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.NetworkListener;

/**
 * The {@link NetworkManager} is responsible for holding all instances of {@link Network}
 * and providing some utility methods that would have probably been static otherwise.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Network
 * @see NetworkListener
 *
 */
public class NetworkManager {

    private final int maxNodes;
    private final boolean enableVisualizer;
    private final boolean deleteExcessItems;
    private final List<Network> networks = new LinkedList<>();

    /**
     * This creates a new {@link NetworkManager} with the given capacity.
     * 
     * @param maxStepSize
     *            The maximum amount of nodes a {@link Network} can have
     * @param enableVisualizer
     *            Whether the {@link Network} visualizer is enabled
     * @param deleteExcessItems
     *            Whether excess items from a {@link CargoNet} should be voided
     */
    public NetworkManager(int maxStepSize, boolean enableVisualizer, boolean deleteExcessItems) {
        Validate.isTrue(maxStepSize > 0, "The maximal Network size must be above zero!");

        this.enableVisualizer = enableVisualizer;
        this.deleteExcessItems = deleteExcessItems;
        maxNodes = maxStepSize;
    }

    /**
     * This creates a new {@link NetworkManager} with the given capacity.
     * 
     * @param maxStepSize
     *            The maximum amount of nodes a {@link Network} can have
     */
    public NetworkManager(int maxStepSize) {
        this(maxStepSize, true, false);
    }

    /**
     * This method returns the limit of nodes a {@link Network} can have.
     * This value is read from the {@link Config} file.
     * 
     * @return the maximum amount of nodes a {@link Network} can have
     */
    public int getMaxSize() {
        return maxNodes;
    }

    /**
     * This returns whether the {@link Network} visualizer is enabled.
     * 
     * @return Whether the {@link Network} visualizer is enabled
     */
    public boolean isVisualizerEnabled() {
        return enableVisualizer;
    }

    /**
     * This returns whether excess items from a {@link CargoNet} should be voided
     * instead of being dropped to the ground.
     * 
     * @return Whether to delete excess items
     */
    public boolean isItemDeletionEnabled() {
        return deleteExcessItems;
    }

    /**
     * This returns a {@link List} of every {@link Network} on the {@link Server}.
     * 
     * @return A {@link List} containing every {@link Network} on the {@link Server}
     */
    @Nonnull
    public List<Network> getNetworkList() {
        return networks;
    }

    @Nonnull
    public <T extends Network> Optional<T> getNetworkFromLocation(@Nullable Location l, @Nonnull Class<T> type) {
        if (l == null) {
            return Optional.empty();
        }

        Validate.notNull(type, "Type must not be null");

        for (Network network : networks) {
            if (type.isInstance(network) && network.connectsTo(l)) {
                return Optional.of(type.cast(network));
            }
        }

        return Optional.empty();
    }

    @Nonnull
    public <T extends Network> List<T> getNetworksFromLocation(@Nullable Location l, @Nonnull Class<T> type) {
        if (l == null) {
            // No networks here, if the location does not even exist
            return new ArrayList<>();
        }

        Validate.notNull(type, "Type must not be null");
        List<T> list = new ArrayList<>();

        for (Network network : networks) {
            if (type.isInstance(network) && network.connectsTo(l)) {
                list.add(type.cast(network));
            }
        }

        return list;
    }

    /**
     * This registers a given {@link Network}.
     * 
     * @param network
     *            The {@link Network} to register
     */
    public void registerNetwork(@Nonnull Network network) {
        Validate.notNull(network, "Cannot register a null Network");
        networks.add(network);
    }

    /**
     * This removes a {@link Network} from the network system.
     * 
     * @param network
     *            The {@link Network} to remove
     */
    public void unregisterNetwork(@Nonnull Network network) {
        Validate.notNull(network, "Cannot unregister a null Network");
        networks.remove(network);
    }

    /**
     * This method updates every {@link Network} found at the given {@link Location}.
     * More precisely, {@link Network#markDirty(Location)} will be called.
     * 
     * @param l
     *            The {@link Location} to update
     */
    public void updateAllNetworks(@Nonnull Location l) {
        Validate.notNull(l, "The Location cannot be null");

        // No need to create a sublist and loop through it if there are no Networks
        if (!networks.isEmpty()) {
            for (Network network : getNetworksFromLocation(l, Network.class)) {
                network.markDirty(l);
            }
        }
    }

}
