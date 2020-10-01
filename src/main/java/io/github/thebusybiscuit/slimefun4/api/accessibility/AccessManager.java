package io.github.thebusybiscuit.slimefun4.api.accessibility;

import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents the base for anything which will handle {@link AccessData}
 *
 * @author md5sha256
 */
public interface AccessManager {

    @Nonnull
    AccessLevel getLevel(@Nonnull Player player);

    @Nonnull
    AccessLevel getLevel(@Nonnull UUID player);

    @Nonnull
    <T> Optional<AccessData<T>> getAccessData(@Nonnull Class<T> clazz);

    @Nonnull
    <T> AccessData<T> getOrRegisterAccessData(@Nonnull Class<T> clazz,
                                              @Nonnull final AccessData<T> def);

    default boolean hasLevel(@Nonnull Player player, @Nonnull AccessLevel accessLevel) {
        return getLevel(player).compare(accessLevel) >= 0;
    }

    boolean hasDataFor(@Nonnull Object object);

    boolean isEmpty();

    void reset();

    void load(@Nonnull SlimefunItemStack itemStack);

    void saveTo(@Nonnull SlimefunItemStack itemStack);

    void loadFromString(@Nonnull String data) throws IllegalArgumentException;

    @Nonnull
    String saveToString();

}
