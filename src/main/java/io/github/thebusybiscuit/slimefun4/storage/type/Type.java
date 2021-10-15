package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;

/**
 * Type is nothing but an interface all types implement to make generalisation feasible.
 * This allows us to have clean casts to Type without having to worry about which particular datatype the values
 * actually are.
 *
 *
 */
public interface Type {

    /**
     * Get the {@link TypeEnum} of this {@link Type}
     *
     * @return The {@link TypeEnum} of this {@link Type}
     */
    @Nonnull
    TypeEnum getTypeEnum();
}
