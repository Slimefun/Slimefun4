package io.github.thebusybiscuit.slimefun4.api.accessibility;

import javax.annotation.Nonnull;

/**
 * Represents a object holding the {@link AccessLevel}
 * for a given object instance.
 *
 * @param <T> The type parameter, can be any object.
 *
 * @author md5sha256
 */
public interface AccessData<T> {

    @Nonnull
    Class<T> getType();

    @Nonnull
    AccessLevel getAccessLevel(@Nonnull T object);

    boolean setAccessLevel(@Nonnull T object, @Nonnull AccessLevel newLevel);

    @Nonnull
    AccessLevel decrementAccessLevel(@Nonnull T object);

    @Nonnull
    AccessLevel incrementAccessLevel(@Nonnull T object);

}
