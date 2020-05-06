package io.github.thebusybiscuit.slimefun4.tests.items;

import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.mocks.SlimefunMocks;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ItemState;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class TestSlimefunItemRegistration {

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
    public void testSuccessfulRegistration() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("TEST_ITEM", new CustomItem(Material.DIAMOND, "&cTest"));
        item.register(plugin);

        Assertions.assertEquals(ItemState.ENABLED, item.getState());
    }

    @Test
    public void testVanillaItemFallback() {
        VanillaItem item = SlimefunMocks.mockVanillaItem(Material.ACACIA_SIGN, false);
        item.register(plugin);

        Assertions.assertEquals(ItemState.VANILLA_FALLBACK, item.getState());
    }

    @Test
    public void testIdConflict() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("DUPLICATE_ID", new CustomItem(Material.DIAMOND, "&cTest"));
        item.register(plugin);

        SlimefunItem item2 = SlimefunMocks.mockSlimefunItem("DUPLICATE_ID", new CustomItem(Material.DIAMOND, "&cTest"));
        item2.register(plugin);

        Assertions.assertEquals(ItemState.ENABLED, item.getState());
        Assertions.assertEquals(ItemState.UNREGISTERED, item2.getState());
    }

}
