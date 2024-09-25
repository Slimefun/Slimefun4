package io.github.thebusybiscuit.slimefun4.test;

import static org.mockito.Mockito.when;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.block.BlockMock;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunBlockPlaceEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.test.mocks.MockSlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public final class TestUtilities {

    private static final Random random = new Random();

    private TestUtilities() {}

    @ParametersAreNonnullByDefault
    public static @Nonnull Inventory mockInventory(InventoryType type, ItemStack... contents) {
        Inventory inv = Mockito.mock(Inventory.class);

        when(inv.getType()).thenReturn(type);
        when(inv.getContents()).thenReturn(contents);

        return inv;
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull ItemGroup getItemGroup(Plugin plugin, String name) {
        return new ItemGroup(new NamespacedKey(plugin, name), new CustomItemStack(Material.NETHER_STAR, "&4Test ItemGroup"));
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull SlimefunItem mockSlimefunItem(Plugin plugin, String id, ItemStack item) {
        ItemGroup itemGroup = new ItemGroup(new NamespacedKey(plugin, "test"), new CustomItemStack(Material.EMERALD, "&4Test ItemGroup"));

        return new MockSlimefunItem(itemGroup, item, id);
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull VanillaItem mockVanillaItem(Plugin plugin, Material type, boolean enabled) {
        ItemGroup itemGroup = new ItemGroup(new NamespacedKey(plugin, "test"), new CustomItemStack(Material.EMERALD, "&4Test ItemGroup"));
        VanillaItem item = new VanillaItem(itemGroup, new ItemStack(type), type.name(), RecipeType.NULL, new ItemStack[9]);
        Slimefun.getItemCfg().setValue(type.name() + ".enabled", enabled);
        return item;
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull PlayerProfile awaitProfile(OfflinePlayer player) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<PlayerProfile> ref = new AtomicReference<>();

        // This loads the profile asynchronously
        Assertions.assertFalse(PlayerProfile.get(player, profile -> {
            ref.set(profile);
            latch.countDown();
        }));

        latch.await(2, TimeUnit.SECONDS);
        return ref.get();
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull int randomInt() {
        return random.nextInt(Integer.MAX_VALUE);
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull int randomInt(int upperBound) {
        return random.nextInt(upperBound);
    }

    public static World createWorld(ServerMock server) {
        World world = server.addSimpleWorld("world_" + randomInt());
        Slimefun.getRegistry().getWorlds().put(world.getName(), new BlockStorage(world));
        return world;
    }
    
    public static Block placeSlimefunBlock(ServerMock server, ItemStack item, World world, Player player) {
        int x = TestUtilities.randomInt();
        int z = TestUtilities.randomInt();
        Block block = new BlockMock(item.getType(), new Location(world, x, 0, z));
        Block blockAgainst = new BlockMock(Material.GRASS_BLOCK, new Location(world, x, 1, z));

         BlockPlaceEvent blockPlaceEvent  = new BlockPlaceEvent(
            block, block.getState(), blockAgainst, item, player, true, EquipmentSlot.HAND
        );

        server.getPluginManager().callEvent(blockPlaceEvent);
        server.getPluginManager().assertEventFired(SlimefunBlockPlaceEvent.class, e -> true);

        return block;
    }
}
