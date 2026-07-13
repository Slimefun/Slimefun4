package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.FileUtils;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockInfoConfig;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

/**
 * Pure-JDBC schema test: no MockBukkit needed since the schema only touches H2 via plain SQL.
 * Block/chunk round-trip tests use MockBukkit for {@link World}/{@link Location}.
 */
class JdbcBackendTest {

    private static ServerMock server;
    private static World world;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
        world = server.createWorld(WorldCreator.name("world").environment(Environment.NORMAL));

        // MockBukkit doesn't always trigger ItemStack/ItemMeta's static ConfigurationSerialization
        // registration, needed for YamlConfiguration (de)serialization of inventory contents.
        ConfigurationSerialization.registerClass(ItemStack.class);
        ConfigurationSerialization.registerClass(ItemMeta.class);
    }

    @AfterAll
    public static void unload() throws IOException {
        MockBukkit.unmock();
        FileUtils.deleteDirectory(new File("data-storage"));
    }

    /**
     * Minimal concrete {@link BlockMenuPreset} for exercising {@link BlockMenu}/{@link UniversalBlockMenu}
     * construction without needing a registered SlimefunItem.
     */
    private static final class TestBlockMenuPreset extends BlockMenuPreset {

        TestBlockMenuPreset(String id) {
            super(id, "Test Menu", false);
        }

        TestBlockMenuPreset(String id, boolean universal) {
            super(id, "Test Menu", universal);
        }

        @Override
        public void init() {
            setSize(9);
        }

        @Override
        public boolean canOpen(Block b, Player p) {
            return true;
        }

        @Override
        public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
            return new int[0];
        }
    }

    @Test
    void schemaIsCreatedIdempotently() throws Exception {
        JdbcBackend b1 = new JdbcBackend("jdbc:h2:mem:sf_schema;DB_CLOSE_DELAY=-1");
        // Same mem db, second init must not fail.
        JdbcBackend b2 = new JdbcBackend("jdbc:h2:mem:sf_schema;DB_CLOSE_DELAY=-1");

        try (Connection c = DriverManager.getConnection("jdbc:h2:mem:sf_schema;DB_CLOSE_DELAY=-1");
                ResultSet rs = c.getMetaData().getTables(null, null, "BLOCK_DATA", null)) {
            Assertions.assertTrue(rs.next(), "block_data table must exist");
        }

        b1.close();
        b2.close();
    }

    @Test
    void testFlushBlocksWritesAndLoadWorldBlocksRoundTrips() {
        JdbcBackend backend = new JdbcBackend("jdbc:h2:mem:sf_blocks;DB_CLOSE_DELAY=-1");

        try {
            Location location = new Location(world, 1, 2, 3);

            BlockInfoConfig info = new BlockInfoConfig();
            info.setValue("id", "TEST_ITEM");

            Config cfg = new BlockInfoConfig();
            cfg.setValue(BlockStorage.serializeLocation(location), BlockStorage.serializeBlockInfo(info));

            backend.flushBlocks(world, Collections.singletonMap("TEST_ITEM", cfg));

            Map<String, Map<Location, Config>> blocks = backend.loadWorldBlocks(world);
            Assertions.assertTrue(blocks.containsKey("TEST_ITEM"));
            Assertions.assertEquals("TEST_ITEM", blocks.get("TEST_ITEM").get(location).getString("id"));

            // flushBlocks is upsert-only: an empty (fully drained) Config for the same id must
            // NOT delete its row(s) - that would be the mass-delete bug. Deletion only happens
            // via the explicit deleteBlocks() call, exercised below.
            Config emptyCfg = new BlockInfoConfig();
            backend.flushBlocks(world, Collections.singletonMap("TEST_ITEM", emptyCfg));

            Map<String, Map<Location, Config>> stillPresent = backend.loadWorldBlocks(world);
            Assertions.assertTrue(stillPresent.containsKey("TEST_ITEM"), "flushBlocks must not delete rows for an empty Config");

            backend.deleteBlocks(world, Collections.singletonList(location));

            Map<String, Map<Location, Config>> afterDelete = backend.loadWorldBlocks(world);
            Assertions.assertFalse(afterDelete.containsKey("TEST_ITEM"));
        } finally {
            backend.close();
        }
    }

    @Test
    void testDeleteBlocksOnlyRemovesTheTargetedLocation() {
        // Regression test for the H2 mass-delete bug: deleting one location of a sf_id must not
        // touch other locations sharing that same id.
        JdbcBackend backend = new JdbcBackend("jdbc:h2:mem:sf_delete_targeted;DB_CLOSE_DELAY=-1");

        try {
            Location locationA = new Location(world, 10, 20, 30);
            Location locationB = new Location(world, 40, 50, 60);

            BlockInfoConfig infoA = new BlockInfoConfig();
            infoA.setValue("id", "SAME_ID");
            BlockInfoConfig infoB = new BlockInfoConfig();
            infoB.setValue("id", "SAME_ID");

            Config cfg = new BlockInfoConfig();
            cfg.setValue(BlockStorage.serializeLocation(locationA), BlockStorage.serializeBlockInfo(infoA));
            cfg.setValue(BlockStorage.serializeLocation(locationB), BlockStorage.serializeBlockInfo(infoB));

            backend.flushBlocks(world, Collections.singletonMap("SAME_ID", cfg));

            backend.deleteBlocks(world, Collections.singletonList(locationA));

            Map<String, Map<Location, Config>> blocks = backend.loadWorldBlocks(world);
            Assertions.assertTrue(blocks.containsKey("SAME_ID"), "the sf_id must still have a remaining block");
            Assertions.assertNull(blocks.get("SAME_ID").get(locationA), "locationA must be deleted");
            Assertions.assertNotNull(blocks.get("SAME_ID").get(locationB), "locationB must survive the delete");
            Assertions.assertEquals("SAME_ID", blocks.get("SAME_ID").get(locationB).getString("id"));
        } finally {
            backend.close();
        }
    }

    @Test
    void testDeleteBlocksOfLocationWithNoRowIsHarmlessNoOp() {
        JdbcBackend backend = new JdbcBackend("jdbc:h2:mem:sf_delete_noop;DB_CLOSE_DELAY=-1");

        try {
            Location location = new Location(world, 100, 5, 100);

            Assertions.assertDoesNotThrow(() -> backend.deleteBlocks(world, Arrays.asList(location)));

            Map<String, Map<Location, Config>> blocks = backend.loadWorldBlocks(world);
            Assertions.assertTrue(blocks.isEmpty());
        } finally {
            backend.close();
        }
    }

    @Test
    void testFlushChunksWritesAndLoadChunksForWorldRoundTrips() {
        JdbcBackend backend = new JdbcBackend("jdbc:h2:mem:sf_chunks;DB_CLOSE_DELAY=-1");

        try {
            BlockInfoConfig biCfg = new BlockInfoConfig();
            biCfg.setValue("k", "v");

            String chunkKey = world.getName() + ";Chunk;0;0";
            backend.flushChunks(Collections.singletonMap(chunkKey, biCfg));

            Map<String, BlockInfoConfig> chunks = backend.loadChunksForWorld(world);
            Assertions.assertEquals("v", chunks.get(chunkKey).getString("k"));
        } finally {
            backend.close();
        }
    }

    @Test
    void testLoadChunksForWorldFiltersByWorld() {
        JdbcBackend backend = new JdbcBackend("jdbc:h2:mem:sf_chunks_filter;DB_CLOSE_DELAY=-1");

        try {
            World otherWorld = server.createWorld(WorldCreator.name("other-world").environment(Environment.NORMAL));

            BlockInfoConfig biCfg = new BlockInfoConfig();
            biCfg.setValue("k", "v");

            backend.flushChunks(Collections.singletonMap(otherWorld.getName() + ";Chunk;0;0", biCfg));

            Map<String, BlockInfoConfig> chunks = backend.loadChunksForWorld(world);
            Assertions.assertTrue(chunks.isEmpty());
        } finally {
            backend.close();
        }
    }

    @Test
    void testFlushInventoriesWritesAndLoadWorldInventoriesRoundTrips() {
        JdbcBackend backend = new JdbcBackend("jdbc:h2:mem:sf_inventories;DB_CLOSE_DELAY=-1");

        try {
            Location location = new Location(world, 5, 6, 7);
            BlockMenuPreset preset = new TestBlockMenuPreset("JDBC_TEST_BLOCK_PRESET");

            BlockMenu menu = new BlockMenu(preset, location);
            ItemStack item = new ItemStack(Material.DIAMOND, 5);
            menu.replaceExistingItem(0, item);

            backend.flushInventories(Collections.singletonMap(location, menu));

            Map<Location, BlockMenu> loaded = backend.loadWorldInventories(world);
            Assertions.assertTrue(loaded.containsKey(location));
            Assertions.assertEquals(item, loaded.get(location).getItemInSlot(0));

            backend.deleteInventory(location);

            Map<Location, BlockMenu> afterDelete = backend.loadWorldInventories(world);
            Assertions.assertFalse(afterDelete.containsKey(location));
        } finally {
            backend.close();
        }
    }

    @Test
    void testFlushInventoriesSkipsNonDirtyMenus() {
        JdbcBackend backend = new JdbcBackend("jdbc:h2:mem:sf_inventories_nondirty;DB_CLOSE_DELAY=-1");

        try {
            Location location = new Location(world, 8, 9, 10);
            BlockMenuPreset preset = new TestBlockMenuPreset("JDBC_TEST_BLOCK_PRESET_NONDIRTY");

            BlockMenu menu = new BlockMenu(preset, location);
            menu.replaceExistingItem(0, new ItemStack(Material.DIAMOND, 1));

            // BlockMenu.save() (its own file-based save) is the only way to reset isDirty() - there
            // is no public reset API. Calling it here writes a throwaway .sfi file (cleaned up in
            // @AfterAll via FileUtils.deleteDirectory), purely to get the menu into a non-dirty state.
            menu.save(location);
            Assertions.assertFalse(menu.isDirty());

            backend.flushInventories(Collections.singletonMap(location, menu));

            Map<Location, BlockMenu> loaded = backend.loadWorldInventories(world);
            Assertions.assertFalse(loaded.containsKey(location), "a non-dirty menu must not be persisted to H2");
        } finally {
            backend.close();
        }
    }

    @Test
    void testGetMetaAndSetMetaRoundTripAndOverwrite() {
        JdbcBackend backend = new JdbcBackend("jdbc:h2:mem:sf_meta;DB_CLOSE_DELAY=-1");

        try {
            Assertions.assertNull(backend.getMeta("absent"));

            backend.setMeta("k", "v1");
            Assertions.assertEquals("v1", backend.getMeta("k"));

            backend.setMeta("k", "v2");
            Assertions.assertEquals("v2", backend.getMeta("k"));
        } finally {
            backend.close();
        }
    }

    @Test
    void testFlushUniversalInventoriesWritesAndLoadUniversalInventoriesRoundTrips() {
        JdbcBackend backend = new JdbcBackend("jdbc:h2:mem:sf_universal_inventories;DB_CLOSE_DELAY=-1");

        try {
            BlockMenuPreset preset = new TestBlockMenuPreset("JDBC_TEST_UNIVERSAL_PRESET", true);

            // Use the (preset, Config) constructor with an empty in-memory Config so this never
            // touches disk - the single-arg UniversalBlockMenu(preset) constructor calls save().
            io.github.bakedlibs.dough.config.Config emptyCfg = new io.github.bakedlibs.dough.config.Config(new File(""), new YamlConfiguration());
            UniversalBlockMenu menu = new UniversalBlockMenu(preset, emptyCfg);

            ItemStack item = new ItemStack(Material.EMERALD, 3);
            menu.replaceExistingItem(0, item);

            backend.flushUniversalInventories(Collections.singletonMap(preset.getID(), menu));

            Map<String, UniversalBlockMenu> loaded = backend.loadUniversalInventories();
            Assertions.assertTrue(loaded.containsKey(preset.getID()));
            Assertions.assertEquals(item, loaded.get(preset.getID()).getItemInSlot(0));
        } finally {
            backend.close();
        }
    }
}
