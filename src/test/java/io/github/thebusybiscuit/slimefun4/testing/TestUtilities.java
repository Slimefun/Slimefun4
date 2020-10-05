package io.github.thebusybiscuit.slimefun4.testing;

import static org.mockito.Mockito.when;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

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

    @Nonnull
    public static PlayerProfile awaitProfile(@Nonnull OfflinePlayer player) throws InterruptedException {
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
}
