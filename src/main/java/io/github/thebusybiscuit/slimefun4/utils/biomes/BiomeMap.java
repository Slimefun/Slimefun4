package io.github.thebusybiscuit.slimefun4.utils.biomes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;
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
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * {@link BiomeMap}s are used to map data values to {@link Biome} constants.
 * <p>
 * We heavily utilise this method of data mapping for {@link GEOResource}s, especially
 * when supporting multiple versions of Minecraft. This way, we can have different {@link BiomeMap}s
 * for different versions of Minecraft, in case {@link Biome} names change in-between versions.
 * <p>
 * The data type can be any type of {@link Object}.
 * The most common type is {@link Integer}, if you are using complex objects and try to read
 * your {@link BiomeMap} from a {@link JsonElement}, make sure to provide an adequate
 * {@link BiomeDataConverter} to convert the raw json data.
 * 
 * @author TheBusyBiscuit
 *
 * @param <T>
 *            The stored data type
 */
public class BiomeMap<T> implements Keyed {

    /**
     * Our internal {@link EnumMap} holding all the data.
     */
    private final Map<Biome, T> dataMap = new EnumMap<>(Biome.class);

    /**
     * The {@link NamespacedKey} to identify this {@link BiomeMap}.
     */
    private final NamespacedKey namespacedKey;

    /**
     * This constructs a new {@link BiomeMap} with the given {@link NamespacedKey}.
     * 
     * @param namespacedKey
     *            The {@link NamespacedKey} for this {@link BiomeMap}
     */
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

    public boolean containsKey(@Nonnull Biome biome) {
        Validate.notNull(biome, "The biome must not be null.");

        return dataMap.containsKey(biome);
    }

    public boolean containsValue(@Nonnull T value) {
        Validate.notNull(value, "The value must not be null.");

        return dataMap.containsValue(value);
    }

    /**
     * This returns whether this {@link BiomeMap} is empty.
     * An empty {@link BiomeMap} contains no biomes or values.
     * 
     * @return Whether this {@link BiomeMap} is empty.
     */
    public boolean isEmpty() {
        return dataMap.isEmpty();
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

    public boolean remove(@Nonnull Biome biome) {
        Validate.notNull(biome, "The biome cannot be null.");

        return dataMap.remove(biome) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull NamespacedKey getKey() {
        return namespacedKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "BiomeMap " + dataMap.toString();
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static <T> BiomeMap<T> fromJson(NamespacedKey key, String json, BiomeDataConverter<T> valueConverter) throws BiomeMapException {
        // All parameters are validated by the Parser.
        BiomeMapParser<T> parser = new BiomeMapParser<>(key, valueConverter);
        parser.read(json);
        return parser.buildBiomeMap();
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static <T> BiomeMap<T> fromJson(NamespacedKey key, String json, BiomeDataConverter<T> valueConverter, boolean isLenient) throws BiomeMapException {
        // All parameters are validated by the Parser.
        BiomeMapParser<T> parser = new BiomeMapParser<>(key, valueConverter);
        parser.setLenient(isLenient);
        parser.read(json);
        return parser.buildBiomeMap();
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static <T> BiomeMap<T> fromResource(NamespacedKey key, JavaPlugin plugin, String path, BiomeDataConverter<T> valueConverter) throws BiomeMapException {
        Validate.notNull(key, "The key shall not be null.");
        Validate.notNull(plugin, "The plugin shall not be null.");
        Validate.notNull(path, "The path should not be null!");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getClass().getResourceAsStream(path), StandardCharsets.UTF_8))) {
            return fromJson(key, reader.lines().collect(Collectors.joining("")), valueConverter);
        } catch (IOException x) {
            throw new BiomeMapException(key, x);
        }
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull BiomeMap<Integer> getIntMapFromResource(NamespacedKey key, JavaPlugin plugin, String path) throws BiomeMapException {
        return fromResource(key, plugin, path, JsonElement::getAsInt);
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull BiomeMap<Long> getLongMapFromResource(NamespacedKey key, JavaPlugin plugin, String path) throws BiomeMapException {
        return fromResource(key, plugin, path, JsonElement::getAsLong);
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull BiomeMap<String> getStringMapFromResource(NamespacedKey key, JavaPlugin plugin, String path) throws BiomeMapException {
        return fromResource(key, plugin, path, JsonElement::getAsString);
    }
}
