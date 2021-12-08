package io.github.thebusybiscuit.slimefun4.utils.biomes;

import java.util.EnumMap;
import java.util.EnumSet;
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
import com.google.gson.JsonParser;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.thebusybiscuit.slimefun4.api.exceptions.BiomeMapException;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;

/**
 * The {@link BiomeMapParser} allows you to parse json data into a {@link BiomeMap}.
 * 
 * @author TheBusyBiscuit
 *
 * @param <T>
 *            The data type of the resulting {@link BiomeMap}
 */
class BiomeMapParser<T> {

    private static final String VALUE_KEY = "value";
    private static final String BIOMES_KEY = "biomes";

    private final NamespacedKey key;
    private final BiomeDataConverter<T> valueConverter;
    private final Map<Biome, T> map = new EnumMap<>(Biome.class);

    @ParametersAreNonnullByDefault
    BiomeMapParser(NamespacedKey key, BiomeDataConverter<T> valueConverter) {
        Validate.notNull(key, "The key shall not be null.");
        Validate.notNull(valueConverter, "You must provide a Function to convert raw json values to your desired data type.");

        this.key = key;
        this.valueConverter = valueConverter;
    }

    void read(@Nonnull String json) throws BiomeMapException {
        Validate.notNull(json, "The JSON string should not be null!");
        JsonArray root = null;

        try {
            JsonParser parser = new JsonParser();
            root = parser.parse(json).getAsJsonArray();
        } catch (IllegalStateException | JsonParseException x) {
            throw new BiomeMapException(key, x);
        }

        /*
         * We don't include this in our try/catch, as this type of exception
         * is already specified in the throws-declaration.
         */
        read(root);
    }

    void read(@Nonnull JsonArray json) throws BiomeMapException {
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

        if (entry.has(VALUE_KEY)) {
            T value = valueConverter.convert(entry.get(VALUE_KEY));

            if (entry.has(BIOMES_KEY) && entry.get(BIOMES_KEY).isJsonArray()) {
                Set<Biome> biomes = readBiomes(entry.get(BIOMES_KEY).getAsJsonArray());

                for (Biome biome : biomes) {
                    T prev = map.put(biome, value);

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
        Set<Biome> biomes = EnumSet.noneOf(Biome.class);

        for (JsonElement element : array) {
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                String value = element.getAsString();

                if (PatternUtils.MINECRAFT_NAMESPACEDKEY.matcher(value).matches()) {
                    String formattedValue = CommonPatterns.COLON.split(value)[1].toUpperCase(Locale.ROOT);

                    try {
                        Biome biome = Biome.valueOf(formattedValue);
                        biomes.add(biome);
                    } catch (IllegalArgumentException x) {
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

    @Nonnull
    BiomeMap<T> buildBiomeMap() {
        BiomeMap<T> biomeMap = new BiomeMap<>(key);
        biomeMap.putAll(map);
        return biomeMap;
    }

}
