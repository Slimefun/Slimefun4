package io.github.thebusybiscuit.slimefun4.api.network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;

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
public final class NetworkManager {

    private final int maxNodes;
    private final List<Network> networks = new LinkedList<>();

    public NetworkManager(int capacity) {
        maxNodes = capacity;
    }

    public int getMaxSize() {
        return maxNodes;
    }

    public List<Network> getNetworkList() {
        return networks;
    }

    public <T extends Network> T getNetworkFromLocation(Location l, Class<T> type) {
        for (Network network : networks) {
            if (type.isInstance(network) && network.connectsTo(l)) {
                return type.cast(network);
            }
        }

        return null;
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
            n.handleLocationUpdate(l);
        }
    }

}
