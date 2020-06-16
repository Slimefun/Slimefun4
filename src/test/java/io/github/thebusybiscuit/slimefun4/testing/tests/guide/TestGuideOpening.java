package io.github.thebusybiscuit.slimefun4.testing.tests.guide;

import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.GuideHistory;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class TestGuideOpening {

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

    private PlayerProfile prepare(SlimefunGuideImplementation guide, Consumer<GuideHistory> consumer) throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);
        GuideHistory history = profile.getGuideHistory();
        consumer.accept(history);
        history.openLastEntry(guide);
        return profile;
    }

    @Test
    public void testOpenMainMenu() throws InterruptedException {
        SlimefunGuideImplementation guide = Mockito.mock(SlimefunGuideImplementation.class);
        PlayerProfile profile = prepare(guide, history -> {});
        Mockito.verify(guide).openMainMenu(profile, 1);
    }

    @Test
    public void testOpenCategory() throws InterruptedException {
        Category category = new Category(new NamespacedKey(plugin, "history_category"), new CustomItem(Material.BLUE_TERRACOTTA, "&9Testy test"));

        SlimefunGuideImplementation guide = Mockito.mock(SlimefunGuideImplementation.class);
        PlayerProfile profile = prepare(guide, history -> history.add(category, 1));
        Mockito.verify(guide).openCategory(profile, category, 1);
    }

    @Test
    public void testOpenSlimefunItem() throws InterruptedException {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "OPEN_SLIMEFUN_ITEM", new CustomItem(Material.PRISMARINE_SHARD, "&5Some Shard I guess"));

        SlimefunGuideImplementation guide = Mockito.mock(SlimefunGuideImplementation.class);
        PlayerProfile profile = prepare(guide, history -> history.add(item));
        Mockito.verify(guide).displayItem(profile, item, false);
    }

    @Test
    public void testOpenItemStack() throws InterruptedException {
        ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);

        SlimefunGuideImplementation guide = Mockito.mock(SlimefunGuideImplementation.class);
        PlayerProfile profile = prepare(guide, history -> history.add(item, 1));
        Mockito.verify(guide).displayItem(profile, item, 1, false);
    }

    @Test
    public void testOpenSearch() throws InterruptedException {
        String query = "electric";

        SlimefunGuideImplementation guide = Mockito.mock(SlimefunGuideImplementation.class);
        PlayerProfile profile = prepare(guide, history -> history.add(query));
        Mockito.verify(guide).openSearch(profile, query, false);
    }

    @Test
    public void testGoBack() throws InterruptedException {
        Player player = server.addPlayer();

        String query = "electric";
        String query2 = "cargo";

        SlimefunGuideImplementation guide = Mockito.mock(SlimefunGuideImplementation.class);

        PlayerProfile profile = TestUtilities.awaitProfile(player);
        GuideHistory history = profile.getGuideHistory();
        history.add(query);
        history.add(query2);
        history.goBack(guide);
        Mockito.verify(guide).openSearch(profile, query, false);
    }

}
