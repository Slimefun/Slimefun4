package io.github.thebusybiscuit.slimefun4.tests.items;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.mocks.MockItemHandler;
import io.github.thebusybiscuit.slimefun4.mocks.SlimefunMocks;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;

public class TestItemHandlers {

    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testIllegalItemHandlers() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("ITEM_HANDLER_TEST", new CustomItem(Material.DIAMOND, "&cTest"));
        item.register(plugin);

        Assertions.assertThrows(IllegalArgumentException.class, () -> item.addItemHandler());
        Assertions.assertThrows(IllegalArgumentException.class, () -> item.addItemHandler((MockItemHandler) null));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> item.addItemHandler(new MockItemHandler()));
    }

    @Test
    public void testItemHandler() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("ITEM_HANDLER_TEST_2", new CustomItem(Material.DIAMOND, "&cTest"));

        MockItemHandler handler = new MockItemHandler();
        item.addItemHandler(handler);

        item.register(plugin);

        Assertions.assertTrue(item.getHandlers().contains(handler));

        AtomicBoolean bool = new AtomicBoolean(false);
        Assertions.assertTrue(item.callItemHandler(MockItemHandler.class, x -> bool.set(true)));
        Assertions.assertTrue(bool.get());

        AtomicBoolean bool2 = new AtomicBoolean(false);
        Assertions.assertFalse(item.callItemHandler(ItemUseHandler.class, x -> bool2.set(true)));
        Assertions.assertFalse(bool2.get());
    }
}
