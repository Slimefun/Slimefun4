package io.github.thebusybiscuit.slimefun5.utils.tags;

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
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import io.github.thebusybiscuit.slimefun5.libraries.keys.Keyed;
import org.bukkit.Material;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag;

import com.cryptomorin.xseries.XMaterial;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.thebusybiscuit.slimefun5.api.exceptions.TagMisconfigurationException;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.JsonUtils;
import io.github.thebusybiscuit.slimefun5.utils.PatternUtils;

/**
 * The {@link TagParser} is responsible for parsing a JSON input into a {@link SlimefunTag}.
 *
 * @author TheBusyBiscuit
 *
 * @see SlimefunTag
 *
 */
public class TagParser implements Keyed {

    /**
     * Every {@link Tag} has a {@link NamespacedKey}.
     * This is the {@link NamespacedKey} for the resulting {@link Tag}.
     */
    private final NamespacedKey key;

    /**
     * This constructs a new {@link TagParser}.
     *
     * @param key
     *            The {@link NamespacedKey} of the resulting {@link SlimefunTag}
     */
    public TagParser(@Nonnull NamespacedKey key) {
        this.key = key;
    }

    /**
     * This constructs a new {@link TagParser} for the given {@link SlimefunTag}
     *
     * @param tag
     *            The {@link SlimefunTag} to parse inputs for
     */
    TagParser(@Nonnull SlimefunTag tag) {
        this(tag.getKey());
    }

