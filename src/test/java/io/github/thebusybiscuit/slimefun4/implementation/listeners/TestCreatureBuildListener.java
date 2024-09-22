package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.entity.CreatureBuildListener;
import io.github.thebusybiscuit.slimefun4.utils.blockpattern.TShapedBlockPattern;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.LivingEntityMock;
import be.seeseemelk.mockbukkit.entity.SimpleMobMock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TestCreatureBuildListener {

    private static ServerMock server;
    private static Slimefun plugin;

    private static CreatureBuildListener listener;

    @BeforeAll
    public static void load() {
        setupFiles();
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
        listener = new CreatureBuildListener(plugin);
    }

    @AfterAll
    public static void unload() {
        teardownFiles();
        MockBukkit.unmock();
    }

    private static void setupFiles() {
        // Create these files so BlockStorage doesn't complain
        File universalInventories = new File("data-storage/Slimefun/universal-inventories");
        File storedInventories = new File("data-storage/Slimefun/stored-inventories");
        universalInventories.mkdirs();
        storedInventories.mkdirs();
    }

    private static void teardownFiles() {
        // Delete these files after we are done
        File parent = new File("data-storage");
        try (Stream<Path> stream = Files.walk(parent.toPath())) {
            stream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private CreatureSpawnEvent mockCreatureBuildEvent(CreatureSpawnEvent.SpawnReason spawnReason) {
        WorldMock world = new WorldMock();
        Location location = new Location(world, 0, 0, 0);
        EntityType entityType = switch (spawnReason) {
            case BUILD_SNOWMAN -> EntityType.SNOWMAN;
            case BUILD_IRONGOLEM -> EntityType.IRON_GOLEM;
            case BUILD_WITHER -> EntityType.WITHER;
            default ->
                    throw new IllegalArgumentException("Cannot mock creature building for spawn reason: " + spawnReason);
        };
        LivingEntityMock livingEntity = new SimpleMobMock(server) {
            @Override
            public @Nonnull EntityType getType() {
                return entityType;
            }
        };
        livingEntity.setLocation(location);
        return new CreatureSpawnEvent(livingEntity, spawnReason);
    }

    private void mockBlockStorageEntry(Collection<Block> blocks) {
        if (blocks.isEmpty()) {
            return;
        }
        Optional<Block> optional = blocks.stream().findAny();
        Block selectedBlock = optional.get();
        World world = selectedBlock.getWorld();
        // Init a BlockStorage impl for this world so BlockStorage doesn't complain of fake worlds
        BlockStorage.getOrCreate(world);
        BlockStorage.addBlockInfo(selectedBlock.getLocation(), "id", "slimefun:test");
    }

    @Test
    void testNonBuildingCreatureSpawnEvents() {
        Set<CreatureSpawnEvent.SpawnReason> spawnReasons = EnumSet.allOf(CreatureSpawnEvent.SpawnReason.class);
        spawnReasons.remove(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM);
        spawnReasons.remove(CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN);
        spawnReasons.remove(CreatureSpawnEvent.SpawnReason.BUILD_WITHER);
        for (CreatureSpawnEvent.SpawnReason spawnReason : spawnReasons) {
            // The spawnee can be null here, we only care about the spawn reason
            CreatureSpawnEvent creatureBuiltEvent = new CreatureSpawnEvent(null, spawnReason);
            listener.onCreatureSpawn(creatureBuiltEvent);
            Assertions.assertFalse(creatureBuiltEvent.isCancelled());
        }
    }

    @Test
    void testBuildIronGolem() {
        CreatureSpawnEvent spawnEvent = mockCreatureBuildEvent(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM);
        Collection<Block> ironGolem = new ArrayList<>(TShapedBlockPattern.getTShapeEastWest(spawnEvent.getLocation(), false));
        ironGolem.forEach(block -> block.setType(Material.IRON_BLOCK));
        Block carvedPumpkin = spawnEvent.getLocation().getBlock().getRelative(BlockFace.UP, 2);
        carvedPumpkin.setType(Material.CARVED_PUMPKIN);
        ironGolem.add(carvedPumpkin);

        listener.onCreatureSpawn(spawnEvent);
        // Valid iron golem was built
        Assertions.assertFalse(spawnEvent.isCancelled());

        mockBlockStorageEntry(ironGolem);
        listener.onCreatureSpawn(spawnEvent);
        Assertions.assertTrue(spawnEvent.isCancelled());
    }

    @Test
    void testBuildIronGolemInverted() {
        CreatureSpawnEvent spawnEvent = mockCreatureBuildEvent(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM);
        Collection<Block> ironGolem = new ArrayList<>(TShapedBlockPattern.getTShapeEastWest(spawnEvent.getLocation(), true));
        ironGolem.forEach(block -> block.setType(Material.IRON_BLOCK));
        Block carvedPumpkin = spawnEvent.getLocation().getBlock().getRelative(BlockFace.UP, 1);
        carvedPumpkin.setType(Material.CARVED_PUMPKIN);
        ironGolem.add(carvedPumpkin);

        listener.onCreatureSpawn(spawnEvent);
        // Valid iron golem was built
        Assertions.assertFalse(spawnEvent.isCancelled());

        mockBlockStorageEntry(ironGolem);
        listener.onCreatureSpawn(spawnEvent);
        Assertions.assertTrue(spawnEvent.isCancelled());
    }


    @Test
    void testBuildSnowman() {
        CreatureSpawnEvent spawnEvent = mockCreatureBuildEvent(CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN);
        Block base = spawnEvent.getLocation().getBlock();
        List<Block> blocks = List.of(base, base.getRelative(BlockFace.UP), base.getRelative(BlockFace.UP, 2));
        base.setType(Material.SNOW_BLOCK);
        blocks.get(1).setType(Material.SNOW_BLOCK);
        blocks.get(2).setType(Material.CARVED_PUMPKIN);

        listener.onCreatureSpawn(spawnEvent);
        // Valid snowman was built
        Assertions.assertFalse(spawnEvent.isCancelled());

        mockBlockStorageEntry(blocks);
        listener.onCreatureSpawn(spawnEvent);
        Assertions.assertTrue(spawnEvent.isCancelled());
    }

    @Test
    void testBuildWither() {
        for (Material material : Tag.WITHER_SUMMON_BASE_BLOCKS.getValues()) {
            testBuildWither(material);
        }
    }

    private void testBuildWither(Material baseMaterial) {
        CreatureSpawnEvent spawnEvent = mockCreatureBuildEvent(CreatureSpawnEvent.SpawnReason.BUILD_WITHER);
        Block base = spawnEvent.getLocation().getBlock();
        Collection<Block> soulSand = TShapedBlockPattern.getTShapeEastWest(base.getLocation(), false);
        soulSand.forEach(block -> block.setType(baseMaterial));
        Collection<Block> witherSkulls = TShapedBlockPattern.getLineEastWest(base.getRelative(BlockFace.UP, 2));
        witherSkulls.forEach(block -> block.setType(Material.WITHER_SKELETON_SKULL));
        Collection<Block> wither = new ArrayList<>(soulSand);
        wither.addAll(witherSkulls);

        // Valid wither was built
        listener.onCreatureSpawn(spawnEvent);
        Assertions.assertFalse(spawnEvent.isCancelled());

        mockBlockStorageEntry(wither);
        listener.onCreatureSpawn(spawnEvent);
        Assertions.assertTrue(spawnEvent.isCancelled());
    }

    @Test
    void testBuildWitherInverted() {
        for (Material material : Tag.WITHER_SUMMON_BASE_BLOCKS.getValues()) {
            testBuildWitherInverted(material);
        }
    }


    private void testBuildWitherInverted(Material baseMaterial) {
        CreatureSpawnEvent spawnEvent = mockCreatureBuildEvent(CreatureSpawnEvent.SpawnReason.BUILD_WITHER);
        Block base = spawnEvent.getLocation().getBlock();
        // Because inverted, the skulls will be on the bottom layer
        Location baseLocation = base.getLocation().clone().add(0, 1, 0);
        Collection<Block> baseBlocks = TShapedBlockPattern.getTShapeEastWest(baseLocation, true);
        baseBlocks.forEach(block -> block.setType(baseMaterial));
        Collection<Block> witherSkulls = TShapedBlockPattern.getLineEastWest(base);
        witherSkulls.forEach(block -> block.setType(Material.WITHER_SKELETON_SKULL));
        Collection<Block> wither = new ArrayList<>(baseBlocks);
        wither.addAll(witherSkulls);

        // Valid wither was built
        listener.onCreatureSpawn(spawnEvent);
        Assertions.assertFalse(spawnEvent.isCancelled());

        mockBlockStorageEntry(wither);
        listener.onCreatureSpawn(spawnEvent);
        Assertions.assertTrue(spawnEvent.isCancelled());
    }

}
