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
}
