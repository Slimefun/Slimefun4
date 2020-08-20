package io.github.thebusybiscuit.slimefun4.testing.tests.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.meta.FireworkMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.inventory.meta.FireworkMetaMock;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.FireworksListener;

class TestFireworksListener {

    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        SlimefunPlugin plugin = MockBukkit.load(SlimefunPlugin.class);
        new FireworksListener(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test if Fireworks from Research cause no damage")
    void testFireworkDamage() {
        Player player = server.addPlayer();
        Firework firework = Mockito.mock(Firework.class);
        FireworkMeta meta = new FireworkMetaMock();
        meta.setDisplayName(ChatColor.GREEN + "Slimefun Research");

        Mockito.when(firework.getType()).thenReturn(EntityType.FIREWORK);
        Mockito.when(firework.getFireworkMeta()).thenReturn(meta);

        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(firework, player, DamageCause.ENTITY_EXPLOSION, 6.0);
        server.getPluginManager().callEvent(event);
        Assertions.assertTrue(event.isCancelled());
    }

}
