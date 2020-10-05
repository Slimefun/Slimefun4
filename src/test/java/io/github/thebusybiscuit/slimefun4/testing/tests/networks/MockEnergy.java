package io.github.thebusybiscuit.slimefun4.testing.tests.networks;

import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import org.bukkit.Location;

public class MockEnergy extends EnergyNet {
    protected MockEnergy(Location l) {
        super(l);
    }
}
