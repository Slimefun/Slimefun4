package io.github.thebusybiscuit.slimefun4.testing.tests.items.implementations.tools;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.block.BlockMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.github.thebusybiscuit.slimefun4.api.events.ClimbingPickLaunchEvent;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ClimbingPick;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import io.github.thebusybiscuit.slimefun4.testing.interfaces.SlimefunItemTest;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class TestClimbingPick implements SlimefunItemTest<ClimbingPick> {

    private static ServerMock server;
    private static SlimefunPlugin plugin;

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

    @ParameterizedTest
    @EnumSource(value = BlockFace.class)
    public void testItemUse(BlockFace face) {
        PlayerMock player = server.addPlayer();
        ClimbingPick pick = registerSlimefunItem(plugin, "TEST_CLIMBING_PICK_" + face.name());
        Location blockLocation = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX() + 1, player.getLocation().getBlockY(), player.getLocation().getBlockZ());

        boolean shouldCancel = face == BlockFace.DOWN || face == BlockFace.UP;

        BlockMock block1 = new BlockMock(Material.STONE, blockLocation);
        simulateRightClickBlock(player, pick, block1, face);
        server.getPluginManager().assertEventFired(ClimbingPickLaunchEvent.class);
        if (!shouldCancel) player.assertSoundHeard(Sound.ENTITY_ENDERMAN_TELEPORT);

        BlockMock block2 = new BlockMock(Material.DIRT, blockLocation);
        simulateRightClickBlock(player, pick, block2, face);
        server.getPluginManager().assertEventFired(ClimbingPickLaunchEvent.class);
        if (!shouldCancel) player.assertSoundHeard(Sound.ENTITY_ENDERMAN_TELEPORT);
    }
}
