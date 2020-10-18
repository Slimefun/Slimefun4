package io.github.thebusybiscuit.slimefun4.testing.tests.settings;

import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> new IntRangeSetting("test", min, -50, max));
    }

    @Test
    @DisplayName("Test min and max getters")
    void testMinMaxGetters() {
        IntRangeSetting setting = new IntRangeSetting("test", min, 1, max);

        Assertions.assertEquals(min, setting.getMinimum());
        Assertions.assertEquals(max, setting.getMaximum());
    }

    @Test
    @DisplayName("Test illegal values")
    void testIllegalValues() {
        IntRangeSetting setting = new IntRangeSetting("test", min, 1, max);

        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "INT_RANGE_TEST", new CustomItem(Material.DIAMOND, "&cTest"));
        item.addItemSetting(setting);
        item.register(plugin);

        Assertions.assertThrows(IllegalArgumentException.class, () -> setting.update(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> setting.update(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> setting.update(101));
    }

    @Test
    @DisplayName("Test allowed value")
    void testAllowedValue() {
        IntRangeSetting setting = new IntRangeSetting("test", min, 1, max);

        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "INT_RANGE_TEST_2", new CustomItem(Material.DIAMOND, "&cTest"));
        item.addItemSetting(setting);
        item.register(plugin);

        Assertions.assertEquals(1, setting.getValue());
        setting.update(42);
        Assertions.assertEquals(42, setting.getValue());
    }
}
