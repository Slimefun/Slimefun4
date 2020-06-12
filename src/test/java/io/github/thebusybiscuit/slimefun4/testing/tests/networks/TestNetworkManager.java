package io.github.thebusybiscuit.slimefun4.testing.tests.networks;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.api.network.NetworkComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.NetworkManager;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;

public class TestNetworkManager {

    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testIllegalNetworkSize() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new NetworkManager(-100));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new NetworkManager(0));
    }

    @Test
    public void testGetMaxNetworkSize() {
        int size = 50;
        NetworkManager manager = new NetworkManager(size);

        Assertions.assertEquals(size, manager.getMaxSize());
    }

    @Test
    public void testGetNetworkList() {
        NetworkManager manager = new NetworkManager(10);
        World world = server.addSimpleWorld("Simple Network World");
        Location loc = new Location(world, 0, 100, 0);

        Network network = new DummyNetwork(manager, loc, 10, new HashMap<>());

        Assertions.assertFalse(manager.getNetworkList().contains(network));
        manager.registerNetwork(network);
        Assertions.assertTrue(manager.getNetworkList().contains(network));
        manager.unregisterNetwork(network);
        Assertions.assertFalse(manager.getNetworkList().contains(network));
    }

    @Test
    public void testDirtyRegulatorUnregistersNetwork() {
        World world = server.addSimpleWorld("Simple Network World");
        Location loc = new Location(world, 0, 100, 0);

        NetworkManager manager = Mockito.mock(NetworkManager.class);
        Network network = new DummyNetwork(manager, loc, 10, new HashMap<>());
        network.markDirty(loc);

        Mockito.verify(manager).unregisterNetwork(network);
    }

    @Test
    public void testGetNetworkAtLocation() {
        NetworkManager manager = new NetworkManager(10);
        World world = server.addSimpleWorld("Simple Network World");
        Location loc = new Location(world, 0, 100, 0);
        Location loc2 = new Location(world, 0, 200, 0);

        Network network = new DummyNetwork(manager, loc, 10, new HashMap<>());

        Assertions.assertFalse(manager.getNetworkFromLocation(loc, DummyNetwork.class).isPresent());

        manager.registerNetwork(network);

        Optional<DummyNetwork> optional = manager.getNetworkFromLocation(loc, DummyNetwork.class);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(network, optional.get());

        Assertions.assertFalse(manager.getNetworkFromLocation(loc2, DummyNetwork.class).isPresent());
        Assertions.assertFalse(manager.getNetworkFromLocation(loc, CargoNet.class).isPresent());
    }

    @Test
    public void testGetNetworksAtLocation() {
        NetworkManager manager = new NetworkManager(10);
        World world = server.addSimpleWorld("Simple Network World");
        Location loc = new Location(world, 0, 100, 0);
        Location loc2 = new Location(world, 0, 200, 0);

        Network network = new DummyNetwork(manager, loc, 10, new HashMap<>());
        manager.registerNetwork(network);

        Assertions.assertFalse(manager.getNetworksFromLocation(loc2, DummyNetwork.class).contains(network));
        Assertions.assertFalse(manager.getNetworksFromLocation(loc, CargoNet.class).contains(network));
        Assertions.assertTrue(manager.getNetworksFromLocation(loc, DummyNetwork.class).contains(network));
    }

    @Test
    public void testSingleNodeNetwork() {
        NetworkManager manager = new NetworkManager(1);
        World world = server.addSimpleWorld("Simple Network World");
        Location loc = new Location(world, 0, 100, 0);

        Network network = new DummyNetwork(manager, loc, 1, new HashMap<>());
        network.tick();

        Assertions.assertEquals(1, network.getSize());
        Assertions.assertEquals(NetworkComponent.REGULATOR, network.classifyLocation(loc));
    }

    @Test
    public void testCornerConnection() {
        NetworkManager manager = new NetworkManager(100);
        World world = server.addSimpleWorld("Simple Network World");
        Map<Location, NetworkComponent> map = new HashMap<>();

        Location loc = new Location(world, 0, 100, 0);

        Location loc2 = new Location(world, 0, 100, 2);
        map.put(loc2, NetworkComponent.CONNECTOR);

        Location loc3 = new Location(world, 2, 100, 2);
        map.put(loc3, NetworkComponent.CONNECTOR);

        Network network = new DummyNetwork(manager, loc, 3, map);
        network.tick();

        Assertions.assertEquals(3, network.getSize());
    }

    private class DummyNetwork extends Network {

        private final int range;
        private final Map<Location, NetworkComponent> locations;

        protected DummyNetwork(NetworkManager manager, Location regulator, int range, Map<Location, NetworkComponent> locations) {
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
            if (l.equals(regulator)) return NetworkComponent.REGULATOR;
            return locations.get(l);
        }

        @Override
        public void onClassificationChange(Location l, NetworkComponent from, NetworkComponent to) {
            // Do nothing
        }

    }

}
