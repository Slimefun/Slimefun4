package io.github.thebusybiscuit.slimefun4.api.accessibility;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Represents a data object representing the various {@link AccessLevel}
 * each {@link Player} will have. All the inherited methods from {@link AccessData<UUID>}
 * are designed to be usable with {@link Player#getUniqueId()}
 *
 * @author md5sha256
 */
public interface PlayerAccessData extends AccessData<UUID> {

    @Nonnull
    default AccessLevel getAccessLevel(@Nonnull Player object) {
        return getAccessLevel(object.getUniqueId());
    }

    default boolean setAccessLevel(@Nonnull Player object, @Nonnull AccessLevel newLevel) {
        return setAccessLevel(object.getUniqueId(), newLevel);
    }

    @Nonnull
    default AccessLevel decrementAccessLevel(@Nonnull Player player) {
        return decrementAccessLevel(player.getUniqueId());
    }

    @Nonnull
    default AccessLevel incrementAccessLevel(@Nonnull Player player) {
        return incrementAccessLevel(player.getUniqueId());
    }


}
