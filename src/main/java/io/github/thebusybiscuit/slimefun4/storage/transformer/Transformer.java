package io.github.thebusybiscuit.slimefun4.storage.transformer;

import io.github.thebusybiscuit.slimefun4.storage.DataObject;
import io.github.thebusybiscuit.slimefun4.storage.NamedKey;
import org.bukkit.NamespacedKey;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public interface Transformer<T> {

    @ParametersAreNonnullByDefault
    void transformInto(DataObject dataObject, NamedKey key, T object);

    @Nullable
    @ParametersAreNonnullByDefault
    T transformFrom(DataObject dataObject, NamedKey key);
}
