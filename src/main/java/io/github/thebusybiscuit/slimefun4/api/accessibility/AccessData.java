package io.github.thebusybiscuit.slimefun4.api.accessibility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    void setAccessLevel(@Nonnull T object, @Nullable AccessLevel newLevel);

    boolean hasDataFor(@Nonnull T object);

    @Nonnull String saveToString();

}
