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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        energyNet = new MockEnergy(location);
    }

    @AfterEach
    public void unload() {
        MockBukkit.unmock();
    }

    @DisplayName("it will display different component")
    @Test
    void testClassifyLocation() {
        NetworkComponent actual = energyNet.classifyLocation(location);

        Assertions.assertEquals(NetworkComponent.REGULATOR, actual);

        location = new Location(world, 0, 101, 0);
        Assertions.assertNull(energyNet.classifyLocation(location));
    }

    /**
     * This test should aim to have location instance of EnergyNetComponent
     */
    @DisplayName("it will display a location but the location should be an energynetcomponent")
    @Test
    void testClassifyLocation2() {

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
        Assertions.assertNull(actual);

    }

}