package io.github.thebusybiscuit.slimefun4.testing.tests.tasks;

import java.util.Arrays;
import java.util.Locale;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.RadioactiveItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.ArmorTask;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

class TestArmorTask {

    private static ServerMock server;
    private static SlimefunPlugin plugin;
    private static ArmorTask task;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        task = new ArmorTask(false);
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
        SlimefunArmorPiece armor = new SlimefunArmorPiece(TestUtilities.getCategory(plugin, "armor_test"), helmet, RecipeType.NULL, new ItemStack[9], effects);
        armor.register(plugin);

        player.getInventory().setHelmet(helmet.clone());
        player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        task.run();

        // Check if all Potion Effects were applied
        Assertions.assertTrue(player.getActivePotionEffects().containsAll(Arrays.asList(effects)));
    }

    @ParameterizedTest
    @DisplayName("Test Radiation and Hazmat Suits")
    @ValueSource(booleans = { true, false })
    void testRadiactivity(boolean hazmat) throws InterruptedException {
        Player player = server.addPlayer();
        TestUtilities.awaitProfile(player);

        // Setting the time to noon, to exclude the Solar Helmet check
        player.getWorld().setTime(16000);

        Category category = TestUtilities.getCategory(plugin, "hazmat_suit_test");
        SlimefunItemStack item = new SlimefunItemStack("MOCK_URANIUM_" + String.valueOf(hazmat).toUpperCase(Locale.ROOT), Material.EMERALD, "&aHi, I am deadly");
        new RadioactiveItem(category, Radioactivity.VERY_DEADLY, item, RecipeType.NULL, new ItemStack[9]).register(plugin);

        player.getInventory().setItemInMainHand(item.clone());
        player.getInventory().setItemInOffHand(new ItemStack(Material.EMERALD_ORE));

        if (hazmat) {
            SlimefunItemStack chestplate = new SlimefunItemStack("MOCK_HAZMAT_SUIT", Material.LEATHER_CHESTPLATE, "&4Hazmat Prototype");
            MockHazmatSuit armor = new MockHazmatSuit(category, chestplate);
            armor.register(plugin);

            player.getInventory().setChestplate(chestplate.clone());
        }

        task.run();

        // Check if the Player is suffering from radiation
        boolean radiation = player.getActivePotionEffects().containsAll(task.getRadiationEffects());
        Assertions.assertEquals(!hazmat, radiation);
    }

}
