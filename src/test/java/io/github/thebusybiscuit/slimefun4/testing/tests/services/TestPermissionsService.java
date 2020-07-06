package io.github.thebusybiscuit.slimefun4.testing.tests.services;

import java.util.Arrays;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.services.PermissionsService;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class TestPermissionsService {

    private static ServerMock server;
    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testRegistration() {
        PermissionsService service = new PermissionsService(plugin);
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "PERMISSIONS_TEST", new CustomItem(Material.EMERALD, "&bBad omen"));
        service.register(Arrays.asList(item), false);

        Optional<String> permission = service.getPermission(item);
        Assertions.assertFalse(permission.isPresent());
    }

    @Test
    public void testSetPermission() {
        PermissionsService service = new PermissionsService(plugin);
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "PERMISSIONS_TEST", new CustomItem(Material.EMERALD, "&bBad omen"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.setPermission(null, null));

        service.setPermission(item, "slimefun.tests");

        Optional<String> permission = service.getPermission(item);
        Assertions.assertTrue(permission.isPresent());
        Assertions.assertEquals("slimefun.tests", permission.get());

        service.setPermission(item, null);
        Assertions.assertFalse(service.getPermission(item).isPresent());

        service.setPermission(item, "none");
        Assertions.assertFalse(service.getPermission(item).isPresent());
    }

    @Test
    public void testHasPermissionTrue() {
        PermissionsService service = new PermissionsService(plugin);
        Player player = server.addPlayer();
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "PERMISSIONS_TEST", new CustomItem(Material.EMERALD, "&bBad omen"));

        Assertions.assertTrue(service.hasPermission(player, null));
        Assertions.assertTrue(service.hasPermission(player, item));

        service.setPermission(item, "none");
        Assertions.assertTrue(service.hasPermission(player, item));
    }

    @Test
    public void testHasPermissionFalse() {
        PermissionsService service = new PermissionsService(plugin);
        Player player = server.addPlayer();
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "PERMISSIONS_TEST", new CustomItem(Material.EMERALD, "&bBad omen"));

        service.setPermission(item, "slimefun.tests");
        Assertions.assertFalse(service.hasPermission(player, item));
    }
}
