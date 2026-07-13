package io.github.thebusybiscuit.slimefun5.storage.backend.migration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
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
import io.github.thebusybiscuit.slimefun5.storage.backend.jdbc.JdbcBackend;
import io.github.thebusybiscuit.slimefun5.utils.FileUtils;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockInfoConfig;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

class MigrationServiceTest {

    private static ServerMock server;
    private static World world;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
        world = server.createWorld(WorldCreator.name("migration-world").environment(Environment.NORMAL));

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
     * Minimal concrete {@link BlockMenuPreset} for exercising {@link BlockMenu} construction
     * without needing a registered SlimefunItem.
     */
    private static final class TestBlockMenuPreset extends BlockMenuPreset {

        TestBlockMenuPreset(String id) {
            super(id, "Test Menu", false);
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
    void testMigrateWorldIfNeededMovesFlatDataIntoH2WithBackupAndIsIdempotent() {
        // Seed one block: stored-blocks/<world>/TEST.sfb
        Location blockLocation = new Location(world, 10, 64, 10);

        BlockInfoConfig info = new BlockInfoConfig();
        info.setValue("id", "TEST");

        File blocksDir = new File(BlockStorage.PATH_BLOCKS + world.getName());
        blocksDir.mkdirs();
        Config blockFile = new Config(new File(blocksDir, "TEST.sfb"));
        blockFile.setValue(BlockStorage.serializeLocation(blockLocation), BlockStorage.serializeBlockInfo(info));
        blockFile.save();

        // Seed one chunk entry: stored-chunks/chunks.sfc
        File chunksDir = new File(BlockStorage.PATH_CHUNKS);
        chunksDir.mkdirs();
        Config chunksFile = new Config(new File(chunksDir, "chunks.sfc"));
        chunksFile.setValue(world.getName() + ";Chunk;0;0", "{\"k\":\"v\"}");
        chunksFile.save();

        // Seed one inventory: stored-inventories/<world>;x;y;z.sfi
        Location invLocation = new Location(world, 20, 64, 20);
        BlockMenuPreset preset = new TestBlockMenuPreset("MIGRATION_TEST_PRESET");
        BlockMenu menu = new BlockMenu(preset, invLocation);
        menu.replaceExistingItem(0, new ItemStack(Material.DIAMOND, 1));
        menu.save(invLocation);

        JdbcBackend jdbc = new JdbcBackend("jdbc:h2:mem:sf_migration;DB_CLOSE_DELAY=-1");

        try {
            MigrationService migration = new MigrationService(jdbc, 12345L);

            migration.migrateWorldIfNeeded(world);

            Map<String, Map<Location, Config>> blocks = jdbc.loadWorldBlocks(world);
            Assertions.assertTrue(blocks.containsKey("TEST"), "the migrated block id must be present in H2");
            Assertions.assertEquals("TEST", blocks.get("TEST").get(blockLocation).getString("id"));

            File backupDir = new File(BlockStorage.PATH_BLOCKS + world.getName() + ".migrated-backup-12345");
            Assertions.assertTrue(backupDir.isDirectory(), "flat block dir must be renamed to the backup sibling");
            Assertions.assertFalse(blocksDir.exists(), "the original flat block dir must be gone");

            Assertions.assertNotNull(jdbc.getMeta("migrated.world." + world.getName()), "the per-world migration flag must be set");

            // Second call: the flat dir is gone and the flag is set - must be a silent no-op.
            Assertions.assertDoesNotThrow(() -> migration.migrateWorldIfNeeded(world));
        } finally {
            jdbc.close();
        }
    }
}
