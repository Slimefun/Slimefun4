package io.github.thebusybiscuit.slimefun5.test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import io.github.thebusybiscuit.slimefun5.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Verifies the backpack identity layer that replaced the fragile visible "§7ID: <uuid>#<n>" lore line.
 * Identity now lives in the item's persistent data (invisible), with a legacy-lore fallback so backpacks
 * from older Slimefun 4/5 versions keep working and migrate on first touch. Two different identities must
 * never compare equal - that is what stops distinct backpacks from stacking and duplicating.
 */
class BackpackIdentityTest {

    private static final String ID_PREFIX = ChatColor.GRAY + "ID: ";

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    private static ItemStack backpack() {
        return new ItemStack(Material.CHEST);
    }

    @Test
    @DisplayName("A fresh backpack has no identity (so openBackpack will assign one)")
    void freshHasNoIdentity() {
        Assertions.assertFalse(PlayerBackpack.readIdentity(backpack()).isPresent());
    }

    @Test
    @DisplayName("writeIdentity stores the id in persistent data and reads back")
    void writeThenRead() {
        ItemStack item = backpack();
        PlayerBackpack.writeIdentity(item, "0000#5");
        Assertions.assertEquals(Optional.of("0000#5"), PlayerBackpack.readIdentity(item));
    }

    @Test
    @DisplayName("A legacy visible ID lore line is read, then migrated to PDC and hidden on write")
    void legacyLoreMigratesAndHides() {
        ItemStack item = backpack();
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Size: 9", ID_PREFIX + "abcd#7"));
        item.setItemMeta(meta);

        // Read falls back to the legacy lore line.
        Assertions.assertEquals(Optional.of("abcd#7"), PlayerBackpack.readIdentity(item));

        // Writing migrates it into PDC and strips the visible line.
        PlayerBackpack.writeIdentity(item, "abcd#7");

        List<String> lore = item.getItemMeta().getLore();
        Assertions.assertTrue(lore == null || lore.stream().noneMatch(l -> l.startsWith(ID_PREFIX)),
            "the visible ID lore line must be removed (hidden)");
        Assertions.assertEquals(Optional.of("abcd#7"), PlayerBackpack.readIdentity(item),
            "identity must still be readable from PDC after hiding the lore line");
    }

    @Test
    @DisplayName("The unassigned '<ID>' placeholder is not treated as a real identity")
    void placeholderIsNotIdentity() {
        ItemStack item = backpack();
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(ID_PREFIX + "<ID>"));
        item.setItemMeta(meta);
        Assertions.assertFalse(PlayerBackpack.readIdentity(item).isPresent());
    }

    @Test
    @DisplayName("Different identities never compare equal (dupe guard); same/unassigned do")
    void identityDistinguishesBackpacks() {
        ItemStack a = backpack();
        ItemStack b = backpack();
        PlayerBackpack.writeIdentity(a, "0000#1");
        PlayerBackpack.writeIdentity(b, "0000#2");

        // canStack delegates to identity equality - two different backpacks must NOT be equal.
        Assertions.assertNotEquals(PlayerBackpack.readIdentity(a), PlayerBackpack.readIdentity(b));
        // Two blank/unassigned backpacks share (empty) identity and may stack.
        Assertions.assertEquals(PlayerBackpack.readIdentity(backpack()), PlayerBackpack.readIdentity(backpack()));
    }
}
