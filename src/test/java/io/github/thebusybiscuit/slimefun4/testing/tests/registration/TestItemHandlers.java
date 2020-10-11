package io.github.thebusybiscuit.slimefun4.testing.tests.registration;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.exceptions.IncompatibleItemHandlerException;
import io.github.thebusybiscuit.slimefun4.core.handlers.BowShootHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import io.github.thebusybiscuit.slimefun4.testing.mocks.MockItemHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

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
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "ITEM_HANDLER_TEST", new CustomItem(Material.DIAMOND, "&cTest"));
        item.register(plugin);

        Assertions.assertThrows(IllegalArgumentException.class, () -> item.addItemHandler());
        Assertions.assertThrows(IllegalArgumentException.class, () -> item.addItemHandler((MockItemHandler) null));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> item.addItemHandler(new MockItemHandler()));
    }

    @Test
    public void testItemHandler() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "ITEM_HANDLER_TEST_2", new CustomItem(Material.DIAMOND, "&cTest"));

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

    @Test
    public void testBowShootHandler() {
        BowShootHandler handler = (e, n) -> {};
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "NOT_A_BOW", new CustomItem(Material.KELP, "&bNot a bow!"));

        Optional<IncompatibleItemHandlerException> exception = handler.validate(item);
        Assertions.assertTrue(exception.isPresent());

        SlimefunItem bow = TestUtilities.mockSlimefunItem(plugin, "A_BOW", new CustomItem(Material.BOW, "&bA bow!"));
        Optional<IncompatibleItemHandlerException> exception2 = handler.validate(bow);
        Assertions.assertFalse(exception2.isPresent());
    }

    @Test
    public void testPublicHandler() {
        ItemHandler handler = new MockItemHandler() {

            @Override
            public boolean isPrivate() {
                return false;
            }
        };

        Assertions.assertFalse(handler.isPrivate());

        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "PUBLIC_HANDLER_TEST", new CustomItem(Material.KELP, "&bHappy kelp"));
        item.addItemHandler(handler);
        item.register(plugin);

        Map<Class<? extends ItemHandler>, Set<ItemHandler>> handlers = SlimefunPlugin.getRegistry().getPublicItemHandlers();
        Assertions.assertTrue(handlers.get(handler.getIdentifier()).contains(handler));
    }
}
