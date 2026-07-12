package io.github.thebusybiscuit.slimefun5.storage.backend.legacy;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
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

class LegacyFileBackendTest {

    private static ServerMock server;
    private static World world;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
        world = server.createWorld(WorldCreator.name("world").environment(Environment.NORMAL));
    }

    @AfterAll
    public static void unload() throws IOException {
        MockBukkit.unmock();
        FileUtils.deleteDirectory(new File("data-storage"));
    }

    @Test
    void testLoadWorldBlocks() {
        Location location = new Location(world, 100, 64, 100);

        BlockInfoConfig info = new BlockInfoConfig();
        info.setValue("id", "TEST_ITEM");

        File blocksDir = new File(BlockStorage.PATH_BLOCKS + world.getName());
        blocksDir.mkdirs();
        Config blockFile = new Config(new File(blocksDir, "TEST_ITEM.sfb"));
        blockFile.setValue(BlockStorage.serializeLocation(location), BlockStorage.serializeBlockInfo(info));
        blockFile.save();

        LegacyFileBackend backend = new LegacyFileBackend();
        Map<String, Map<Location, Config>> blocks = backend.loadWorldBlocks(world);

        Assertions.assertTrue(blocks.containsKey("TEST_ITEM"));
        Assertions.assertEquals("TEST_ITEM", blocks.get("TEST_ITEM").values().iterator().next().getString("id"));
    }

    @Test
    void testLoadChunksForWorld() {
        File chunksDir = new File(BlockStorage.PATH_CHUNKS);
        chunksDir.mkdirs();
        Config chunksFile = new Config(new File(chunksDir, "chunks.sfc"));
        chunksFile.setValue(world.getName() + ";Chunk;0;0", "{\"k\":\"v\"}");
        chunksFile.save();

        LegacyFileBackend backend = new LegacyFileBackend();
        Map<String, BlockInfoConfig> chunks = backend.loadChunksForWorld(world);

        Assertions.assertEquals("v", chunks.get(world.getName() + ";Chunk;0;0").getString("k"));
    }

    @Test
    void testFlushBlocksWritesAndRoundTrips() {
        Location location = new Location(world, 200, 64, 200);

        BlockInfoConfig info = new BlockInfoConfig();
        info.setValue("id", "TEST_FLUSH_ITEM");

        File sfbFile = new File(BlockStorage.PATH_BLOCKS + world.getName() + "/TEST_FLUSH_ITEM.sfb");
        Config cfg = new Config(sfbFile, new YamlConfiguration());
        cfg.setValue(BlockStorage.serializeLocation(location), BlockStorage.serializeBlockInfo(info));

        LegacyFileBackend backend = new LegacyFileBackend();
        backend.flushBlocks(world, Collections.singletonMap("TEST_FLUSH_ITEM", cfg));

        Assertions.assertTrue(sfbFile.exists());

        Map<String, Map<Location, Config>> blocks = backend.loadWorldBlocks(world);
        Assertions.assertTrue(blocks.containsKey("TEST_FLUSH_ITEM"));
        Assertions.assertEquals("TEST_FLUSH_ITEM", blocks.get("TEST_FLUSH_ITEM").values().iterator().next().getString("id"));

        // An empty (fully drained) Config for the same id must delete the .sfb instead of writing it
        Config emptyCfg = new Config(sfbFile, new YamlConfiguration());
        backend.flushBlocks(world, Collections.singletonMap("TEST_FLUSH_ITEM", emptyCfg));

        Assertions.assertFalse(sfbFile.exists());
    }

    @Test
    void testFlushChunksRoundTrips() {
        BlockInfoConfig biCfg = new BlockInfoConfig();
        biCfg.setValue("k", "v");

        LegacyFileBackend backend = new LegacyFileBackend();
        backend.flushChunks(Collections.singletonMap(world.getName() + ";Chunk;0;0", biCfg));

        Map<String, BlockInfoConfig> chunks = backend.loadChunksForWorld(world);
        Assertions.assertEquals("v", chunks.get(world.getName() + ";Chunk;0;0").getString("k"));
    }
}
