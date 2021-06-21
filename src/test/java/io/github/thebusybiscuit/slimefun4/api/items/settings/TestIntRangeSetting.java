package io.github.thebusybiscuit.slimefun4.api.items.settings;

import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

import be.seeseemelk.mockbukkit.MockBukkit;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

class TestIntRangeSetting {

    private static SlimefunPlugin plugin;
    private final int min = 0;
    private final int max = 100;

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
    @DisplayName("Test Constructor validation")
    void testConstructorValidation() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "INT_RANGE_TEST_00", new CustomItem(Material.DIAMOND, "&cTest"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new IntRangeSetting(item, "test", min, -50, max));
    }

    @Test
    @DisplayName("Test min and max getters")
    void testMinMaxGetters() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "INT_RANGE_TEST_0", new CustomItem(Material.DIAMOND, "&cTest"));
        IntRangeSetting setting = new IntRangeSetting(item, "test", min, 1, max);

        Assertions.assertEquals(min, setting.getMinimum());
        Assertions.assertEquals(max, setting.getMaximum());
    }

    @Test
    @DisplayName("Test illegal values")
    void testIllegalValues() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "INT_RANGE_TEST", new CustomItem(Material.DIAMOND, "&cTest"));
        IntRangeSetting setting = new IntRangeSetting(item, "test", min, 1, max);

        item.addItemSetting(setting);
        item.register(plugin);

        Assertions.assertThrows(IllegalArgumentException.class, () -> setting.update(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> setting.update(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> setting.update(101));
    }

    @Test
    @DisplayName("Test allowed value")
    void testAllowedValue() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "INT_RANGE_TEST_2", new CustomItem(Material.DIAMOND, "&cTest"));
        IntRangeSetting setting = new IntRangeSetting(item, "test", min, 1, max);

        item.addItemSetting(setting);
        item.register(plugin);

        Assertions.assertEquals(1, setting.getValue());
        setting.update(42);
        Assertions.assertEquals(42, setting.getValue());
    }
}
