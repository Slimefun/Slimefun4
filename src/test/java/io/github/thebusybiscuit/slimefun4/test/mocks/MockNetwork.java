package io.github.thebusybiscuit.slimefun4.test.mocks;

import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;

import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.api.network.NetworkComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.NetworkManager;

public class MockNetwork extends Network {

    private final int range;
    private final Map<Location, NetworkComponent> locations;

    @ParametersAreNonnullByDefault
    public MockNetwork(NetworkManager manager, Location regulator, int range, Map<Location, NetworkComponent> locations) {
        super(manager, regulator);
        this.range = range;
        this.locations = locations;
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public NetworkComponent classifyLocation(Location l) {
        if (l.equals(regulator)) {
            return NetworkComponent.REGULATOR;
        } else {
            return locations.get(l);
        }
    }

    @Override
    public void onClassificationChange(Location l, NetworkComponent from, NetworkComponent to) {
        // Do nothing
    }

}