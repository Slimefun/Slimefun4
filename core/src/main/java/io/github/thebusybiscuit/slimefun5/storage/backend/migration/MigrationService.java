package io.github.thebusybiscuit.slimefun5.storage.backend.migration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.annotations.Beta;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.storage.backend.jdbc.JdbcBackend;
import io.github.thebusybiscuit.slimefun5.storage.backend.legacy.LegacyFileBackend;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

/**
 * One-time, per-world migration of the flat-file ({@code .sfb}/{@code .sfc}/{@code .sfi}) block
 * storage into a {@link JdbcBackend}. Idempotent via {@code storage_meta} flags: once a world (or
 * the universal inventories) has been migrated, the flat data is renamed to a
 * {@code .migrated-backup-<bootTimestamp>} sibling and never touched again.
 *
 * <p>
 * Any failure during a migration leaves the flat files completely intact (no flag, no rename) so
 * the next boot simply retries - zero data loss on a partial/failed run.
 *
 * <p>
 * <b>This API is still experimental, it may change without notice.</b>
 */
@Beta
public class MigrationService {

    private static final String VERSION = "1";

    private final JdbcBackend jdbc;
    private final LegacyFileBackend legacy = new LegacyFileBackend();
    private final long ts;

    public MigrationService(@Nonnull JdbcBackend jdbc, long bootTimestamp) {
        this.jdbc = jdbc;
        this.ts = bootTimestamp;
    }

    public void migrateWorldIfNeeded(@Nonnull World world) {
        String flag = "migrated.world." + world.getName();

        if (jdbc.getMeta(flag) != null) {
            return;
        }

        File blockDir = new File(BlockStorage.PATH_BLOCKS + world.getName());
        boolean hasFlat = blockDir.isDirectory() || hasWorldInventoryFiles(world) || hasChunkEntryForWorld(world);

        if (!hasFlat) {
            // Nothing to migrate for this world - mark it done so we don't re-check every boot.
            jdbc.setMeta(flag, VERSION);
            return;
        }

        try {
            Map<String, Map<Location, Config>> loaded = legacy.loadWorldBlocks(world);
            Map<String, Config> flushShape = new HashMap<>();

            for (Map.Entry<String, Map<Location, Config>> e : loaded.entrySet()) {
                // Same construction BlockStorage.refreshCache uses for its blocksCache entries: a
                // File-backed Config that is never save()d, so it stays a pure in-memory holder.
                Config idCfg = new Config(BlockStorage.PATH_BLOCKS + world.getName() + '/' + e.getKey() + ".sfb");

                for (Map.Entry<Location, Config> le : e.getValue().entrySet()) {
                    idCfg.setValue(BlockStorage.serializeLocation(le.getKey()), BlockStorage.serializeBlockInfo(le.getValue()));
                }

                flushShape.put(e.getKey(), idCfg);
            }

            jdbc.flushBlocksOrThrow(world, flushShape);
            jdbc.flushChunksOrThrow(legacy.loadChunksForWorld(world));

            Map<Location, BlockMenu> inventories = hasWorldInventoryFiles(world)
                    ? legacy.loadWorldInventories(world)
                    : Collections.<Location, BlockMenu>emptyMap();
            jdbc.flushInventoriesOrThrow(inventories);

            // Only after every flush succeeded: mark migrated, then back up the flat pieces.
            jdbc.setMeta(flag, VERSION);
            renameToBackup(blockDir, backupSiblingOf(BlockStorage.PATH_BLOCKS + world.getName()));
            backupWorldSfiFiles(world);

            int blockIdCount = flushShape.size();
            Slimefun.logger().log(Level.INFO,
                    "[storage] Migrated world \"{0}\" to the database ({1} block ids). Flat backup: *.migrated-backup-{2}",
                    new Object[] { world.getName(), blockIdCount, ts });
        } catch (Exception | LinkageError x) {
            Slimefun.logger().log(Level.SEVERE, x, () -> "[storage] Migration FAILED for world \"" + world.getName() + "\" - flat files kept intact, will retry next boot");
            // Do NOT set the flag, do NOT rename anything - retried next boot, no data loss.
        }
    }

    public void migrateUniversalIfNeeded() {
        if (jdbc.getMeta("migrated.universal") != null) {
            return;
        }

        File dir = new File(BlockStorage.PATH_UNIVERSAL_INVENTORIES);

        if (!dir.isDirectory()) {
            jdbc.setMeta("migrated.universal", VERSION);
            return;
        }

        try {
            Map<String, UniversalBlockMenu> uni = legacy.loadUniversalInventories();
            jdbc.flushUniversalInventoriesOrThrow(uni);

            jdbc.setMeta("migrated.universal", VERSION);
            renameToBackup(dir, backupSiblingOf(BlockStorage.PATH_UNIVERSAL_INVENTORIES));

            Slimefun.logger().log(Level.INFO,
                    "[storage] Migrated universal inventories to the database ({0} presets). Flat backup: *.migrated-backup-{1}",
                    new Object[] { uni.size(), ts });
        } catch (Exception | LinkageError x) {
            Slimefun.logger().log(Level.SEVERE, x, () -> "[storage] Universal-inventory migration FAILED - kept intact");
        }
    }

    @Nonnull
    private File backupSiblingOf(@Nonnull String path) {
        String trimmed = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        return new File(trimmed + ".migrated-backup-" + ts);
    }

    private void renameToBackup(@Nonnull File src, @Nonnull File dst) {
        if (!src.exists()) {
            return;
        }

        try {
            Files.move(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Slimefun.logger().log(Level.INFO, "[storage] Backed up \"{0}\" to \"{1}\"", new Object[] { src.getPath(), dst.getPath() });
        } catch (IOException e) {
            // Never delete flat data - if the rename itself fails, just leave it where it was.
            Slimefun.logger().log(Level.SEVERE, e, () -> "[storage] Could not back up \"" + src.getPath() + "\" - flat data left in place");
        }
    }

    private void backupWorldSfiFiles(@Nonnull World world) {
        File dir = new File(BlockStorage.PATH_INVENTORIES);
        File[] files = dir.listFiles();

        if (files == null) {
            return;
        }

        String prefix = world.getName() + ";";
        File backupDir = null;

        for (File file : files) {
            if (!file.getName().startsWith(prefix) || !file.getName().endsWith(".sfi")) {
                continue;
            }

            if (backupDir == null) {
                backupDir = backupSiblingOf(BlockStorage.PATH_INVENTORIES);
                backupDir.mkdirs();
            }

            try {
                Files.move(file.toPath(), new File(backupDir, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                Slimefun.logger().log(Level.SEVERE, e, () -> "[storage] Could not back up flat inventory file \"" + file.getName() + '"');
            }
        }
    }

    private boolean hasWorldInventoryFiles(@Nonnull World world) {
        File[] files = new File(BlockStorage.PATH_INVENTORIES).listFiles();

        if (files == null) {
            return false;
        }

        String prefix = world.getName() + ";";

        for (File file : files) {
            if (file.getName().startsWith(prefix) && file.getName().endsWith(".sfi")) {
                return true;
            }
        }

        return false;
    }

    private boolean hasChunkEntryForWorld(@Nonnull World world) {
        File chunks = new File(BlockStorage.PATH_CHUNKS + "chunks.sfc");

        if (!chunks.exists()) {
            return false;
        }

        FileConfiguration cfg = YamlConfiguration.loadConfiguration(chunks);
        String prefix = world.getName() + ";";

        for (String key : cfg.getKeys(false)) {
            if (key.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }
}
