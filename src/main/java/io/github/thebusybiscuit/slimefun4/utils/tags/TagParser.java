package io.github.thebusybiscuit.slimefun4.utils.tags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import io.github.thebusybiscuit.slimefun4.api.exceptions.TagMisconfigurationException;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;

class TagParser implements Keyed {

    private final NamespacedKey key;

    TagParser(@Nonnull SlimefunTag tag) {
        this.key = tag.getKey();
    }

    void parse(@Nonnull BiConsumer<Set<Material>, Set<Tag<Material>>> callback) throws TagMisconfigurationException {
        Set<Material> materials = new HashSet<>();
        Set<Tag<Material>> tags = new HashSet<>();

        JsonParser parser = new JsonParser();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(SlimefunPlugin.class.getResourceAsStream("/tags/" + key.getKey() + ".json"), StandardCharsets.UTF_8))) {
            JsonObject root = parser.parse(reader.lines().collect(Collectors.joining(""))).getAsJsonObject();
            JsonElement child = root.get("values");

            if (child instanceof JsonArray) {
                JsonArray values = child.getAsJsonArray();

                for (JsonElement element : values) {
                    if (element instanceof JsonPrimitive && ((JsonPrimitive) element).isString()) {
                        parsePrimitiveValue(element.getAsString(), materials, tags);
                    }
                    else if (element instanceof JsonObject) {
                        parseComplexValue(element.getAsJsonObject(), materials, tags);
                    }
                    else {
                        throw new TagMisconfigurationException(key, "Unexpected value format: " + element.getClass().getSimpleName() + " - " + element.toString());
                    }
                }

                callback.accept(materials, tags);
            }
            else {
                throw new TagMisconfigurationException(key, "No values array specified");
            }
        }
        catch (IOException | IllegalStateException | JsonParseException x) {
            throw new TagMisconfigurationException(key, x.getMessage());
        }
    }

    @ParametersAreNonnullByDefault
    private void parsePrimitiveValue(String value, Set<Material> materials, Set<Tag<Material>> tags) throws TagMisconfigurationException {
        if (PatternUtils.MINECRAFT_MATERIAL.matcher(value).matches()) {
            // Match the NamespacedKey against Materials
            Material material = Material.matchMaterial(value);

            if (material != null) {
                materials.add(material);
            }
            else {
                throw new TagMisconfigurationException(key, "Minecraft Material '" + value + "' seems to not exist!");
            }
        }
        else if (PatternUtils.MINECRAFT_TAG.matcher(value).matches()) {
            String keyValue = PatternUtils.COLON.split(value)[1];
            NamespacedKey namespacedKey = NamespacedKey.minecraft(keyValue);
            Tag<Material> itemsTag = Bukkit.getTag(Tag.REGISTRY_ITEMS, namespacedKey, Material.class);
            Tag<Material> blocksTag = Bukkit.getTag(Tag.REGISTRY_BLOCKS, namespacedKey, Material.class);

            if (itemsTag != null) {
                tags.add(itemsTag);
            }
            else if (blocksTag != null) {
                tags.add(blocksTag);
            }
            else {
                throw new TagMisconfigurationException(key, "There is no '" + value + "' tag in Minecraft.");
            }
        }
        else if (PatternUtils.SLIMEFUN_TAG.matcher(value).matches()) {
            try {
                String keyValue = PatternUtils.COLON.split(value)[1].toUpperCase(Locale.ROOT);
                SlimefunTag tag = SlimefunTag.valueOf(keyValue);
                tags.add(tag);
            }
            catch (IllegalArgumentException x) {
                throw new TagMisconfigurationException(key, "There is no '" + value + "' tag in Slimefun");
            }
        }
        else {
            throw new TagMisconfigurationException(key, "Could not recognize value '" + value + "'");
        }
    }

    @ParametersAreNonnullByDefault
    private void parseComplexValue(JsonObject entry, Set<Material> materials, Set<Tag<Material>> tags) throws TagMisconfigurationException {
        JsonElement id = entry.get("id");
        JsonElement required = entry.get("required");

        if (id instanceof JsonPrimitive && ((JsonPrimitive) id).isString() && required instanceof JsonPrimitive && ((JsonPrimitive) required).isBoolean()) {
            if (required.getAsBoolean()) {
                parsePrimitiveValue(id.getAsString(), materials, tags);
            }
            else {
                try {
                    parsePrimitiveValue(id.getAsString(), materials, tags);
                }
                catch (TagMisconfigurationException x) {
                    // This is an optional entry, so we will ignore the validation here
                }
            }
        }
        else {
            throw new TagMisconfigurationException(key, "Found a JSON Object value without an id!");
        }
    }

    @Nonnull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

}
