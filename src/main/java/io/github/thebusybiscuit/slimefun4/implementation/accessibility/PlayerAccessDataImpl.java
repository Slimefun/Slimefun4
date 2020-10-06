package io.github.thebusybiscuit.slimefun4.implementation.accessibility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessData;
import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessLevel;
import io.github.thebusybiscuit.slimefun4.api.accessibility.ConcreteAccessLevel;
import io.github.thebusybiscuit.slimefun4.api.accessibility.PlayerAccessData;
import io.github.thebusybiscuit.slimefun4.core.services.JsonDeserializationService;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Implementation for {@link PlayerAccessData}.
 */
public class PlayerAccessDataImpl implements PlayerAccessData {

    static {
        SlimefunPlugin.getJsonDeserializationService()
            .registerDeserialization(PlayerAccessDataImpl.class, PlayerAccessDataImpl::new);
    }

    private final Map<UUID, AccessLevel> levelMap = new HashMap<>();

    public PlayerAccessDataImpl() {

    }

    public PlayerAccessDataImpl(@Nonnull final String data) {
        loadFromString(data);
    }

    public PlayerAccessDataImpl(@Nonnull final JsonElement data) {
        loadFromJsonElement(data);
    }

    @Nonnull
    @Override
    public Class<UUID> getType() {
        return UUID.class;
    }

    private static AccessLevel deserializeAccessLevel(
        @Nonnull final String key, @Nonnull final JsonElement element) {
        if (!element.isJsonObject()) {
            Slimefun.getLogger().log(Level.WARNING,
                "Invalid AccessData for key: " + key + "! Element is not a JsonObject!");
            return null;
        }
        final JsonObject object = element.getAsJsonObject();
        final JsonElement raw = object.get(JsonDeserializationService.ENUM_CLASS_KEY);
        final JsonPrimitive rawString;
        if (!raw.isJsonPrimitive() || !((rawString = raw.getAsJsonPrimitive()).isString())) {
            Slimefun.getLogger().log(Level.WARNING,
                "Invalid AccessLevel for key: " + key + "! Element is not a String!");
            return null;
        }
        final Class<?> clazz;
        try {
            clazz = Class.forName(rawString.getAsString());
        } catch (ClassNotFoundException ex) {
            Slimefun.getLogger().log(Level.WARNING,
                "Invalid AccessLevel for key: " + key + "! Unknown AccessLevel class: " + rawString
                    .getAsString());
            return null;
        }
        if (!AccessLevel.class.isAssignableFrom(clazz)) {
            Slimefun.getLogger().log(Level.WARNING,
                "Invalid AccessLevel for key:" + key + "! Class is not an access level: " + clazz
                    .getCanonicalName());
            return null;
        }
        return (AccessLevel) SlimefunPlugin.getJsonDeserializationService()
            .deserialize(clazz, object).orElse(null);
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
            levelMap.put(player, newLevel);
        }
    }

    @Override
    public void reset() {
        levelMap.clear();
    }

    @Override
    public boolean hasDataFor(@Nonnull final UUID object) {
        return levelMap.containsKey(object);
    }

    @Nonnull
    @Override
    public JsonElement saveToJsonElement() {
        final JsonObject object = new JsonObject();
        for (final Map.Entry<UUID, AccessLevel> entry : levelMap.entrySet()) {
            final AccessLevel level = entry.getValue();
            object.add(serialize(entry.getKey()), level.saveToJsonElement());
        }
        return object;
    }

    @Override
    public void loadFromJsonElement(@Nonnull final JsonElement element) {
        if (!element.isJsonObject()) {
            return;
        }
        final JsonObject object = element.getAsJsonObject();
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
            if ((level = deserializeAccessLevel(key, value)) != null) {
                levelMap.put(reconstructedObject, level);
            }
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

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final PlayerAccessDataImpl that = (PlayerAccessDataImpl) o;
        return levelMap.equals(that.levelMap);
    }

    @Override
    public int hashCode() {
        return levelMap.hashCode();
    }
}
