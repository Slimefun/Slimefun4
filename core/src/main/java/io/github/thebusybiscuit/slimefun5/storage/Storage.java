package io.github.thebusybiscuit.slimefun5.storage;

import io.github.thebusybiscuit.slimefun5.storage.data.PlayerData;

import javax.annotation.concurrent.ThreadSafe;

import com.google.common.annotations.Beta;

import java.util.UUID;

/**
 * The {@link Storage} interface is the abstract layer on top of our storage backends.
 * Every backend has to implement this interface and has to implement it in a thread-safe way.
 * There will be no expectation of running functions in here within the main thread.
 *
 * <p>
 * <b>This API is still experimental, it may change without notice.</b>
 */
@Beta
@ThreadSafe
public interface Storage {

    PlayerData loadPlayerData(UUID uuid);

    void savePlayerData(UUID uuid, PlayerData data);

    /**
     * Releases any resources held by this backend (e.g. a JDBC connection). Called once on plugin
     * disable. The flat-file backend holds nothing, so it defaults to a no-op.
     */
    default void close() {
        // Flat-file backend needs no teardown; DB backends override this.
    }
}

