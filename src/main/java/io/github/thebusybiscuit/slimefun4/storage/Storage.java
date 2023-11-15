package io.github.thebusybiscuit.slimefun4.storage;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.storage.data.PlayerData;

import javax.annotation.concurrent.ThreadSafe;
import java.util.UUID;

/**
 * TODO
 *
 */
@ThreadSafe
public interface Storage {

    PlayerData loadPlayerData(UUID uuid);

    void savePlayerData(UUID uuid, PlayerData data);
}
