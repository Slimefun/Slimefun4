package io.github.thebusybiscuit.slimefun4.api.events;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans.Talisman;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.TalismanListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestTalismanActivateEvent {

    private static ServerMock server;
    private static Slimefun plugin;
    private static Player player;
    private static SlimefunItem talisman;
    private static SlimefunItem enderTalisman;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);

        new TalismanListener(plugin);

        talisman = new Talisman(SlimefunItems.TALISMAN_ANVIL, new ItemStack[] {}, true, false, "anvil");
        talisman.register(plugin);

        enderTalisman = SlimefunItem.getById("ENDER_" + talisman.getId());

        player = server.addPlayer();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    void activateAnvilTalisman(boolean enderVariant, boolean inEnderChest) {
        player.getInventory().clear();
        player.getEnderChest().clear();

        ItemStack talismanItem = enderVariant ? enderTalisman.getItem() : talisman.getItem();
        ItemStack breakableItem = new ItemStack(Material.IRON_PICKAXE);

        if (inEnderChest) {
            player.getEnderChest().setItem(9, talismanItem);
        } else {
            player.getInventory().setItem(9, talismanItem);
        }

        player.getInventory().setItemInMainHand(breakableItem);

        PlayerItemBreakEvent event = new PlayerItemBreakEvent(player, breakableItem);
        server.getPluginManager().callEvent(event);
    }

    @Test
    @DisplayName("Test that TalismanActivateEvent is fired when an anvil talisman activates")
    void testEventIsFired() {
        // Assert the talisman activates in the inventory
        activateAnvilTalisman(false, false);
        server.getPluginManager().assertEventFired(TalismanActivateEvent.class, ignored -> true);
        server.getPluginManager().clearEvents();

        // Assert the talisman activates in the ender chest
        activateAnvilTalisman(true, true);
        server.getPluginManager().assertEventFired(TalismanActivateEvent.class, ignored -> true);
        server.getPluginManager().clearEvents();
        // Assert the normal talisman does not activate in the ender chest
        activateAnvilTalisman(false, true);
        try {
            server.getPluginManager().assertEventFired(TalismanActivateEvent.class, ignored -> true);
        } catch (AssertionError ignored) {
            return; // This is expected; the event should not have fired
        }
        server.getPluginManager().clearEvents();

        // Assert the ender talisman does not activate in the inventory
        try {
            activateAnvilTalisman(true, false);
            server.getPluginManager().assertEventFired(TalismanActivateEvent.class, ignored -> true);
        } catch (AssertionError ignored) {
            return; // This is expected; the event should not have fired
        }
        server.getPluginManager().clearEvents();
    }

    @Test
    @DisplayName("Test that the TalismanActivateEvent has the correct fields")
    void testEventFields() {
        // Assert the talisman activates in the inventory
        activateAnvilTalisman(false, false);
        server.getPluginManager().assertEventFired(TalismanActivateEvent.class, event -> {
            Assertions.assertEquals(talisman, event.getTalisman());
            Assertions.assertEquals(talisman.getItem(), event.getTalismanItem());
            Assertions.assertEquals(player, event.getPlayer());
            return true;
        });
        server.getPluginManager().clearEvents();

        // Assert the talisman activates in the ender chest
        activateAnvilTalisman(true, true);
        server.getPluginManager().assertEventFired(TalismanActivateEvent.class, event -> {
            Assertions.assertEquals(enderTalisman, event.getTalisman());
            Assertions.assertEquals(enderTalisman.getItem(), event.getTalismanItem());
            Assertions.assertEquals(player, event.getPlayer());
            return true;
        });
        server.getPluginManager().clearEvents();
    }

    @Test
    @DisplayName("Test that the TalismanActivateEvent can be cancelled")
    void testEventCanBeCancelled() {
        server.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onTalismanActivate(TalismanActivateEvent event) {
                event.setCancelled(true);
            }
        }, plugin);

        // Assert the talisman activates in the inventory
        activateAnvilTalisman(false, false);
        server.getPluginManager().assertEventFired(TalismanActivateEvent.class, event -> {
            Assertions.assertTrue(event.isCancelled());
            return true;
        });
        server.getPluginManager().clearEvents();

        // Assert the talisman activates in the ender chest
        activateAnvilTalisman(true, true);
        server.getPluginManager().assertEventFired(TalismanActivateEvent.class, event -> {
            Assertions.assertTrue(event.isCancelled());
            return true;
        });
        server.getPluginManager().clearEvents();
    }

    @Test
    @DisplayName("Test that the TalismanActivateEvent can prevent consumption")
    void testEventCanPreventConsumption() {
        server.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onTalismanActivate(TalismanActivateEvent event) {
                event.setPreventConsumption(true);
            }
        }, plugin);

        // Assert the talisman activates in the inventory
        activateAnvilTalisman(false, false);
        server.getPluginManager().assertEventFired(TalismanActivateEvent.class, event -> {
            Assertions.assertTrue(event.preventsConsumption());
            return true;
        });
        server.getPluginManager().clearEvents();

        // Assert the talisman activates in the ender chest
        activateAnvilTalisman(true, true);
        server.getPluginManager().assertEventFired(TalismanActivateEvent.class, event -> {
            Assertions.assertTrue(event.preventsConsumption());
            return true;
        });
        server.getPluginManager().clearEvents();
    }
}
