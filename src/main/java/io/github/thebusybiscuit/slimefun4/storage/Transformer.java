package io.github.thebusybiscuit.slimefun4.storage;

import org.bukkit.NamespacedKey;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public interface Transformer<T> {

    @ParametersAreNonnullByDefault
    void transformInto(DataObject dataObject, NamespacedKey key, T object);

    @Nullable
    @ParametersAreNonnullByDefault
    T transformFrom(DataObject dataObject, NamespacedKey key);
}
