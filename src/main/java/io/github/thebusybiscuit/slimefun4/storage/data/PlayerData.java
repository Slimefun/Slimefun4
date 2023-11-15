package io.github.thebusybiscuit.slimefun4.storage.data;

import com.google.common.annotations.Beta;
import org.bukkit.NamespacedKey;

import java.util.HashSet;
import java.util.Set;

/**
 * The data which backs {@link io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile}
 *
 * <b>This API is still experimental, it may change without notice.</b>
 */
@Beta
public class PlayerData {

    private final Set<NamespacedKey> researches = new HashSet<>();

    public PlayerData(Set<NamespacedKey> researches) {
        this.researches.addAll(researches);
    }

    public Set<NamespacedKey> getResearches() {
        return researches;
    }
}
