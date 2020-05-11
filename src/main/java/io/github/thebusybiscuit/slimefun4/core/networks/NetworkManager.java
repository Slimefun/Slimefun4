package io.github.thebusybiscuit.slimefun4.core.networks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Server;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.api.network.Network;
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
    private final List<Network> networks = new LinkedList<>();

    /**
     * This creates a new {@link NetworkManager} with the given capacity.
     * 
     * @param maxStepSize
     *            The maximum amount of nodes a {@link Network} can have
     */
    public NetworkManager(int maxStepSize) {
        maxNodes = maxStepSize;
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
     * This returns a {@link List} of every {@link Network} on the {@link Server}.
     * 
     * @return A {@link List} containing every {@link Network} on the {@link Server}
     */
    public List<Network> getNetworkList() {
        return networks;
    }

    public <T extends Network> Optional<T> getNetworkFromLocation(Location l, Class<T> type) {
        for (Network network : networks) {
            if (type.isInstance(network) && network.connectsTo(l)) {
                return Optional.of(type.cast(network));
            }
        }

        return Optional.empty();
    }

    public <T extends Network> List<T> getNetworksFromLocation(Location l, Class<T> type) {
        List<T> list = new ArrayList<>();

        for (Network network : networks) {
            if (type.isInstance(network) && network.connectsTo(l)) {
                list.add(type.cast(network));
            }
        }

        return list;
    }

    public void registerNetwork(Network n) {
        networks.add(n);
    }

    public void unregisterNetwork(Network n) {
        networks.remove(n);
    }

    public void handleAllNetworkLocationUpdate(Location l) {
        for (Network n : getNetworksFromLocation(l, Network.class)) {
            n.markDirty(l);
        }
    }

}
