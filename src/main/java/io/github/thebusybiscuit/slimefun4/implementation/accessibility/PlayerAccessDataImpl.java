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


    private static boolean validateAccessLevel(@Nonnull String key, @Nonnull JsonElement element) {
        if (!element.isJsonObject()) {
            Slimefun.getLogger().log(Level.WARNING,
                "Invalid AccessData for key: " + key + "! Element is not a JsonObject!");
            return false;
        }
        JsonObject object = element.getAsJsonObject();
        JsonElement raw = object.get(JsonDeserializationService.ENUM_CLASS_KEY);
        JsonPrimitive rawString;
        if (!raw.isJsonPrimitive() || !((rawString = raw.getAsJsonPrimitive()).isString())) {
            Slimefun.getLogger().log(Level.WARNING,
                "Invalid AccessLevel for key: " + key + "! Element is not a String!");
            return false;
        }
        return resolveAccessLevelClass(rawString.getAsString()) != null;
    }

    @Nullable
    private static Class<? extends AccessLevel> resolveAccessLevelClass(@Nonnull String rawClass) {
        Class<?> clazz;
        try {
            clazz = Class.forName(rawClass);
        } catch (ClassNotFoundException ex) {
            Slimefun.getLogger()
                .log(Level.WARNING, "Invalid AccessData! Unknown AccessData class: " + rawClass);
            return null;
        }
        if (!AccessLevel.class.isAssignableFrom(clazz)) {
            Slimefun.getLogger()
                .log(Level.WARNING, "Invalid AccessData! AccessLevel is not assignable from: " + rawClass);
            return null;
        }
        return clazz.asSubclass(AccessLevel.class);
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
        JsonDeserializationService service = SlimefunPlugin.getJsonDeserializationService();
        JsonObject object = element.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String rawClass = entry.getKey();
            UUID reconstructedObject;
            try {
                reconstructedObject = deserialize(rawClass);
            } catch (IllegalArgumentException ex) {
                Slimefun.getLogger().log(Level.WARNING,
                    "Failed to deserialize data for key: " + rawClass + "! Reason: " + ex.getMessage());
                continue;
            }
            JsonElement value = entry.getValue();
            Class<? extends AccessLevel> clazz = resolveAccessLevelClass(rawClass);
            if (clazz != null) {
                service.deserialize(clazz, value, jsonElement -> validateAccessLevel(rawClass, jsonElement))
                    .ifPresent(accessLevel -> levelMap.put(reconstructedObject, accessLevel));
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
