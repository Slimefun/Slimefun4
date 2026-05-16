package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import java.util.EnumMap;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;

import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.entity.BeeListener;
import io.github.thebusybiscuit.slimefun5.test.TestUtilities;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

class TestBeeListener {

    private static ServerMock server;
    private static Slimefun plugin;
    private static BeeListener listener;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
        listener = new BeeListener(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @SuppressWarnings("deprecation")
    @ParameterizedTest
    @DisplayName("Test Bee damage protection")
    @ValueSource(booleans = { true, false })
    void testBeeDamage(boolean hasArmor) throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        if (hasArmor) {
            ItemGroup itemGroup = TestUtilities.getItemGroup(plugin, "bee_suit_test");
            SlimefunItemStack chestplate = new SlimefunItemStack("MOCK_BEE_SUIT", Material.LEATHER_CHESTPLATE, "&cBee Suit Prototype");
            MockBeeProtectionSuit armor = new MockBeeProtectionSuit(itemGroup, chestplate);
            armor.register(plugin);

            player.getInventory().setChestplate(chestplate.item());
            // Force update the cached armor
            profile.getArmor()[1].update(chestplate.item(), armor);
        }

        double damage = 7.5;

        Bee bee = Mockito.mock(Bee.class);
        DamageSource source = Mockito.mock(DamageSource.class);
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(bee, player, DamageCause.ENTITY_ATTACK, source,
                new EnumMap<>(ImmutableMap.of(DamageModifier.BASE, damage)),
                new EnumMap<DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(DamageModifier.BASE, Functions.constant(-0.0))),
                false);
        listener.onDamage(event);

        if (hasArmor) {
            Assertions.assertEquals(0, event.getDamage());
        } else {
            Assertions.assertEquals(damage, event.getDamage());
        }
    }

}

