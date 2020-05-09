package io.github.thebusybiscuit.slimefun4.tests.researches;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.mocks.SlimefunMocks;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class TestResearches {

    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testResearchGetters() {
        NamespacedKey key = new NamespacedKey(plugin, "test");
        Research research = new Research(key, 0, "Test", 100);

        Assertions.assertEquals(key, research.getKey());
        Assertions.assertEquals(100, research.getCost());

        Assertions.assertFalse(Research.getResearch(null).isPresent());
        Assertions.assertFalse(Research.getResearch(key).isPresent());
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
        SlimefunItem item = SlimefunMocks.mockSlimefunItem(plugin, "RESEARCH_TEST", new CustomItem(Material.TORCH, "&bResearch Test"));
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
        SlimefunItem item = SlimefunMocks.mockSlimefunItem(plugin, "RESEARCH_TEST", new CustomItem(Material.TORCH, "&bResearch Test"));
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

}
