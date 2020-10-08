package io.github.thebusybiscuit.slimefun4.core.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;

/**
 * Represents a mapper between classes and json deserialization logic. This class is
 * somewhat similar to Bukkit's {@link ConfigurationSerialization} class.
 *
 * @author md5sha256
 */
public class JsonDeserializationService {

    public static final String ENUM_CLASS_KEY = "enum-class";
    public static final String ENUM_VALUE_KEY = "value";

    private final JsonParser parser = new JsonParser();
    private final Map<String, Function<JsonElement, ?>> deserializerMap = new HashMap<>();

    public JsonDeserializationService() {
    }

    /**
     * Convenience method to deserialize an {@link Enum} which was serialized via {@link #serializeEnum(Enum)}
     *
     * @param element The {@link JsonElement} to deserialize
     * @return Returns the deserialize enum instance or null
     * @see #serializeEnum(Enum)
     */
    @Nullable
    public static Enum<?> deserializeEnum(@Nonnull JsonElement element) {
        if (!element.isJsonObject()) {
            return null;
        }
        JsonObject object = element.getAsJsonObject();
        JsonElement clazz = object.get(ENUM_CLASS_KEY), value = object.get(ENUM_VALUE_KEY);
        JsonPrimitive clazzString;
        JsonPrimitive enumValue;
        if (!clazz.isJsonPrimitive() || !((clazzString = (JsonPrimitive) clazz).isString())
                || !value.isJsonPrimitive() || !((enumValue = (JsonPrimitive) value)).isString()) {
            return null;
        }
        try {
            String rawClazz = clazzString.getAsString(), rawValue = enumValue.getAsString();
            Class<?> classObj = Class.forName(rawClazz);
            if (classObj.isEnum()) {
                return Enum.valueOf((Class<? extends Enum>) classObj, rawValue);
            }
        } catch (ClassNotFoundException ex) {
            Slimefun.getLogger().log(Level.WARNING, "Unknown class: " + clazzString.getAsString());
        }
        return null;
    }

    /**
     * Convenience method to serialize an {@link Enum}
     *
     * @param value The enum instance to serialize
     * @return Returns a {@link JsonObject} representing the instance of the enum instance.
     * @see #deserializeEnum(JsonElement)
     */
    @Nonnull
    public static JsonObject serializeEnum(@Nonnull Enum<?> value) {
        JsonObject object = new JsonObject();
        object.addProperty(ENUM_CLASS_KEY, value.getClass().getCanonicalName());
        object.addProperty(ENUM_VALUE_KEY, value.name());
        return object;
    }

    /**
     * Check whether deserialization logic was registered to this service.
     *
     * @param clazz The {@link Class} instance to check for.
     * @return Returns whether deserialization logic was registered for the given class.
     * @see #registerDeserialization(Class, Function)
     */
    public boolean isDeserializationRegistered(@Nonnull Class<?> clazz) {
        return deserializerMap.containsKey(clazz.getCanonicalName());
    }

    /**
     * Register deserialization logic for a given class.
     *
     * @param clazz        The {@link Class} instance
     * @param deserializer The deserialization logic.
     * @param <T>          The type parameter, can be any object.
     */
    public <T> void registerDeserialization(@Nonnull Class<T> clazz, @Nonnull Function<JsonElement, ?> deserializer) {
        deserializerMap.put(clazz.getCanonicalName(), deserializer);
    }

    /**
     * Unregister deserialization logic for a given class.
     *
     * @param clazz The {@link Class} instance
     * @see #unregisterDeserialization(String)
     */
    public void unregisterDeserialization(@Nonnull Class<?> clazz) {
        deserializerMap.remove(clazz.getCanonicalName());
    }

    /**
     * Unregister deserialization logic for a given class.
     *
     * @param clazz The canonical name of the class instance {@link Class#getCanonicalName()}
     * @see #unregisterDeserialization(Class)
     */
    public void unregisterDeserialization(@Nonnull String clazz) {
        deserializerMap.remove(clazz);
    }

    /**
     * Attempt to deserialize a given JSON string back to it's object counterpart.
     *
     * @param expectedClass The class instance of the expected type
     * @param json          The JSON string to deserialize
     * @param <T>           The expected type of the deserialized object
     * @return Return an optional populated with the deserialized object, else it is empty.
     *
     * @see #deserialize(Class, JsonElement)
     */
    @Nonnull
    public <T> Optional<T> deserialize(@Nonnull Class<T> expectedClass, @Nonnull String json) {
        return deserialize(expectedClass, parser.parse(json));
    }

    /**
     * Attempt to deserialize a given JSON string back to it's object counterpart.
     *
     * @param expectedClass The class instance of the expected type
     * @param json          The {@link JsonElement} to deserialize
     * @param <T>           The expected type of the deserialized object
     * @return Return an optional populated with the deserialized object, else it is empty.
     */
    public <T> Optional<T> deserialize(@Nonnull Class<T> expectedClass, @Nonnull JsonElement json) {
        // Try to get the mapped deserialization logic.
        Function<JsonElement, ?> function = deserializerMap.get(expectedClass.getCanonicalName());
        if (function == null) {
            // Only try generic serialization if explicit deserialization logic is not present.
            if (expectedClass.isEnum()) {
                Object object = deserializeEnum(json);
                // Return the successfully reconstructed object instance.
                if (object != null) {
                    return Optional.of(expectedClass.cast(object));
                }
            }
            return Optional.empty();
        }
        return Optional.ofNullable(expectedClass.cast(function.apply(json)));
    }

}
