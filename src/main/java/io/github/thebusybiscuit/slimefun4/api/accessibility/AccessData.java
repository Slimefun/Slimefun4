package io.github.thebusybiscuit.slimefun4.api.accessibility;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a object holding the {@link AccessLevel}
 * for a given object instance.
 *
 * @param <T> The type parameter, can be any object.
 * @author md5sha256
 */
public interface AccessData<T> {

    /**
     * Get the class instance of the type this access data uses as keys.
     *
     * @return Returns a never-null class instance
     */
    @Nonnull
    Class<T> getType();

    /**
     * Get the access level of a given object.
     *
     * @param object The instance of the object
     * @return Returns the registered access level or {@link ConcreteAccessLevel#NONE} if {@link #hasDataFor(Object)} was false.
     */
    @Nonnull
    AccessLevel getAccessLevel(@Nonnull T object);

    /**
     * Set the access level for a given object.
     *
     * @param object   The target object
     * @param newLevel The instance of the new {@link AccessLevel} or null to remove.
     */
    void setAccessLevel(@Nonnull T object, @Nullable AccessLevel newLevel);

    /**
     * Reset (clear) all held data.
     */
    void clear();

    /**
     * Check whether this data object contains a value for a given object.
     *
     * @param object The instance of the object to check.
     * @return Whether this data object contains a value for a given object.
     */
    boolean hasDataFor(@Nonnull T object);

    /**
     * Serialize this object into a JSON string.
     *
     * @return Returns the string representation of the object's current state
     */
    @Nonnull
    default String saveToString() {
        return saveToJsonElement().toString();
    }

    /**
     * Serialize this object into a {@link JsonElement}
     *
     * @return Returns a JsonElement representing the object's current state
     */
    @Nonnull
    JsonElement saveToJsonElement();


    /**
     * Initialize this object's data from a JSON string.
     *
     * @param json The serialized data
     * @see #loadFromJsonElement(JsonElement)
     */
    default void loadFromString(@Nonnull String json) {
        loadFromJsonElement(new JsonParser().parse(json));
    }


    /**
     * Initialize this object's data from a JSON element.
     *
     * @param element The serialized data.
     */
    void loadFromJsonElement(@Nonnull JsonElement element);

}