    void parse(@Nonnull SlimefunTag tag, @Nonnull BiConsumer<Set<Material>, Set<Tag<Material>>> callback) throws TagMisconfigurationException {
        String path = "/tags/" + tag.getKey().getKey() + ".json";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Slimefun.class.getResourceAsStream(path), StandardCharsets.UTF_8))) {
            parse(reader.lines().collect(Collectors.joining("")), callback);
        } catch (IOException x) {
            throw new TagMisconfigurationException(key, x);
        }
    }

    /**
     * This will parse the given JSON {@link String} and run the provided callback with {@link Set Sets} of
     * matched {@link Material Materials} and {@link Tag Tags}.
     *
     * @param json
     *            The JSON {@link String} to parse
     * @param callback
     *            A callback to run after successfully parsing the input
     *
     * @throws TagMisconfigurationException
     *             This is thrown whenever the given input is malformed or no adequate
     *             {@link Material} or {@link Tag} could be found
     */
    public void parse(@Nonnull String json, @Nonnull BiConsumer<Set<Material>, Set<Tag<Material>>> callback) throws TagMisconfigurationException {
        Validate.notNull(json, "Cannot parse a null String");

        try {
            Set<Material> materials = new HashSet<>();
            Set<Tag<Material>> tags = new HashSet<>();

            JsonObject root = JsonUtils.parseString(json).getAsJsonObject();
            JsonElement child = root.get("values");

            if (child instanceof JsonArray) {
                JsonArray values = child.getAsJsonArray();

                for (JsonElement element : values) {
                    if (element instanceof JsonPrimitive && ((JsonPrimitive) element).isString()) {
                        JsonPrimitive primitive = (JsonPrimitive) element;                        // Strings will be parsed directly
                        parsePrimitiveValue(element.getAsString(), materials, tags, true);
                    } else if (element instanceof JsonObject) {
                        /*
                         * JSONObjects can have a "required" property which can
                         * make it optional to resolve the underlying value
                         */
                        parseComplexValue(element.getAsJsonObject(), materials, tags);
                    } else {
                        throw new TagMisconfigurationException(key, "Unexpected value format: " + element.getClass().getSimpleName() + " - " + element.toString());
                    }
                }

                // Run the callback with the filled-in materials and tags
                callback.accept(materials, tags);
            } else {
                // The JSON seems to be empty yet valid
                throw new TagMisconfigurationException(key, "No values array specified");
            }
        } catch (IllegalStateException | JsonParseException x) {
            throw new TagMisconfigurationException(key, x);
        }
    }

    @ParametersAreNonnullByDefault
    private void parsePrimitiveValue(String value, Set<Material> materials, Set<Tag<Material>> tags, boolean throwException) throws TagMisconfigurationException {
        if (PatternUtils.MINECRAFT_NAMESPACEDKEY.matcher(value).matches()) {
            // Match the NamespacedKey against Materials
            Material material = matchMaterialCompat(value);

            if (material != null) {
                // If the Material could be matched, simply add it to our Set
                materials.add(material);
            } else if (throwException && !isLegacyServer()) {
                // On a legacy server an unresolved material almost always means it was added in a
                // newer Minecraft version, not a misconfiguration - skip it silently so the rest of
                // the tag still loads. On modern servers this stays a hard error.
                throw new TagMisconfigurationException(key, "Minecraft Material '" + value + "' seems to not exist!");
            }
        } else if (PatternUtils.MINECRAFT_TAG.matcher(value).matches()) {
            // Get the actual Key portion and match it to item and block tags.
            String keyValue = CommonPatterns.COLON.split(value)[1];
            Tag<Material> itemsTag = Tag.lookup(Tag.REGISTRY_ITEMS, keyValue);
            Tag<Material> blocksTag = Tag.lookup(Tag.REGISTRY_BLOCKS, keyValue);

            if (itemsTag != null) {
                // We will prioritize the item tag
                tags.add(itemsTag);
            } else if (blocksTag != null) {
                // If no item tag exists, fall back to the block tag
                tags.add(blocksTag);
            } else if (throwException && !isLegacyServer()) {
                // Vanilla tags only exist on 1.13+; on legacy servers a missing tag is expected.
                throw new TagMisconfigurationException(key, "There is no '" + value + "' tag in Minecraft.");
            }
        } else if (PatternUtils.SLIMEFUN_TAG.matcher(value).matches()) {
            // Get a SlimefunTag enum value for the given key
            String keyValue = CommonPatterns.COLON.split(value)[1].toUpperCase(Locale.ROOT);
            SlimefunTag tag = SlimefunTag.getTag(keyValue);

            if (tag != null) {
                tags.add(tag);
            } else if (throwException) {
                throw new TagMisconfigurationException(key, "There is no '" + value + "' tag in Slimefun");
            }
        } else if (throwException) {
            // If no RegEx pattern matched, it's malformed.
            throw new TagMisconfigurationException(key, "Could not recognize value '" + value + "'");
        }
    }

    /**
     * Resolves a (possibly namespaced/lowercase) material id to a {@link Material} across versions.
     * {@code Material#matchMaterial} understands namespaced ids only from 1.13 onwards, so on legacy
     * servers we strip the namespace and resolve the legacy enum via XSeries.
     *
     * @param value
     *            The material id, e.g. {@code "minecraft:coal_ore"}
     *
     * @return The matching {@link Material}, or {@code null} if it does not exist on this version
     */
    @Nullable
    private static Material matchMaterialCompat(@Nonnull String value) {
        Material material = Material.matchMaterial(value);

        if (material == null) {
            String name = value.contains(":") ? CommonPatterns.COLON.split(value)[1] : value;
            material = XMaterial.matchXMaterial(name).map(XMaterial::parseMaterial).orElse(null);
        }

        return material;
    }

    /**
     * @return Whether the running server predates Minecraft 1.13, where many modern materials and all
     *         vanilla tags are absent (so missing entries are expected rather than misconfigured).
     */
    private static boolean isLegacyServer() {
        MinecraftVersion version = Slimefun.getMinecraftVersion();
        return version != null && version.isBefore(MinecraftVersion.MINECRAFT_1_13);
    }

    @ParametersAreNonnullByDefault
    private void parseComplexValue(JsonObject entry, Set<Material> materials, Set<Tag<Material>> tags) throws TagMisconfigurationException {
        JsonElement id = entry.get("id");
        JsonElement required = entry.get("required");

        // Check if the entry contains elements of the correct type
        if (id instanceof JsonPrimitive && ((JsonPrimitive) id).isString() && required instanceof JsonPrimitive && ((JsonPrimitive) required).isBoolean()) {
            JsonPrimitive idJson = (JsonPrimitive) id;            boolean isRequired = required.getAsBoolean();

            /*
             * If the Tag is required, an exception may be thrown.
             * Otherwise it will just ignore the value
             */
            parsePrimitiveValue(id.getAsString(), materials, tags, isRequired);
        } else {
            throw new TagMisconfigurationException(key, "Found a JSON Object value without an id!");
        }
    }

    @Override
    public @Nonnull NamespacedKey getKey() {
        return key;
    }

}

