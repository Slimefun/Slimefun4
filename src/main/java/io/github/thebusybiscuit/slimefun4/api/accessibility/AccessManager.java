package io.github.thebusybiscuit.slimefun4.api.accessibility;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Represents the base for anything which will handle {@link AccessData}
 *
 * @author md5sha256
 */
public interface AccessManager {

    @Nonnull AccessLevel getLevel(@Nonnull Player player);

    @Nonnull AccessLevel getLevel(@Nonnull UUID player);

    @Nonnull AccessLevel getLevel(@Nonnull Object object);

    boolean hasDataFor(@Nonnull Object object);
}
