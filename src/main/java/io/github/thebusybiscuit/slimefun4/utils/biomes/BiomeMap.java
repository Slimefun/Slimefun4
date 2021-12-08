package io.github.thebusybiscuit.slimefun4.utils.biomes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;

import com.google.gson.JsonElement;

import io.github.thebusybiscuit.slimefun4.api.exceptions.BiomeMapException;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public class BiomeMap<T> implements Keyed {

    private final Map<Biome, T> dataMap = new EnumMap<>(Biome.class);
    private final NamespacedKey namespacedKey;

    @ParametersAreNonnullByDefault
    public BiomeMap(NamespacedKey namespacedKey) {
        Validate.notNull(namespacedKey, "The key must not be null.");

        this.namespacedKey = namespacedKey;
    }

    public @Nullable T get(@Nonnull Biome biome) {
        Validate.notNull(biome, "The biome shall not be null.");

        return dataMap.get(biome);
    }

    public @Nonnull T getOrDefault(@Nonnull Biome biome, T defaultValue) {
        Validate.notNull(biome, "The biome should not be null.");

        return dataMap.getOrDefault(biome, defaultValue);
    }

    public boolean contains(@Nonnull Biome biome) {
        Validate.notNull(biome, "The biome must not be null.");

        return dataMap.containsKey(biome);
    }

    public boolean put(@Nonnull Biome biome, @Nonnull T value) {
        Validate.notNull(biome, "The biome should not be null.");
        Validate.notNull(value, "Values cannot be null.");

        return dataMap.put(biome, value) == null;
    }

    public void putAll(@Nonnull Map<Biome, T> map) {
        Validate.notNull(map, "The map should not be null.");

        dataMap.putAll(map);
    }

    public void putAll(@Nonnull BiomeMap<T> map) {
        Validate.notNull(map, "The map should not be null.");

        dataMap.putAll(map.dataMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull NamespacedKey getKey() {
        return namespacedKey;
    }

    @Override
    public String toString() {
        return "BiomeMap " + dataMap.toString();
    }

    @ParametersAreNonnullByDefault
    public static <T> @Nonnull BiomeMap<T> fromJson(NamespacedKey key, String json, Function<JsonElement, T> valueConverter) throws BiomeMapException {
        // All parameters are validated by the Parser.
        BiomeMapParser<T> parser = new BiomeMapParser<>(key, valueConverter);
        parser.read(json);
        return parser.buildBiomeMap();
    }

    @ParametersAreNonnullByDefault
    public static <T> @Nonnull BiomeMap<T> fromResource(NamespacedKey key, String path, Function<JsonElement, T> valueConverter) throws BiomeMapException {
        Validate.notNull(key, "The key shall not be null.");
        Validate.notNull(path, "The path should not be null!");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Slimefun.class.getResourceAsStream(path), StandardCharsets.UTF_8))) {
            return fromJson(key, reader.lines().collect(Collectors.joining("")), valueConverter);
        } catch (IOException x) {
            throw new BiomeMapException(key, x);
        }
    }
}
