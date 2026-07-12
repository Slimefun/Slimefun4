package io.github.thebusybiscuit.slimefun5.storage.backend.legacy;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.annotations.Beta;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.storage.backend.BlockStorageBackend;
import io.github.thebusybiscuit.slimefun5.utils.NumberUtils;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockInfoConfig;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

/**
 * File-based (legacy) implementation of {@link BlockStorageBackend}. Reads the same
 * {@code .sfb}/{@code .sfc}/{@code .sfi} files and uses the exact same parsing helpers as the
 * pre-SP-1 {@link BlockStorage}, so the on-disk format is unchanged.
 *
 * <p>
 * <b>This API is still experimental, it may change without notice.</b>
 */
@Beta
public class LegacyFileBackend implements BlockStorageBackend {

    @Override
    @Nonnull
    public Map<String, Map<Location, Config>> loadWorldBlocks(@Nonnull World world) {
        Map<String, Map<Location, Config>> result = new HashMap<>();
        File dir = new File(BlockStorage.PATH_BLOCKS + world.getName());

        if (!dir.exists()) {
            dir.mkdirs();
            return result;
        }

        File[] files = dir.listFiles();
        long total = files.length;
        long start = System.currentTimeMillis();
        long done = 0;
        long timestamp = System.currentTimeMillis();
        long totalBlocks = 0;
        int delay = Slimefun.getCfg().getInt("URID.info-delay");

        try {
            for (File file : files) {
                if (file.getName().equals("null.sfb")) {
                    Slimefun.logger().log(Level.WARNING, "File with corrupted blocks detected!");
                    Slimefun.logger().log(Level.WARNING, "Slimefun will simply skip this File, you should look inside though!");
                    Slimefun.logger().log(Level.WARNING, file.getPath());
                } else if (file.getName().endsWith(".sfb")) {
                    if (timestamp + delay < System.currentTimeMillis()) {
                        int progress = Math.round((((done * 100.0F) / total) * 100.0F) / 100.0F);
                        Slimefun.logger().log(Level.INFO, "Loading Blocks... {0}% done (\"{1}\")", new Object[] { progress, world.getName() });
                        timestamp = System.currentTimeMillis();
                    }

                    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                    String fileId = file.getName().replace(".sfb", "");
                    Map<Location, Config> parsed = new HashMap<>();

                    for (String key : cfg.getKeys(false)) {
                        totalBlocks++;
                        loadBlock(file, cfg, key, parsed);
                    }

                    result.put(fileId, parsed);
                    done++;
                }
            }
        } finally {
            long time = (System.currentTimeMillis() - start);
            Slimefun.logger().log(Level.INFO, "Loading Blocks... 100% (FINISHED - {0}ms)", time);
            Slimefun.logger().log(Level.INFO, "Loaded a total of {0} Blocks for World \"{1}\"", new Object[] { totalBlocks, world.getName() });

            if (totalBlocks > 0) {
                Slimefun.logger().log(Level.INFO, "Avg: {0}ms/Block", NumberUtils.roundDecimalNumber((double) time / (double) totalBlocks));
            }
        }

        return result;
    }

    private void loadBlock(@Nonnull File file, @Nonnull FileConfiguration cfg, @Nonnull String key, @Nonnull Map<Location, Config> parsed) {
        Location l = BlockStorage.deserializeLocation(key);

        if (l == null) {
            // That location was malformed, we will skip this one
            return;
        }

        try {
            String json = cfg.getString(key);
            Config blockInfo = BlockStorage.parseBlockInfo(l, json);

            if (blockInfo != null && blockInfo.contains("id")) {
                parsed.put(l, blockInfo);
            }
        } catch (Exception x) {
            Slimefun.logger().log(Level.WARNING, x, () -> "Failed to load " + file.getName() + '(' + key + ") for Slimefun " + Slimefun.getVersion());
        }
    }

