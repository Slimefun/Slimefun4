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

    default void setAccessLevel(@Nonnull Player object, @Nonnull AccessLevel newLevel) {
        setAccessLevel(object.getUniqueId(), newLevel);
    }

    default boolean hasDataFor(@Nonnull final Player object) {
        return hasDataFor(object.getUniqueId());
    }


}
