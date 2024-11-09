package io.github.thebusybiscuit.slimefun4.utils.biomes;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.thebusybiscuit.slimefun4.api.exceptions.BiomeMapException;
import io.github.thebusybiscuit.slimefun4.utils.JsonUtils;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;

/**
 * The {@link BiomeMapParser} allows you to parse json data into a {@link BiomeMap}.
 *
 * @author TheBusyBiscuit
 *
 * @param <T>
 *            The data type of the resulting {@link BiomeMap}
 *
 * @see BiomeMap
 */
public class BiomeMapParser<T> {

    private static final String VALUE_KEY = "value";
    private static final String BIOMES_KEY = "biomes";

    private final NamespacedKey key;
    private final BiomeDataConverter<T> valueConverter;
    private final Map<Biome, T> map = new HashMap<>();

    /**
     * This flag specifies whether the parsing is "lenient" or not.
     * A lenient parser will not throw a {@link BiomeMapException} if the {@link Biome}
     * could not be found.
     * The default value is false.
     */
    private boolean isLenient = false;

    /**
     * This constructs a new {@link BiomeMapParser}.
     * <p>
     * To parse data, use the {@link #read(JsonArray)} or {@link #read(String)} method.
     *
     * @param key
     *            The {@link NamespacedKey} for the resulting {@link BiomeMap}
     * @param valueConverter
     *            A function to convert {@link JsonElement}s into your desired data type
     */
    @ParametersAreNonnullByDefault
    public BiomeMapParser(NamespacedKey key, BiomeDataConverter<T> valueConverter) {
        Validate.notNull(key, "The key shall not be null.");
        Validate.notNull(valueConverter, "You must provide a Function to convert raw json values to your desired data type.");

        this.key = key;
        this.valueConverter = valueConverter;
    }

    /**
     * This method sets the "lenient" flag for this parser.
     * <p>
     * A lenient parser will not throw a {@link BiomeMapException} if the {@link Biome}
     * could not be found.
     * The default value is false.
     *
     * @param isLenient
     *            Whether this parser should be lenient or not.
     */
    public void setLenient(boolean isLenient) {
        this.isLenient = isLenient;
    }

    /**
     * This method returns whether this parser is flagged as "lenient".
     * <p>
     * A lenient parser will not throw a {@link BiomeMapException} if the {@link Biome}
     * could not be found.
     * The default value is false.
     *
     * @return Whether this parser is lenient or not.
     */
    public boolean isLenient() {
        return isLenient;
    }

    public void read(@Nonnull String json) throws BiomeMapException {
        Validate.notNull(json, "The JSON string should not be null!");
        JsonArray root = null;

        try {
            root = JsonUtils.parseString(json).getAsJsonArray();
        } catch (IllegalStateException | JsonParseException x) {
            throw new BiomeMapException(key, x);
        }

        /*
         * We don't include this in our try/catch, as this type of exception
         * is already specified in the throws-declaration.
         */
        read(root);
    }

    public void read(@Nonnull JsonArray json) throws BiomeMapException {
        Validate.notNull(json, "The JSON Array should not be null!");

        for (JsonElement element : json) {
            if (element instanceof JsonObject) {
                readEntry(element.getAsJsonObject());
            } else {
                throw new BiomeMapException(key, "Unexpected array element: " + element.getClass().getSimpleName() + " - " + element.toString());
            }
        }
    }

    private void readEntry(@Nonnull JsonObject entry) throws BiomeMapException {
        Validate.notNull(entry, "The JSON entry should not be null!");

        /*
         * Check if the entry has a "value" element.
         * The data type is irrelevant here, any JsonElement is supported (in theory).
         * If you write a converter for it, you can also serialize complex objects this way.
         */
        if (entry.has(VALUE_KEY)) {
            T value = valueConverter.convert(entry.get(VALUE_KEY));

            // Check if the entry has a "biomes" element of type JsonArray.
            if (entry.has(BIOMES_KEY) && entry.get(BIOMES_KEY).isJsonArray()) {
                Set<Biome> biomes = readBiomes(entry.get(BIOMES_KEY).getAsJsonArray());

                // Loop through all biome strings in this array
                for (Biome biome : biomes) {
                    T prev = map.put(biome, value);

                    // Check for duplicates
                    if (prev != null) {
                        throw new BiomeMapException(key, "Biome '" + biome.getKey() + "' is registered twice");
                    }
                }
            } else {
                throw new BiomeMapException(key, "Entry is missing a 'biomes' child of type array.");
            }
        } else {
            throw new BiomeMapException(key, "Entry is missing a 'value' child.");
        }
    }

    private @Nonnull Set<Biome> readBiomes(@Nonnull JsonArray array) throws BiomeMapException {
        Validate.notNull(array, "The JSON array should not be null!");
        Set<Biome> biomes = new HashSet<>();

        for (JsonElement element : array) {
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                String value = element.getAsString();

                if (PatternUtils.MINECRAFT_NAMESPACEDKEY.matcher(value).matches()) {
                    String formattedValue = CommonPatterns.COLON.split(value)[1].toUpperCase(Locale.ROOT);

                    try {
                        Biome biome = Biome.valueOf(formattedValue);
                        biomes.add(biome);
                    } catch (IllegalArgumentException x) {
                        // Lenient Parsers will ignore unknown biomes
                        if (isLenient) {
                            continue;
                        }

                        throw new BiomeMapException(key, "The Biome '" + value + "' does not exist!");
                    }
                } else {
                    // The regular expression did not match
                    throw new BiomeMapException(key, "Could not recognize value '" + value + "'");
                }
            } else {
                throw new BiomeMapException(key, "Unexpected array element: " + element.getClass().getSimpleName() + " - " + element.toString());
            }
        }

        return biomes;
    }

    /**
     * This method builds a {@link BiomeMap} based on the parsed data.
     * <p>
     * Make sure to parse data via {@link #read(JsonArray)} or {@link #read(String)}
     * before calling this method! Otherwise the resulting {@link BiomeMap} will be empty.
     *
     * @return The resulting {@link BiomeMap}
     */
    @Nonnull
    public BiomeMap<T> buildBiomeMap() {
        BiomeMap<T> biomeMap = new BiomeMap<>(key);
        biomeMap.putAll(map);
        return biomeMap;
    }

}
