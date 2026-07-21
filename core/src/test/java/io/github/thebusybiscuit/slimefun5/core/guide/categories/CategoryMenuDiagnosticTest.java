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
import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;
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
        // A real boot links each item into its ItemGroup via item.load() (run by PostSetup.loadItems in
        // SlimefunStartupTask). Without it, groups report 0 items and the guide's visibility filter drops
        // everything - a harness artifact, not the live behaviour. Run it so the guide path is faithful.
        io.github.thebusybiscuit.slimefun5.implementation.setup.PostSetup.loadItems();
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
    void addonItemSplitsIntoTypeSectionFromRegistry() {
        // Register an addon-NAMESPACED item (group namespace != "slimefun" is what marks it as an addon item
        // to build()) and assert it's classified into a "<Type>" section under the shared type category.
        // This exercises the addon path (core-only boots never did) - exactly where the in-game split failed.
        // Registered with the Slimefun instance so the addon-dependency check is skipped in the harness.
        ItemGroup group = new ItemGroup(new io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey("myaddon", "stuff"),
            io.github.bakedlibs.dough.items.CustomItemStack.create(org.bukkit.Material.CHEST, "&7MyAddon"));
        group.register(plugin);
        io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack stack =
            new io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack("MY_TEST_SWORD", org.bukkit.Material.DIAMOND_SWORD, "&cTest Sword");
        io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem item =
            new io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem(group, stack, io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType.NULL, new org.bukkit.inventory.ItemStack[9]);
        item.register(plugin);

        Player p = server.addPlayer();
        List<ItemGroup> visible = new ArrayList<>();
        for (ItemGroup g : Slimefun.getRegistry().getAllItemGroups()) {
            if (!(g instanceof CategoryItemGroup)) {
                visible.add(g);
            }
        }

        List<ItemGroup> tiles = CategoryMenuBuilder.build(p, visible, Slimefun.getGuideCategories());

        CategoryItemGroup weapons = tiles.stream()
            .map(t -> (CategoryItemGroup) t)
            .filter(t -> DefaultGuideCategories.WEAPONS.equals(t.getCategory().getId()))
            .findFirst().orElse(null);
        Assertions.assertNotNull(weapons, "Weapons category must exist");

        boolean hasTypedSection = weapons.getMembers().stream()
            .anyMatch(m -> m.getKey().getKey().startsWith("typed_weapons_") && m.getItems().contains(item));
        Assertions.assertTrue(hasTypedSection,
            "the addon sword must appear as a typed weapon section under Weapons; members="
                + weapons.getMembers().stream().map(m -> m.getKey().getKey()).collect(Collectors.joining(",")));
    }

    @Test
    void realGuideMainMenuIsNotEmpty() throws Exception {
        // Exercise the REAL entry point (getVisibleItemGroups -> collectVisibleCategories -> build), not
        // build() directly, so an empty-guide regression in that path is caught. The guide must produce
        // category tiles from core's curated groups.
        org.bukkit.entity.Player player = server.addPlayer();
        // A real server has its worlds enabled (WorldSettingsService loaded on world-load); the mock does
        // not, so without this every item reports isDisabledIn=true and collectVisibleCategories filters
        // everything - mimicking an empty guide that would NOT happen on a live server.
        Slimefun.getWorldSettingsService().load(player.getWorld());
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        java.util.concurrent.atomic.AtomicReference<PlayerProfile> ref = new java.util.concurrent.atomic.AtomicReference<>();
        PlayerProfile.get(player, pr -> { ref.set(pr); latch.countDown(); });
        latch.await(2, java.util.concurrent.TimeUnit.SECONDS);
        PlayerProfile profile = ref.get();
        Assertions.assertNotNull(profile);

        io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideImplementation guide =
            Slimefun.getRegistry().getSlimefunGuide(io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideMode.SURVIVAL_MODE);
        java.lang.reflect.Method m = io.github.thebusybiscuit.slimefun5.implementation.guide.SurvivalSlimefunGuide.class
            .getDeclaredMethod("getVisibleItemGroups", org.bukkit.entity.Player.class, PlayerProfile.class);
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<ItemGroup> tiles = (List<ItemGroup>) m.invoke(guide, player, profile);

        String diag = "\nregisteredCategories=" + Slimefun.getGuideCategories().getAll().size()
            + "\nenabledItems=" + Slimefun.getRegistry().getEnabledSlimefunItems().size() + "\ntiles=" + tiles.size();

        Assertions.assertFalse(tiles.isEmpty(), "the main menu must not be empty (category tiles from core groups)" + diag);
    }

    @Test
    void openMainMenuActuallyPlacesTiles() throws Exception {
        // The strongest reproduction: drive the REAL openMainMenu and inspect the inventory the player is
        // shown. getVisibleItemGroups being non-empty is necessary but not sufficient - a rendering fault
        // (widget NPE, packet rewrite, slot math) could still leave the shown menu blank.
        org.bukkit.entity.Player player = server.addPlayer();
        Slimefun.getWorldSettingsService().load(player.getWorld());
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        java.util.concurrent.atomic.AtomicReference<PlayerProfile> ref = new java.util.concurrent.atomic.AtomicReference<>();
        PlayerProfile.get(player, pr -> { ref.set(pr); latch.countDown(); });
        latch.await(2, java.util.concurrent.TimeUnit.SECONDS);
        PlayerProfile profile = ref.get();
        Assertions.assertNotNull(profile);

        io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideImplementation guide =
            Slimefun.getRegistry().getSlimefunGuide(io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideMode.SURVIVAL_MODE);
        guide.openMainMenu(profile, 1);

        org.bukkit.inventory.Inventory top = player.getOpenInventory() == null ? null : player.getOpenInventory().getTopInventory();
        int tileCount = 0;
        if (top != null) {
            for (int slot = 9; slot <= 44; slot++) {
                org.bukkit.inventory.ItemStack it = top.getItem(slot);
                if (it != null && it.getType() != org.bukkit.Material.AIR) {
                    tileCount++;
                }
            }
        }
        Assertions.assertNotNull(top, "openMainMenu must open an inventory");
        Assertions.assertTrue(tileCount > 0, "the opened main menu must contain category tiles in the content slots; found " + tileCount);
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
