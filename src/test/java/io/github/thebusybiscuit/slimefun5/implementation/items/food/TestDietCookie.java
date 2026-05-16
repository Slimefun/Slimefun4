package io.github.thebusybiscuit.slimefun5.implementation.items.food;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.test.TestUtilities;
import io.github.thebusybiscuit.slimefun5.test.presets.SlimefunItemTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockbukkit.mockbukkit.matcher.sound.SoundReceiverSoundHeardMatcher.hasHeard;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

class TestDietCookie implements SlimefunItemTest<DietCookie> {

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

    @Override
    public DietCookie registerSlimefunItem(Slimefun plugin, String id) {
        SlimefunItemStack item = new SlimefunItemStack(id, Material.COOKIE, "&5Test Cookie");
        DietCookie cookie = new DietCookie(TestUtilities.getItemGroup(plugin, "diet_cookie"), item, RecipeType.NULL, new ItemStack[9]);
        cookie.register(plugin);
        return cookie;
    }

    @Test
    @DisplayName("Test Diet Cookies giving Levitation Effect")
    void testConsumptionBehaviour() {
        PlayerMock player = server.addPlayer();
        DietCookie cookie = registerSlimefunItem(plugin, "TEST_DIET_COOKIE");

        simulateConsumption(player, cookie);

        assertThat(player, hasHeard(SoundEffect.DIET_COOKIE_CONSUME_SOUND.getDefaultSoundId()));
        Assertions.assertTrue(player.hasPotionEffect(PotionEffectType.LEVITATION));
    }

}

