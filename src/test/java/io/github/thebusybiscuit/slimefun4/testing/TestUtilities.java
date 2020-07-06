package io.github.thebusybiscuit.slimefun4.testing;

import static org.mockito.Mockito.when;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.testing.mocks.MockSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public final class TestUtilities {

    private TestUtilities() {}

    public static Inventory mockInventory(InventoryType type, ItemStack... contents) {
        Inventory inv = Mockito.mock(Inventory.class);

        when(inv.getType()).thenReturn(type);
        when(inv.getContents()).thenReturn(contents);

        return inv;
    }

    public static Category getCategory(Plugin plugin, String name) {
        return new Category(new NamespacedKey(plugin, name), new CustomItem(Material.NETHER_STAR, "&4Test Category"));
    }

    public static SlimefunItem mockSlimefunItem(Plugin plugin, String id, ItemStack item) {
        Category category = new Category(new NamespacedKey(plugin, "test"), new CustomItem(Material.EMERALD, "&4Test Category"));

        return new MockSlimefunItem(category, item, id);
    }

    public static VanillaItem mockVanillaItem(Plugin plugin, Material type, boolean enabled) {
        Category category = new Category(new NamespacedKey(plugin, "test"), new CustomItem(Material.EMERALD, "&4Test Category"));
        VanillaItem item = new VanillaItem(category, new ItemStack(type), type.name(), RecipeType.NULL, new ItemStack[9]);
        SlimefunPlugin.getItemCfg().setValue(type.name() + ".enabled", enabled);
        return item;
    }

    public static PlayerProfile awaitProfile(OfflinePlayer player) throws InterruptedException {
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

    public static void registerDefaultTags(ServerMock server) {
        // We really don't need these to be accurate, just fill them with some examples
        // that approximate the actual content
        server.createMaterialTag(NamespacedKey.minecraft("logs"), Material.OAK_LOG, Material.STRIPPED_OAK_LOG, Material.OAK_WOOD, Material.STRIPPED_OAK_WOOD, Material.ACACIA_LOG, Material.STRIPPED_ACACIA_LOG, Material.ACACIA_WOOD, Material.STRIPPED_ACACIA_WOOD);
        server.createMaterialTag(NamespacedKey.minecraft("wooden_trapdoors"), Material.OAK_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.ACACIA_TRAPDOOR, Material.DARK_OAK_TRAPDOOR);
        server.createMaterialTag(NamespacedKey.minecraft("wooden_slabs"), Material.OAK_SLAB, Material.BIRCH_SLAB, Material.JUNGLE_SLAB, Material.SPRUCE_SLAB, Material.ACACIA_SLAB, Material.DARK_OAK_SLAB);
        server.createMaterialTag(NamespacedKey.minecraft("wooden_fences"), Material.OAK_FENCE, Material.BIRCH_FENCE, Material.JUNGLE_FENCE, Material.SPRUCE_FENCE, Material.ACACIA_FENCE, Material.DARK_OAK_FENCE);
        server.createMaterialTag(NamespacedKey.minecraft("planks"), Material.OAK_PLANKS, Material.BIRCH_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS, Material.ACACIA_PLANKS, Material.DARK_OAK_PLANKS);
        server.createMaterialTag(NamespacedKey.minecraft("small_flowers"), Material.POPPY, Material.DANDELION, Material.AZURE_BLUET, Material.LILY_OF_THE_VALLEY);
        server.createMaterialTag(NamespacedKey.minecraft("leaves"), Material.OAK_LEAVES, Material.BIRCH_LEAVES, Material.SPRUCE_LEAVES, Material.JUNGLE_LEAVES, Material.ACACIA_LEAVES, Material.DARK_OAK_LEAVES);
        server.createMaterialTag(NamespacedKey.minecraft("saplings"), Material.OAK_SAPLING, Material.BIRCH_SAPLING, Material.SPRUCE_SAPLING, Material.JUNGLE_SAPLING, Material.ACACIA_SAPLING, Material.DARK_OAK_SAPLING);
        server.createMaterialTag(NamespacedKey.minecraft("coral_blocks"), Material.BRAIN_CORAL_BLOCK, Material.BUBBLE_CORAL_BLOCK, Material.FIRE_CORAL_BLOCK, Material.HORN_CORAL_BLOCK, Material.TUBE_CORAL_BLOCK);
        server.createMaterialTag(NamespacedKey.minecraft("corals"), Material.BRAIN_CORAL, Material.BUBBLE_CORAL, Material.FIRE_CORAL, Material.HORN_CORAL, Material.TUBE_CORAL);
    }

}
