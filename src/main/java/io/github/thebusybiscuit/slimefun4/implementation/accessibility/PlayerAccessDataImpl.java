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

    public PlayerAccessDataImpl() { }

    public PlayerAccessDataImpl(@Nonnull String data) {
        loadFromString(data);
    }

    public PlayerAccessDataImpl(@Nonnull JsonElement data) {
        loadFromJsonElement(data);
    }

    @Nonnull
    @Override
    public Class<UUID> getType() {
        return UUID.class;
    }

    private static AccessLevel deserializeAccessLevel(
        @Nonnull String key, @Nonnull JsonElement element) {
        if (!element.isJsonObject()) {
            Slimefun.getLogger().log(Level.WARNING,
                "Invalid AccessData for key: " + key + "! Element is not a JsonObject!");
            return null;
        }
        JsonObject object = element.getAsJsonObject();
        JsonElement raw = object.get(JsonDeserializationService.ENUM_CLASS_KEY);
        JsonPrimitive rawString;
        if (!raw.isJsonPrimitive() || !((rawString = raw.getAsJsonPrimitive()).isString())) {
            Slimefun.getLogger().log(Level.WARNING,
                "Invalid AccessLevel for key: " + key + "! Element is not a String!");
            return null;
        }
        Class<?> clazz;
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
                "Invalid AccessLevel for key:" + key + "! Class is not an access level: " + clazz.getCanonicalName());
            return null;
        }
        return (AccessLevel) SlimefunPlugin.getJsonDeserializationService()
            .deserialize(clazz, object).orElse(null);
    }

    @Nonnull
    @Override
    public AccessLevel getAccessLevel(@Nonnull UUID player) {
        return levelMap.getOrDefault(player, ConcreteAccessLevel.NONE);
    }

    @Override
    public void setAccessLevel(@Nonnull UUID player, @Nullable AccessLevel newLevel) {
        levelMap.remove(player);
        if (newLevel != null) {
            levelMap.put(player, newLevel);
        }
    }

    @Override
    public void clear() {
        levelMap.clear();
    }

    @Override
    public boolean hasDataFor(@Nonnull UUID object) {
        return levelMap.containsKey(object);
    }

    @Nonnull
    @Override
    public JsonElement saveToJsonElement() {
        JsonObject object = new JsonObject();
        for (Map.Entry<UUID, AccessLevel> entry : levelMap.entrySet()) {
            AccessLevel level = entry.getValue();
            object.add(serialize(entry.getKey()), level.saveToJsonElement());
        }
        return object;
    }

    @Override
    public void loadFromJsonElement(@Nonnull JsonElement element) {
        if (!element.isJsonObject()) {
            return;
        }
        JsonObject object = element.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String key = entry.getKey();
            UUID reconstructedObject;
            try {
                reconstructedObject = deserialize(key);
            } catch (IllegalArgumentException ex) {
                Slimefun.getLogger().log(Level.WARNING,
                    "Failed to deserialize data for key: " + key + "! Reason: " + ex.getMessage());
                continue;
            }
            JsonElement value = entry.getValue();
            AccessLevel level;
            if ((level = deserializeAccessLevel(key, value)) != null) {
                levelMap.put(reconstructedObject, level);
            }
        }
    }

    @Nullable
    protected UUID deserialize(@Nonnull String input) throws IllegalArgumentException {
        return UUID.fromString(input);
    }

    @Nonnull
    protected String serialize(@Nonnull UUID data) {
        return data.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PlayerAccessDataImpl that = (PlayerAccessDataImpl) o;
        return levelMap.equals(that.levelMap);
    }

    @Override
    public int hashCode() {
        return levelMap.hashCode();
    }
}
