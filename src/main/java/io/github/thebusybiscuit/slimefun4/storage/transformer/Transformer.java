package io.github.thebusybiscuit.slimefun4.storage.transformer;

import io.github.thebusybiscuit.slimefun4.storage.DataObject;
import io.github.thebusybiscuit.slimefun4.storage.NamedKey;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A Transformer converts data from DataObjects into complex objects and back.
 * This is useful when writing or retrieving complex objects to and from DataObjects, as they only work with simple
 * datatypes.
 *
 *
 */
public interface Transformer<T> {

    /**
     * Converts an object of type T into simple datatypes and stores it in a {@link DataObject}
     *
     * @param dataObject The {@link DataObject} to store the object into
     * @param key The {@link NamedKey} associated to the stored data
     * @param object The object to transform
     */
    @ParametersAreNonnullByDefault
    void transformInto(DataObject dataObject, NamedKey key, T object);

    /**
     * Converts the data in a {@link DataObject} to an object of type T and returns it
     *
     * @param dataObject The {@link DataObject} to retrieve the object from
     * @param key The {@link NamedKey} associated to the stored data
     * @return The transformed object
     */
    @Nullable
    @ParametersAreNonnullByDefault
    T transformFrom(DataObject dataObject, NamedKey key);
}
