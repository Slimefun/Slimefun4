package io.github.thebusybiscuit.slimefun4.testing.tests.items.implementations.tools;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.block.BlockMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.github.thebusybiscuit.slimefun4.api.events.ClimbingPickLaunchEvent;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ClimbingPick;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import io.github.thebusybiscuit.slimefun4.testing.interfaces.SlimefunItemTest;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class TestClimbingPick implements SlimefunItemTest<ClimbingPick> {

    private static ServerMock server;
    private static SlimefunPlugin plugin;
    private static ClimbingPick pick;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        TestUtilities.registerDefaultTags(server);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Override
    public ClimbingPick registerSlimefunItem(SlimefunPlugin plugin, String id) {
        SlimefunItemStack item = new SlimefunItemStack(id, Material.IRON_PICKAXE, "&5Test Pick");
        ClimbingPick pick = new ClimbingPick(TestUtilities.getCategory(plugin, "climbing_pick"), item, RecipeType.NULL, new ItemStack[9]);
        pick.register(plugin);
        return pick;
    }

    @Test
    public void testItemUse() {
        PlayerMock player = server.addPlayer();
        if (pick == null) pick = registerSlimefunItem(plugin, "TEST_CLIMBING_PICK");

        for (BlockFace face : BlockFace.values()) {
            BlockMock block1 = new BlockMock(Material.STONE);
            simulateRightClickBlock(player, pick, block1, face);
            server.getPluginManager().assertEventFired(ClimbingPickLaunchEvent.class, e -> !e.isCancelled());

            BlockMock block2 = new BlockMock(Material.DIRT);
            simulateRightClickBlock(player, pick, block2, face);
            server.getPluginManager().assertEventFired(ClimbingPickLaunchEvent.class, ClimbingPickLaunchEvent::isCancelled);
        }
    }
}
