package io.github.thebusybiscuit.slimefun5.storage.backend;

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

    void flushInventories(@Nonnull Map<Location, BlockMenu> dirtyInventories);

    void flushUniversalInventories(@Nonnull Map<String, UniversalBlockMenu> universalInventories);

    void flushChunks(@Nonnull Map<String, BlockInfoConfig> chunks);

    void deleteInventory(@Nonnull Location l);

    default void close() {
        // Flat-file backend needs no teardown; DB backends override this.
    }
}
