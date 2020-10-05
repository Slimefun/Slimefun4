package io.github.thebusybiscuit.slimefun4.testing.tests.networks;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.network.NetworkComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EnergyNetTest {

    private EnergyNet energyNet;
    private SlimefunPlugin plugin;
    private ServerMock server;
    private Location location;
    private World world;

    @BeforeEach
    public void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        world = server.addSimpleWorld("Simple Network World");
        location = new Location(world, 0, 100, 0);
        energyNet = new EnergyNet(location);
    }

    @AfterEach
    public void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testGetRange() {
        assertEquals(6, energyNet.getRange());
    }

    @DisplayName("it will display different component")
    @Test
    void testClassifyLocation() {
        NetworkComponent actual = energyNet.classifyLocation(location);

        assertEquals(NetworkComponent.REGULATOR, actual);

        location = new Location(world, 0, 101, 0);
        assertNull(energyNet.classifyLocation(location));
    }

    @Test
    void testEnergyComponent() {

        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "PERMISSIONS_TEST", new CustomItem(Material.EMERALD, "&bBad omen"));
/*        try (MockedStatic<BlockStorage> theMock = Mockito.mockStatic(BlockStorage.class)) {
            theMock.when(() -> {
                BlockStorage.check(any(Location.class));
            }).thenReturn(item);


            world = server.addSimpleWorld("Simple Network World");
            location = new Location(world, 0, 105, 0);
            NetworkComponent actual = energyNet.classifyLocation(location);
            assertNull(actual);
        }*/

        world = server.addSimpleWorld("Simple Network World");
        location = new Location(world, 0, 105, 0);
        NetworkComponent actual = energyNet.classifyLocation(location);
        assertNull(actual);

    }


}