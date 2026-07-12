package io.github.thebusybiscuit.slimefun5.storage.backend;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.common.annotations.Beta;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockInfoConfig;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

/**
 * The {@link BlockStorageBackend} interface is the abstract layer on top of our block storage
 * backends (blocks, chunks and inventories). Every backend has to implement this interface.
 *
 * <p>
 * <b>This API is still experimental, it may change without notice.</b>
 */
@Beta
public interface BlockStorageBackend {

    @Nonnull
    Map<String, Map<Location, Config>> loadWorldBlocks(@Nonnull World world);

    @Nonnull
    Map<String, BlockInfoConfig> loadChunksForWorld(@Nonnull World world);

    @Nonnull
    Map<Location, BlockMenu> loadWorldInventories(@Nonnull World world);

    @Nonnull
    Map<String, UniversalBlockMenu> loadUniversalInventories();

    @Nullable
    BlockMenu loadInventoryIfPresent(@Nonnull Location l, @Nonnull BlockMenuPreset preset);

    void flushBlocks(@Nonnull World world, @Nonnull Map<String, Config> blocksCache);

    /**
     * Explicitly deletes the block rows at the given {@link Location}s. The dirty per-id
     * {@link Config} objects passed to {@link #flushBlocks(World, Map)} are delta-only (they
     * don't carry the "full known state" the legacy {@code .sfb} file did), so a removed location
     * is simply absent from them - indistinguishable from "not touched". This method is the
     * explicit signal backends need to actually delete a row.
     *
     * @param world
     *            The {@link World} the locations belong to.
     * @param locations
     *            The {@link Location}s to delete.
     */
    void deleteBlocks(@Nonnull World world, @Nonnull Collection<Location> locations);

    void flushInventories(@Nonnull Map<Location, BlockMenu> dirtyInventories);

    void flushUniversalInventories(@Nonnull Map<String, UniversalBlockMenu> universalInventories);

    void flushChunks(@Nonnull Map<String, BlockInfoConfig> chunks);

    void deleteInventory(@Nonnull Location l);

    default void close() {
        // Flat-file backend needs no teardown; DB backends override this.
    }
}
