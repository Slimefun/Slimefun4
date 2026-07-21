package io.github.thebusybiscuit.slimefun5.core.guide.categories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.setup.SlimefunItemSetup;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;

/** Diagnostic: dumps the real categorization of Slimefun's own item groups so we can see whether any land in a fallback. */
class CategoryMenuDiagnosticTest {

    private static ServerMock server;
    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
        SlimefunItemSetup.setup(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void dumpSlimefunGroupCategories() {
        List<String> undeclared = new ArrayList<>();
        int slimefunGroups = 0;

        for (ItemGroup group : Slimefun.getRegistry().getAllItemGroups()) {
            if (group instanceof CategoryItemGroup) {
                continue;
            }

            if ("slimefun".equals(group.getKey().getNamespace())) {
                slimefunGroups++;
                if (group.getCategoryId() == null) {
                    undeclared.add(group.getKey().getKey() + " (" + group.getClass().getSimpleName() + ")");
                }
            }
        }

        Player p = server.addPlayer();
        List<ItemGroup> visible = new ArrayList<>();
        for (ItemGroup group : Slimefun.getRegistry().getAllItemGroups()) {
            if (!(group instanceof CategoryItemGroup)) {
                visible.add(group);
            }
        }

        List<ItemGroup> tiles = CategoryMenuBuilder.build(p, visible, Slimefun.getGuideCategories());
        String tileSummary = tiles.stream()
            .map(t -> ((CategoryItemGroup) t).getCategory().getId() + "=" + ((CategoryItemGroup) t).getMembers().size())
            .collect(Collectors.joining(", "));

        String report = "\nslimefun groups=" + slimefunGroups
            + "\nundeclared slimefun groups=" + undeclared
            + "\ntiles=" + tileSummary;

        // Every Slimefun-owned group must declare a category, so Slimefun's own content never appears
        // under an auto per-addon "addon:slimefun" fallback tile.
        Assertions.assertTrue(undeclared.isEmpty(), "Undeclared Slimefun groups fall into an 'addon:slimefun' fallback tile:" + report);
        Assertions.assertTrue(tiles.stream().noneMatch(t -> "addon:slimefun".equals(((CategoryItemGroup) t).getCategory().getId())),
            "The categorized menu must not produce an 'addon:slimefun' fallback tile:" + report);
    }

    @Test
    @SuppressWarnings("deprecation")
    void addonGroupsSplitAcrossCategoriesByTheirDeclaredTheme() {
        // Reproduce exactly what a theme-tagged addon does (e.g. Networks: setTheme("logistics"/"tools"/...)):
        // distinct themes on one addon's groups must land the groups in the matching canonical categories,
        // NOT lump the whole addon into one tile. An undeclared group falls back to the per-addon tile.
        Player p = server.addPlayer();

        ItemGroup weaponsGroup = new ItemGroup(new NamespacedKey("myaddon", "wg"), new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_SWORD));
        weaponsGroup.setTheme("weapons");
        ItemGroup toolsGroup = new ItemGroup(new NamespacedKey("myaddon", "tg"), new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_PICKAXE));
        toolsGroup.setTheme("tools");
        ItemGroup undeclared = new ItemGroup(new NamespacedKey("myaddon", "ug"), new org.bukkit.inventory.ItemStack(org.bukkit.Material.CHEST));

        List<ItemGroup> tiles = CategoryMenuBuilder.build(p, java.util.Arrays.asList(weaponsGroup, toolsGroup, undeclared), Slimefun.getGuideCategories());

        java.util.Map<String, java.util.List<ItemGroup>> byCat = new java.util.HashMap<>();
        for (ItemGroup t : tiles) {
            CategoryItemGroup cig = (CategoryItemGroup) t;
            byCat.put(cig.getCategory().getId(), cig.getMembers());
        }

        Assertions.assertTrue(byCat.getOrDefault("weapons", java.util.Collections.emptyList()).contains(weaponsGroup),
            "a setTheme(\"weapons\") group must land in the Weapons category, not a per-addon tile: " + byCat.keySet());
        Assertions.assertTrue(byCat.getOrDefault("tools", java.util.Collections.emptyList()).contains(toolsGroup),
            "a setTheme(\"tools\") group must land in the Tools category: " + byCat.keySet());
        Assertions.assertTrue(byCat.getOrDefault("addon:myaddon", java.util.Collections.emptyList()).contains(undeclared),
            "an undeclared group must fall back to the per-addon tile: " + byCat.keySet());
    }
}
