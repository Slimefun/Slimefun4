package io.github.thebusybiscuit.slimefun4.tests.listeners;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.BackpackListener;
import io.github.thebusybiscuit.slimefun4.mocks.TestUtilities;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class TestBackpackListener {

    private static int BACKPACK_SIZE = 27;
    private static ServerMock server;
    private static SlimefunPlugin plugin;
    private static BackpackListener listener;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        listener = new BackpackListener();
        listener.register(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testIllegalSetId() {
        Player player = server.addPlayer();

        Assertions.assertThrows(IllegalArgumentException.class, () -> listener.setBackpackId(null, null, 1, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> listener.setBackpackId(player, null, 1, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> listener.setBackpackId(player, new ItemStack(Material.REDSTONE), 1, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> listener.setBackpackId(player, new CustomItem(Material.REDSTONE, "Hi", "lore"), 1, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> listener.setBackpackId(player, new CustomItem(Material.REDSTONE, "Hi", "lore", "no id"), 1, 1));
    }

    @Test
    public void testSetId() throws InterruptedException {
        Player player = server.addPlayer();
        ItemStack item = new CustomItem(Material.CHEST, "&4Mock Backpack", "", "&7Size: &e" + BACKPACK_SIZE, "&7ID: <ID>", "", "&7&eRight Click&7 to open");

        PlayerProfile profile = TestUtilities.awaitProfile(player);
        int id = profile.createBackpack(BACKPACK_SIZE).getId();

        listener.setBackpackId(player, item, 2, id);
        Assertions.assertEquals(ChatColor.GRAY + "ID: " + player.getUniqueId() + "#" + id, item.getItemMeta().getLore().get(2));

        PlayerBackpack backpack = awaitBackpack(item);
        Assertions.assertEquals(player.getUniqueId(), backpack.getOwner().getUUID());
        Assertions.assertEquals(id, backpack.getId());
    }

    private PlayerBackpack awaitBackpack(ItemStack item) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<PlayerBackpack> ref = new AtomicReference<>();

        // This loads the backpack asynchronously
        PlayerProfile.getBackpack(item, backpack -> {
            ref.set(backpack);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
        return ref.get();
    }

}
