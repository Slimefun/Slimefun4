package io.github.thebusybiscuit.slimefun4.testing.tests.items.food;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.MeatJerky;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import io.github.thebusybiscuit.slimefun4.testing.interfaces.SlimefunItemTest;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

class TestMeatJerky implements SlimefunItemTest<MeatJerky> {

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

    @Override
    public MeatJerky registerSlimefunItem(SlimefunPlugin plugin, String id) {
        SlimefunItemStack item = new SlimefunItemStack(id, Material.COOKED_BEEF, "&5Test Jerky");
        MeatJerky meat = new MeatJerky(TestUtilities.getCategory(plugin, "test_jerky"), item, RecipeType.NULL, new ItemStack[9]);
        meat.register(plugin);
        return meat;
    }

    @Test
    @DisplayName("Test Meat Jerky giving extra saturation")
    void testConsumptionBehaviour() {
        PlayerMock player = server.addPlayer();
        MeatJerky jerky = registerSlimefunItem(plugin, "TEST_MEAT_JERKY");
        float saturation = player.getSaturation();

        simulateConsumption(player, jerky);

        // Saturation should have increased
        Assertions.assertTrue(player.getSaturation() > saturation);
    }

}