    @Override
    @Nonnull
    public Map<String, BlockInfoConfig> loadChunksForWorld(@Nonnull World world) {
        Map<String, BlockInfoConfig> result = new HashMap<>();
        File chunks = new File(BlockStorage.PATH_CHUNKS + "chunks.sfc");

        if (chunks.exists()) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(chunks);

            for (String key : cfg.getKeys(false)) {
                try {
                    if (world.getName().equals(CommonPatterns.SEMICOLON.split(key)[0])) {
                        BlockInfoConfig data = new BlockInfoConfig(BlockStorage.parseJSON(cfg.getString(key)));
                        result.put(key, data);
                    }
                } catch (Exception x) {
                    Slimefun.logger().log(Level.WARNING, x, () -> "Failed to load " + chunks.getName() + " in World " + world.getName() + '(' + key + ") for Slimefun " + Slimefun.getVersion());
                }
            }
        }

        return result;
    }

    @Override
    @Nonnull
    public Map<Location, BlockMenu> loadWorldInventories(@Nonnull World world) {
        Map<Location, BlockMenu> result = new HashMap<>();

        for (File file : new File(BlockStorage.PATH_INVENTORIES).listFiles()) {
            if (file.getName().startsWith(world.getName()) && file.getName().endsWith(".sfi")) {
                try {
                    Location l = BlockStorage.deserializeLocation(file.getName().replace(".sfi", ""));

                    // We only want to only load this world's menus
                    if (world != l.getWorld()) {
                        continue;
                    }

                    io.github.bakedlibs.dough.config.Config cfg = new io.github.bakedlibs.dough.config.Config(file);
                    BlockMenuPreset preset = BlockMenuPreset.getPreset(cfg.getString("preset"));

                    if (preset == null) {
                        preset = BlockMenuPreset.getPreset(BlockStorage.checkID(l));
                    }

                    if (preset != null) {
                        result.put(l, new BlockMenu(preset, l, cfg));
                    }
                } catch (Exception x) {
                    Slimefun.logger().log(Level.SEVERE, x, () -> "An Error occurred while loading this Block Inventory: " + file.getName());
                }
            }
        }

        return result;
    }

    @Override
    @Nonnull
    public Map<String, UniversalBlockMenu> loadUniversalInventories() {
        Map<String, UniversalBlockMenu> result = new HashMap<>();

        for (File file : new File(BlockStorage.PATH_UNIVERSAL_INVENTORIES).listFiles()) {
            if (file.getName().endsWith(".sfi")) {
                try {
                    io.github.bakedlibs.dough.config.Config cfg = new io.github.bakedlibs.dough.config.Config(file);
                    BlockMenuPreset preset = BlockMenuPreset.getPreset(cfg.getString("preset"));

                    if (preset != null) {
                        result.put(preset.getID(), new UniversalBlockMenu(preset, cfg));
                    }
                } catch (Exception x) {
                    Slimefun.logger().log(Level.SEVERE, x, () -> "An Error occurred while loading this universal Inventory: " + file.getName());
                }
            }
        }

        return result;
    }

    @Override
    @Nullable
    public BlockMenu loadInventoryIfPresent(@Nonnull Location l, @Nonnull BlockMenuPreset preset) {
        File file = new File(BlockStorage.PATH_INVENTORIES + BlockStorage.serializeLocation(l) + ".sfi");

        if (file.exists()) {
            return new BlockMenu(preset, l, new io.github.bakedlibs.dough.config.Config(file));
        }

        return null;
    }

    @Override
    public void flushWorldBlocks(@Nonnull World world, @Nonnull Map<String, Config> blocksCache) {
        throw new UnsupportedOperationException("implemented in SP-1 Task 2");
    }

    @Override
    public void flushChunks(@Nonnull Map<String, BlockInfoConfig> chunks) {
        throw new UnsupportedOperationException("implemented in SP-1 Task 2");
    }

    @Override
    public void deleteInventory(@Nonnull Location l) {
        throw new UnsupportedOperationException("implemented in SP-1 Task 2");
    }
}
