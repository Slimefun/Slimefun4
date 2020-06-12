package io.github.thebusybiscuit.slimefun4.testing.tests.items.implementations.food;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.MagicSugar;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import io.github.thebusybiscuit.slimefun4.testing.interfaces.SlimefunItemTest;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class TestMagicSugar implements SlimefunItemTest<MagicSugar> {

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
    public MagicSugar registerSlimefunItem(SlimefunPlugin plugin, String id) {
        SlimefunItemStack item = new SlimefunItemStack(id, Material.SUGAR, "&5Test Magic Sugar");
        MagicSugar sugar = new MagicSugar(TestUtilities.getCategory(plugin, "magic_sugar"), item, RecipeType.NULL, new ItemStack[9]);
        sugar.register(plugin);
        return sugar;
    }

    @Test
    public void testRightClickBehaviour() {
        PlayerMock player = server.addPlayer();
        MagicSugar sugar = registerSlimefunItem(plugin, "TEST_MAGIC_SUGAR");

        simulateRightClick(player, sugar);

        player.assertSoundHeard(Sound.ENTITY_GENERIC_EAT);
        Assertions.assertTrue(player.hasPotionEffect(PotionEffectType.SPEED));
    }

}
