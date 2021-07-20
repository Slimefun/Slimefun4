package io.github.thebusybiscuit.slimefun4.core.networks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.api.network.NetworkComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;
import io.github.thebusybiscuit.slimefun4.test.mocks.MockNetwork;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

class TestNetworkManager {

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
    @DisplayName("Test illegal network size arguments")
    void testIllegalNetworkSize() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new NetworkManager(-100));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new NetworkManager(0));
    }

    @Test
    @DisplayName("Test maximum network size")
    void testGetMaxNetworkSize() {
        int size = 50;
        NetworkManager manager = new NetworkManager(size);

        Assertions.assertEquals(size, manager.getMaxSize());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @DisplayName("Test visualizer setting")
    void testVisualizerSetting(boolean enabled) {
        NetworkManager manager = new NetworkManager(200, enabled, false);

        Assertions.assertEquals(enabled, manager.isVisualizerEnabled());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @DisplayName("Test item deletion setting")
    void testItemDeletionSetting(boolean enabled) {
        NetworkManager manager = new NetworkManager(200, true, enabled);

        Assertions.assertEquals(enabled, manager.isItemDeletionEnabled());
    }

    @Test
    @DisplayName("Test network list")
    void testGetNetworkList() {
        NetworkManager manager = new NetworkManager(10);
        World world = server.addSimpleWorld("Simple Network World");
        Location loc = new Location(world, 0, 100, 0);

        Network network = new MockNetwork(manager, loc, 10, new HashMap<>());

        Assertions.assertFalse(manager.getNetworkList().contains(network));
        manager.registerNetwork(network);
        Assertions.assertTrue(manager.getNetworkList().contains(network));
        manager.unregisterNetwork(network);
        Assertions.assertFalse(manager.getNetworkList().contains(network));
    }

    @Test
    @DisplayName("Test network re-registration for dirty regulators")
    void testDirtyRegulatorUnregistersNetwork() {
        World world = server.addSimpleWorld("Simple Network World");
        Location loc = new Location(world, 0, 100, 0);

        NetworkManager manager = Mockito.mock(NetworkManager.class);
        Network network = new MockNetwork(manager, loc, 10, new HashMap<>());
        network.markDirty(loc);

        Mockito.verify(manager).unregisterNetwork(network);
    }

    @Test
    @DisplayName("Test getting a network at a location")
    void testGetNetworkAtLocation() {
        NetworkManager manager = new NetworkManager(10);
        World world = server.addSimpleWorld("Simple Network World");
        Location loc = new Location(world, 0, 100, 0);
        Location loc2 = new Location(world, 0, 200, 0);

        Network network = new MockNetwork(manager, loc, 10, new HashMap<>());

        Assertions.assertFalse(manager.getNetworkFromLocation(loc, MockNetwork.class).isPresent());

        manager.registerNetwork(network);

        Optional<MockNetwork> optional = manager.getNetworkFromLocation(loc, MockNetwork.class);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(network, optional.get());

        Assertions.assertFalse(manager.getNetworkFromLocation(loc2, MockNetwork.class).isPresent());
        Assertions.assertFalse(manager.getNetworkFromLocation(loc, CargoNet.class).isPresent());
    }

    @Test
    @DisplayName("Test getting all networks at a location")
    void testGetNetworksAtLocation() {
        NetworkManager manager = new NetworkManager(10);
        World world = server.addSimpleWorld("Simple Network World");
        Location loc = new Location(world, 0, 100, 0);
        Location loc2 = new Location(world, 0, 200, 0);

        Network network = new MockNetwork(manager, loc, 10, new HashMap<>());
        manager.registerNetwork(network);

        Assertions.assertFalse(manager.getNetworksFromLocation(loc2, MockNetwork.class).contains(network));
        Assertions.assertFalse(manager.getNetworksFromLocation(loc, CargoNet.class).contains(network));
        Assertions.assertTrue(manager.getNetworksFromLocation(loc, MockNetwork.class).contains(network));
    }

    @Test
    @DisplayName("Test a single node network")
    void testSingleNodeNetwork() {
        NetworkManager manager = new NetworkManager(1);
        World world = server.addSimpleWorld("Simple Network World");
        Location loc = new Location(world, 0, 100, 0);

        Network network = new MockNetwork(manager, loc, 1, new HashMap<>());
        network.tick();

        Assertions.assertEquals(1, network.getSize());
        Assertions.assertEquals(NetworkComponent.REGULATOR, network.classifyLocation(loc));
    }

    @Test
    @DisplayName("Test networks connecting via corners")
    void testCornerConnection() {
        NetworkManager manager = new NetworkManager(100);
        World world = server.addSimpleWorld("Simple Network World");
        Map<Location, NetworkComponent> map = new HashMap<>();

        Location loc = new Location(world, 0, 100, 0);

        Location loc2 = new Location(world, 0, 100, 2);
        map.put(loc2, NetworkComponent.CONNECTOR);

        Location loc3 = new Location(world, 2, 100, 2);
        map.put(loc3, NetworkComponent.CONNECTOR);

        Network network = new MockNetwork(manager, loc, 3, map);
        network.tick();

        Assertions.assertEquals(3, network.getSize());
    }

    @Test
    @DisplayName("Test empty network list for null locations")
    void testNullLocations() {
        NetworkManager manager = new NetworkManager(10, false, false);

        Optional<Network> optional = manager.getNetworkFromLocation(null, Network.class);
        Assertions.assertNotNull(optional);
        Assertions.assertFalse(optional.isPresent());

        List<Network> list = manager.getNetworksFromLocation(null, Network.class);
        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.isEmpty());
    }

}
