package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import java.util.EnumMap;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;

import org.bukkit.Color;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.entity.FireworksListener;
import io.github.thebusybiscuit.slimefun5.utils.FireworkUtils;

import org.mockito.Mockito;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

class TestFireworksListener {

    private static ServerMock server;
    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        Slimefun plugin = MockBukkit.load(Slimefun.class);
        new FireworksListener(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Test if Fireworks from Research cause no damage")
    void testFireworkDamage() {
        Player player = server.addPlayer();
        Firework firework = FireworkUtils.createFirework(player.getLocation(), Color.BLUE);

        DamageSource source = Mockito.mock(DamageSource.class);
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(firework, player, DamageCause.ENTITY_EXPLOSION, source,
                new EnumMap<>(ImmutableMap.of(DamageModifier.BASE, 6.0)),
                new EnumMap<DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(DamageModifier.BASE, Functions.constant(-0.0))),
                false);
        server.getPluginManager().callEvent(event);
        Assertions.assertTrue(event.isCancelled());
    }

}

