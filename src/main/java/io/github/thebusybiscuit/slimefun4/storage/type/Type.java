package io.github.thebusybiscuit.slimefun4.storage.type;

import javax.annotation.Nonnull;

public interface Type {

    /**
     * Get the {@link TypeEnum} of this {@link Type}
     *
     * @return The {@link TypeEnum} of this {@link Type}
     */
    @Nonnull
    TypeEnum getTypeEnum();
}
