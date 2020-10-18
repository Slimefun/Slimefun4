package io.github.thebusybiscuit.slimefun4.testing.tests.settings;

import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.items.settings.DoubleRangeSetting;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DoubleRangeSetting("test", min, -1.0, max));
    }

    @Test
    @DisplayName("Test min and max getters")
    void testMinMaxGetters() {
        DoubleRangeSetting setting = new DoubleRangeSetting("test", min, 0.5, max);

        Assertions.assertEquals(min, setting.getMinimum());
        Assertions.assertEquals(max, setting.getMaximum());
    }

    @Test
    @DisplayName("Test illegal values")
    void testIllegalValues() {
        DoubleRangeSetting setting = new DoubleRangeSetting("test", min, 0.5, max);

        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "DOUBLE_RANGE_TEST", new CustomItem(Material.DIAMOND, "&cTest"));
        item.addItemSetting(setting);
        item.register(plugin);

        Assertions.assertThrows(IllegalArgumentException.class, () -> setting.update(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> setting.update(-0.1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> setting.update(1.1));
    }

    @Test
    @DisplayName("Test allowed value")
    void testAllowedValue() {
        DoubleRangeSetting setting = new DoubleRangeSetting("test", min, 0.25, max);

        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "DOUBLE_RANGE_TEST_2", new CustomItem(Material.DIAMOND, "&cTest"));
        item.addItemSetting(setting);
        item.register(plugin);

        Assertions.assertEquals(0.25, setting.getValue());
        setting.update(0.75);
        Assertions.assertEquals(0.75, setting.getValue());
    }
}
