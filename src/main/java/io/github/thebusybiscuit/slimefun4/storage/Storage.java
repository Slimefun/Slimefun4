package io.github.thebusybiscuit.slimefun4.storage;

import io.github.thebusybiscuit.slimefun4.storage.data.PlayerData;

import javax.annotation.concurrent.ThreadSafe;
import java.util.UUID;

/**
 * The {@link Storage} interface is the abstract layer on top of our storage backends.
 * Every backend has to implement this interface and has to implement it in a thread-safe way.
 * There will be no expectation of running functions in here within the main thread.
 */
@ThreadSafe
public interface Storage {

    PlayerData loadPlayerData(UUID uuid);

    void savePlayerData(UUID uuid, PlayerData data);
}
