package io.github.thebusybiscuit.slimefun4.testing.tests.services;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.core.services.CustomTextureService;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

class TextCustomTextureService {

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
    @DisplayName("Test creating a new Custom Texture Service")
    void testInitialization() {
        Config config = new Config("plugins/temporary");
        CustomTextureService service = new CustomTextureService(config);
        Assertions.assertFalse(service.isActive());
        Assertions.assertNull(service.getVersion());

        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "TEXTURE_TEST", new ItemStack(Material.LANTERN));
        service.register(Arrays.asList(null, item, null), false);

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.register(null, false));

        // These values should not have changed yet
        Assertions.assertFalse(service.isActive());
        Assertions.assertNull(service.getVersion());

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getModelData(null));
    }

    @Test
    @DisplayName("Test applying a custom item model")
    void testSetTexture() {
        Config config = new Config("plugins/temporary");
        CustomTextureService service = new CustomTextureService(config);
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "TEXTURE_TEST", new ItemStack(Material.LANTERN));
        String version = "Unit Test v1.0";

        config.setValue(item.getId(), 300);
        config.setValue("version", version);

        service.register(Arrays.asList(item), false);

        Assertions.assertTrue(service.isActive());
        Assertions.assertEquals(version, service.getVersion());
        Assertions.assertEquals(300, service.getModelData(item.getId()));

        ItemStack stack = new ItemStack(Material.DIAMOND);
        service.setTexture(stack, item.getId());

        Assertions.assertTrue(stack.getItemMeta().hasCustomModelData());
        Assertions.assertEquals(300, stack.getItemMeta().getCustomModelData());
    }
}
