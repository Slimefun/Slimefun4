package io.github.thebusybiscuit.slimefun4.implementation.items.backpacks;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SoulboundListener;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;
import io.github.thebusybiscuit.slimefun4.test.presets.SlimefunItemTest;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestSoulBoundCache implements SlimefunItemTest<SoulboundBackpack> {

    private static ServerMock server;
    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
        new SoulboundListener(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }


    @Override
    public SoulboundBackpack registerSlimefunItem(Slimefun plugin, String id) {
        SlimefunItemStack item = new SlimefunItemStack("BOUND_BACKPACK", HeadTexture.ENDER_BACKPACK, "&cSoulbound Backpack", "", "&7Size: &e36", "&7ID: <ID>", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);
        SoulboundBackpack backpack = new SoulboundBackpack(10, TestUtilities.getItemGroup(plugin, "BOUND_BACKPACK"), item, RecipeType.NULL, new ItemStack[9]);
        backpack.register(plugin);
        return backpack;
    }


    @Test
    @DisplayName("Test soulbound cache")
    void testItemStorage() {
        PlayerMock player = server.addPlayer();
        SoulboundBackpack item = registerSlimefunItem(plugin, "BOUND_BACKPACK");
        player.getInventory().setItem(3, item.getItem());
        player.setHealth(0);
        //server.getPluginManager().callEvent(new PluginDisableEvent(plugin));
        server.getPluginManager().disablePlugin(plugin);

    }
    /*void testItemStorage() {
        PlayerMock player = server.addPlayer();
        SoulboundBackpack item = registerSlimefunItem(plugin, "BOUND_BACKPACK");
        player.getInventory().setItem(3, item.getItem());
        player.setHealth(0);
        PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(player, "cc");
        Bukkit.getPluginManager().callEvent(playerQuitEvent);

    }*/
}
