package io.github.thebusybiscuit.slimefun4.tests.listeners;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SoulboundListener;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class TestSoulboundListener {

    private static SlimefunPlugin plugin;
    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        new SoulboundListener(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testItemDrop(boolean soulbound) {
        PlayerMock player = server.addPlayer();
        ItemStack item = new CustomItem(Material.DIAMOND_SWORD, "&4Cool Sword");
        SlimefunUtils.setSoulbound(item, soulbound);
        player.getInventory().setItem(6, item);
        player.setHealth(0);

        server.getPluginManager().assertEventFired(EntityDeathEvent.class, event -> {
            return soulbound != event.getDrops().contains(item);
        });
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testItemRecover(boolean soulbound) {
        PlayerMock player = server.addPlayer();
        ItemStack item = new CustomItem(Material.DIAMOND_SWORD, "&4Cool Sword");
        SlimefunUtils.setSoulbound(item, soulbound);
        player.getInventory().setItem(6, item);
        player.setHealth(0);
        player.respawn();

        server.getPluginManager().assertEventFired(PlayerRespawnEvent.class, event -> {
            return player.getInventory().getItem(6).isSimilar(item) == soulbound;
        });
    }

}
