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

class TestDoubleRangeSetting {

    private static SlimefunPlugin plugin;
    private final double min = 0.0;
    private final double max = 1.0;

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
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "DOUBLE_RANGE_TEST_00", new CustomItem(Material.DIAMOND, "&cTest"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DoubleRangeSetting(item, "test", min, -1.0, max));
    }

    @Test
    @DisplayName("Test min and max getters")
    void testMinMaxGetters() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "DOUBLE_RANGE_TEST_0", new CustomItem(Material.DIAMOND, "&cTest"));
        DoubleRangeSetting setting = new DoubleRangeSetting(item, "test", min, 0.5, max);

        Assertions.assertEquals(min, setting.getMinimum());
        Assertions.assertEquals(max, setting.getMaximum());
    }

    @Test
    @DisplayName("Test illegal values")
    void testIllegalValues() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "DOUBLE_RANGE_TEST", new CustomItem(Material.DIAMOND, "&cTest"));
        DoubleRangeSetting setting = new DoubleRangeSetting(item, "test", min, 0.5, max);

        item.addItemSetting(setting);
        item.register(plugin);

        Assertions.assertThrows(IllegalArgumentException.class, () -> setting.update(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> setting.update(-0.1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> setting.update(1.1));
    }

    @Test
    @DisplayName("Test allowed value")
    void testAllowedValue() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "DOUBLE_RANGE_TEST_2", new CustomItem(Material.DIAMOND, "&cTest"));
        DoubleRangeSetting setting = new DoubleRangeSetting(item, "test", min, 0.25, max);

        item.addItemSetting(setting);
        item.register(plugin);

        Assertions.assertEquals(0.25, setting.getValue());
        setting.update(0.75);
        Assertions.assertEquals(0.75, setting.getValue());
    }
}
