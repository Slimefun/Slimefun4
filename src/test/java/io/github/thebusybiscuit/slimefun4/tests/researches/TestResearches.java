package io.github.thebusybiscuit.slimefun4.tests.researches;

import java.util.Optional;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.mocks.TestUtilities;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class TestResearches {

    private static ServerMock server;
    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testResearchGetters() {
        NamespacedKey key = new NamespacedKey(plugin, "getter_test");
        Research research = new Research(key, 0, "Test", 100);
        research.register();

        Assertions.assertEquals(key, research.getKey());
        Assertions.assertEquals(100, research.getCost());

        Assertions.assertFalse(Research.getResearch(null).isPresent());
        Assertions.assertFalse(Research.getResearch(new NamespacedKey(plugin, "null")).isPresent());

        Assertions.assertTrue(Research.getResearch(key).isPresent());
        Assertions.assertEquals(research, Research.getResearch(key).get());
    }

    @Test
    public void testResearchCost() {
        NamespacedKey key = new NamespacedKey(plugin, "cost_test");
        Research research = new Research(key, 5, "Cost Test", 100);

        Assertions.assertEquals(100, research.getCost());

        research.setCost(3000);
        Assertions.assertEquals(3000, research.getCost());

        // Negative values are not allowed
        Assertions.assertThrows(IllegalArgumentException.class, () -> research.setCost(-100));
    }

    @Test
    public void testResearchRegistration() {
        NamespacedKey key = new NamespacedKey(plugin, "testResearch");
        Research research = new Research(key, 1, "Test", 100);
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "RESEARCH_TEST", new CustomItem(Material.TORCH, "&bResearch Test"));
        research.addItems(item, null);
        research.register();

        SlimefunPlugin.getRegistry().setResearchingEnabled(true);

        Assertions.assertTrue(research.isEnabled());
        Assertions.assertEquals(research, item.getResearch());
        Assertions.assertTrue(SlimefunPlugin.getRegistry().getResearches().contains(research));

        Optional<Research> optional = Research.getResearch(key);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(research, optional.get());
    }

    @Test
    public void testDisabledResearch() {
        NamespacedKey key = new NamespacedKey(plugin, "disabledResearch");
        Research research = new Research(key, 2, "Test", 100);
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "RESEARCH_TEST", new CustomItem(Material.TORCH, "&bResearch Test"));
        research.addItems(item);

        SlimefunPlugin.getRegistry().setResearchingEnabled(true);
        SlimefunPlugin.getResearchCfg().setValue(key.getNamespace() + '.' + key.getKey() + ".enabled", false);
        research.register();

        Assertions.assertFalse(research.isEnabled());
        Assertions.assertNull(item.getResearch());
    }

    @Test
    public void testResearchGloballyDisabled() {
        NamespacedKey key = new NamespacedKey(plugin, "globallyDisabledResearch");
        Research research = new Research(key, 3, "Test", 100);

        SlimefunPlugin.getRegistry().setResearchingEnabled(true);
        Assertions.assertTrue(research.isEnabled());
        SlimefunPlugin.getRegistry().setResearchingEnabled(false);
        Assertions.assertFalse(research.isEnabled());
        SlimefunPlugin.getRegistry().setResearchingEnabled(true);
    }

    @Test
    public void testAddItems() {
        NamespacedKey key = new NamespacedKey(plugin, "addItemsResearch");
        Research research = new Research(key, 17, "Test", 100);
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "RESEARCH_ITEMS_TEST", new CustomItem(Material.LAPIS_LAZULI, "&9Adding items is fun"));
        item.register(plugin);

        research.addItems(item.getItem(), null);

        Assertions.assertTrue(research.getAffectedItems().contains(item));
        Assertions.assertEquals(research, item.getResearch());
    }

    @Test
    public void testPlayerCanUnlockDisabledResearch() {
        SlimefunPlugin.getRegistry().setResearchingEnabled(false);

        Player player = server.addPlayer();
        NamespacedKey key = new NamespacedKey(plugin, "disabledUnlockableResearch");
        Research research = new Research(key, 567, "Test", 100);

        Assertions.assertTrue(research.canUnlock(player));

        SlimefunPlugin.getRegistry().setResearchingEnabled(true);
    }

    @Test
    public void testFreeCreativeResearch() {
        SlimefunPlugin.getRegistry().setResearchingEnabled(true);

        Player player = server.addPlayer();
        NamespacedKey key = new NamespacedKey(plugin, "freeCreativeResearch");
        Research research = new Research(key, 153, "Test", 100);

        SlimefunPlugin.getRegistry().setFreeCreativeResearchingEnabled(false);

        player.setGameMode(GameMode.SURVIVAL);
        Assertions.assertFalse(research.canUnlock(player));

        player.setGameMode(GameMode.CREATIVE);
        Assertions.assertFalse(research.canUnlock(player));

        SlimefunPlugin.getRegistry().setFreeCreativeResearchingEnabled(true);

        player.setGameMode(GameMode.SURVIVAL);
        Assertions.assertFalse(research.canUnlock(player));

        player.setGameMode(GameMode.CREATIVE);
        Assertions.assertTrue(research.canUnlock(player));
    }

    @Test
    public void testUnlockableResearch() {
        Player player = server.addPlayer();
        NamespacedKey key = new NamespacedKey(plugin, "freeCreativeResearch");
        Research research = new Research(key, 235, "Test", 4);

        Assertions.assertFalse(research.canUnlock(player));
        player.setLevel(8);
        Assertions.assertTrue(research.canUnlock(player));
    }

}
