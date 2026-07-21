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
    void declaredGuideTypeOverridesHeuristic() {
        io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem item =
            io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem.getById("ELECTRIC_MOTOR");
        Assertions.assertNotNull(item, "ELECTRIC_MOTOR must be registered");
        try {
            item.setGuideType(DefaultGuideCategories.RESOURCES);
            Assertions.assertEquals(DefaultGuideCategories.RESOURCES, ItemTypeClassifier.classify(item),
                "an explicit guide type must win over the heuristic");
        } finally {
            item.setGuideType(null); // restore heuristic for other tests
        }
    }

    @Test
    void coreGroupsCategorizedAndNoAddonFallbackInPureCoreBoot() {
        // The headless boot has only core items, so the full <Addon> <Type> item split can't be asserted
        // here (ItemTypeClassifier is unit-tested separately, end-to-end split is verified in-game). This
        // guards that the core path still produces canonical category tiles from core's curated groups and
        // never emits a per-addon "addon:*" fallback tile.
        Player p = server.addPlayer();

        List<ItemGroup> visible = new ArrayList<>();
        for (ItemGroup group : Slimefun.getRegistry().getAllItemGroups()) {
            if (!(group instanceof CategoryItemGroup)) {
                visible.add(group);
            }
        }

        List<ItemGroup> tiles = CategoryMenuBuilder.build(p, visible, Slimefun.getGuideCategories());

        CategoryItemGroup weapons = tiles.stream()
            .map(t -> (CategoryItemGroup) t)
            .filter(t -> DefaultGuideCategories.WEAPONS.equals(t.getCategory().getId()))
            .findFirst().orElse(null);
        Assertions.assertNotNull(weapons, "a Weapons category tile must exist from core's weapons group");
        Assertions.assertFalse(weapons.getMembers().isEmpty(), "the Weapons tile must have members");

        Assertions.assertTrue(tiles.stream().noneMatch(t -> ((CategoryItemGroup) t).getCategory().getId().startsWith("addon:")),
            "no per-addon fallback tiles in a pure-core boot");
    }
}
