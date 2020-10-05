package io.github.thebusybiscuit.slimefun4.implementation.accessibility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessLevel;
import io.github.thebusybiscuit.slimefun4.api.accessibility.ConcreteAccessLevel;
import io.github.thebusybiscuit.slimefun4.api.accessibility.PlayerAccessData;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerAccessDataImpl implements PlayerAccessData {

    protected static final JsonParser SHARED_PARSER = new JsonParser();

    private final Map<UUID, AccessLevel> levelMap = new HashMap<>();

    public PlayerAccessDataImpl() {

    }

    public PlayerAccessDataImpl(@Nonnull final String data) {
      loadFromString(data);
    }

    @Nonnull
    @Override
    public Class<UUID> getType() {
        return UUID.class;
    }

    @Nonnull
    private static JsonObject serializeAccessLevel(@Nonnull final Enum<?> instance) {
        final JsonObject serializedLevel = new JsonObject();
        serializedLevel.addProperty("clazz", instance.getClass().getCanonicalName());
        serializedLevel.addProperty("value", instance.name());
        return serializedLevel;
    }

    private static AccessLevel deserializeAccessLevel(@Nonnull final String key,
                                                      @Nonnull final JsonElement element,
                                                      final Map<String, Class<?>> cache) {
        if (!element.isJsonObject()) {
            Slimefun.getLogger().log(Level.WARNING,
                "Invalid AccessData for key: " + key + "! Element is not a JsonObject!");
            return null;
        }
        final JsonObject object = element.getAsJsonObject();
        JsonElement raw = object.get("clazz");
        JsonPrimitive rawString;
        if (!raw.isJsonPrimitive() || ((rawString = raw.getAsJsonPrimitive()).isString())) {
            Slimefun.getLogger().log(Level.WARNING,
                "Invalid AccessLevel for key: " + key + "! Element is not a String!");
            return null;
        }
        final Class<?> clazz = cache.computeIfAbsent(rawString.getAsString(), (str) -> {
            try {
                return Class.forName(str);
            } catch (ReflectiveOperationException ex) {
                Slimefun.getLogger().log(Level.WARNING,
                    "Invalid AccessLevel for key: " + key + "! Unknown AccessLevel class: " + str);
                return Object.class;
            }
        });
        if (!clazz.isEnum()) {
            Slimefun.getLogger().log(Level.WARNING,
                "Invalid AccessLevel for key:" + key + "! Class is not an enum: " + clazz
                    .getCanonicalName());
            return null;
        }
        raw = object.get("value");
        if (!raw.isJsonPrimitive() || ((rawString = raw.getAsJsonPrimitive()).isString())) {
            Slimefun.getLogger().log(Level.WARNING,
                "Invalid AccessLevel for key: " + key + "! JsonElement is not a String!");
            return null;
        }
        final Object o;
        try {
            o = Enum.valueOf(((Class<? extends Enum>) clazz), rawString.getAsString());
        } catch (IllegalArgumentException ex) {
            Slimefun.getLogger().log(Level.WARNING,
                "Invalid AccessLevel for key: " + key + "! Reason: " + ex.getMessage());
            return null;
        }
        if (o instanceof AccessLevel) {
            return (AccessLevel) o;
        } else {
            Slimefun.getLogger().log(Level.WARNING, "Invalid AccessLevel for key: " + key
                + "! Serialized enum is not an instance of an AccessLevel!");
            return null;
        }
    }

    @Nonnull
    @Override
    public AccessLevel getAccessLevel(@Nonnull final UUID player) {
        return levelMap.getOrDefault(player, ConcreteAccessLevel.NONE);
    }

    @Override
    public void setAccessLevel(@Nonnull final UUID player, @Nullable final AccessLevel newLevel) {
        levelMap.remove(player);
        if (newLevel != null) {
            if (!newLevel.getClass().isEnum()) {
                throw new IllegalArgumentException("Non-enum AccessLevels are not supported by this AccessData instance!");
            }
            levelMap.put(player, newLevel);
        }
    }

    @Override
    public boolean hasDataFor(@Nonnull final UUID object) {
        return levelMap.containsKey(object);
    }

    @Nonnull
    @Override
    public String saveToString() {
        final JsonObject object = new JsonObject();
        for (final Map.Entry<UUID, AccessLevel> entry : levelMap.entrySet()) {
            final AccessLevel level = entry.getValue();
            object.add(serialize(entry.getKey()), serializeAccessLevel(((Enum<?>) level)));
        }
        return object.getAsString();
    }

    @Override
    public void loadFromString(@Nonnull final String json) {
        final JsonElement element = SHARED_PARSER.parse(json);
        if (!element.isJsonObject()) {
            return;
        }
        final JsonObject object = element.getAsJsonObject();
        final Map<String, Class<?>> localCache = new HashMap<>();
        for (final Map.Entry<String, JsonElement> entry : object.entrySet()) {
            final String key = entry.getKey();
            final UUID reconstructedObject;
            try {
                reconstructedObject = deserialize(key);
            } catch (IllegalArgumentException ex) {
                Slimefun.getLogger().log(Level.WARNING,
                    "Failed to deserialize data for key: " + key + "! Reason: " + ex.getMessage());
                continue;
            }
            final JsonElement value = entry.getValue();
            final AccessLevel level;
            if ((level = deserializeAccessLevel(key, value, localCache)) == null) {
                continue;
            }
            levelMap.put(reconstructedObject, level);
        }
    }

    @Nullable
    protected UUID deserialize(@Nonnull final String input) throws IllegalArgumentException {
        return UUID.fromString(input);
    }

    @Nonnull
    protected String serialize(@Nonnull final UUID data) {
        return data.toString();
    }
}
