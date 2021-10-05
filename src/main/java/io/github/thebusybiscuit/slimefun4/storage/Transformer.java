package io.github.thebusybiscuit.slimefun4.storage;

import org.bukkit.NamespacedKey;

import javax.annotation.ParametersAreNonnullByDefault;

public interface Transformer<T> {

    @ParametersAreNonnullByDefault
    public abstract void transformInto(DataObject dataObject, NamespacedKey key, T  object);

    @ParametersAreNonnullByDefault
    public T transformFrom(DataObject dataObject, NamespacedKey key);

}
