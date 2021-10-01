package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.armor.SlimefunArmorTask;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

class TestArmorTask {

    private static ServerMock server;
    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test Slimefun Armor Effects")
    void testSlimefunArmor() throws InterruptedException {
        Player player = server.addPlayer();
        TestUtilities.awaitProfile(player);

        // Setting the time to noon, to exclude the Solar Helmet check
        player.getWorld().setTime(16000);

        PotionEffect[] effects = { new PotionEffect(PotionEffectType.SPEED, 50, 3), new PotionEffect(PotionEffectType.SATURATION, 128, 12) };

        SlimefunItemStack helmet = new SlimefunItemStack("HELMET_FLEX", Material.IRON_HELMET, "&bSuper cool Helmet");
        SlimefunArmorPiece armor = new SlimefunArmorPiece(TestUtilities.getItemGroup(plugin, "armor_test"), helmet, RecipeType.NULL, new ItemStack[9], effects);
        armor.register(plugin);

        player.getInventory().setHelmet(helmet.clone());
        player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        new SlimefunArmorTask().run();

        // Check if all Potion Effects were applied
        Assertions.assertTrue(player.getActivePotionEffects().containsAll(Arrays.asList(effects)));
    }
}
