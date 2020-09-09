package io.github.thebusybiscuit.slimefun4.utils.tags;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;

import io.github.thebusybiscuit.slimefun4.api.exceptions.TagMisconfigurationException;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.CropGrowthAccelerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner.IndustrialMiner;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ClimbingPick;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ExplosiveShovel;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.PickaxeOfTheSeeker;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.PickaxeOfVeinMining;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.SmeltersPickaxe;

/**
 * This enum contains various implementations of the {@link Tag} interface.
 * Most of them serve some purpose within Slimefun's implementation, some are just pure
 * extensions of the default Minecraft tags.
 * The actual tag files are located in the {@literal /src/main/resources/tags} directory
 * and follow Minecraft's tags.json format.
 * 
 * @author TheBusyBiscuit
 *
 */
public enum SlimefunTag implements Tag<Material> {

    /**
     * Materials which are sensitive to break.
     * Things like Saplings or Pressure plates which break as well when you break
     * the block beneath them.
     */
    SENSITIVE_MATERIALS,

    /**
     * Minecraft ores.
     */
    ORES,

    /**
     * All variants of torches, normal, soulfire and redstone.
     */
    TORCHES,

    /**
     * All terracotta variants, does not include glazed terracotta.
     */
    TERRACOTTA,

    /**
     * All ice variants, normal, packed, blue and whatever else there is.
     */
    ICE_VARIANTS,

    /**
     * All stone variants, normal, andesite, diorite, granite and whatever else may come.
     */
    STONE_VARIANTS,

    /**
     * All variants of concrete powder.
     * Can you believe there is no tag for this already?
     */
    CONCRETE_POWDERS,

    /**
     * All materials which the {@link ExplosiveShovel} can break.
     */
    EXPLOSIVE_SHOVEL_BLOCKS,

    /**
     * All materials (ores) which the {@link PickaxeOfVeinMining} recognizes.
     */
    PICKAXE_OF_VEIN_MINING_BLOCKS,

    /**
     * All materials (ores) which the {@link PickaxeOfTheSeeker} recognizes.
     */
    PICKAXE_OF_THE_SEEKER_BLOCKS,

    /**
     * All materials which the {@link SmeltersPickaxe} will try and smelt.
     */
    SMELTERS_PICKAXE_BLOCKS,

    /**
     * All materials (ores) which the {@link IndustrialMiner} will try to mine.
     */
    INDUSTRIAL_MINER_ORES,

    /**
     * All materials (crops) which the {@link CropGrowthAccelerator} will recognize.
     */
    CROP_GROWTH_ACCELERATOR_BLOCKS,

    /**
     * All materials which the {@link ClimbingPick} is able to climb.
     */
    CLIMBING_PICK_SURFACES;

    private static final Map<String, SlimefunTag> nameLookup = new HashMap<>();
    public static final SlimefunTag[] values = values();

    static {
        for (SlimefunTag tag : values) {
            nameLookup.put(tag.name(), tag);
        }
    }

    private final NamespacedKey key;
    private final Set<Material> includedMaterials = new HashSet<>();
    private final Set<Tag<Material>> additionalTags = new HashSet<>();

    /**
     * This constructs a new {@link SlimefunTag}.
     * The {@link NamespacedKey} will be automatically inferred using
     * {@link SlimefunPlugin} and {@link #name()}.
     */
    SlimefunTag() {
        key = new NamespacedKey(SlimefunPlugin.instance(), name().toLowerCase(Locale.ROOT));
    }

    /**
     * This method reloads this {@link SlimefunTag} from our resources directory.
     * 
     * @throws TagMisconfigurationException
     *             This is thrown whenever a {@link SlimefunTag} could not be parsed properly
     */
    public void reload() throws TagMisconfigurationException {
        new TagParser(this).parse(this, (materials, tags) -> {
            this.includedMaterials.clear();
            this.includedMaterials.addAll(materials);

            this.additionalTags.clear();
            this.additionalTags.addAll(tags);
        });
    }

    @Nonnull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public boolean isTagged(@Nonnull Material item) {
        if (includedMaterials.contains(item)) {
            return true;
        }
        else {
            // Check if any of our additional Tags contain this Materials
            for (Tag<Material> tag : additionalTags) {
                if (tag.isTagged(item)) {
                    return true;
                }
            }

            // Now we can be sure it isn't tagged in any way
            return false;
        }
    }

    @Nonnull
    @Override
    public Set<Material> getValues() {
        if (additionalTags.isEmpty()) {
            return Collections.unmodifiableSet(includedMaterials);
        }
        else {
            Set<Material> values = new HashSet<>(includedMaterials);

            for (Tag<Material> tag : additionalTags) {
                values.addAll(tag.getValues());
            }

            return Collections.unmodifiableSet(values);
        }
    }

    /**
     * This returns a {@link Set} of {@link Tag Tags} which are children of this {@link SlimefunTag},
     * these can be other {@link SlimefunTag SlimefunTags} or regular {@link Tag Tags}.
     * 
     * <strong>The returned {@link Set} is immutable</strong>
     * 
     * @return An immutable {@link Set} of all sub tags.
     */
    @Nonnull
    public Set<Tag<Material>> getSubTags() {
        return Collections.unmodifiableSet(additionalTags);
    }

    /**
     * This method returns an Array representation for this {@link SlimefunTag}.
     * 
     * @return A {@link Material} array for this {@link Tag}
     */
    @Nonnull
    public Material[] toArray() {
        return getValues().toArray(new Material[0]);
    }

    /**
     * This returns a {@link Stream} of {@link Material Materials} for this {@link SlimefunTag}.
     * 
     * @return A {@link Stream} of {@link Material Materials}
     */
    @Nonnull
    public Stream<Material> stream() {
        return getValues().stream();
    }

    /**
     * Get a value from the cache map rather than calling {@link Enum#valueOf(Class, String)}.
     * This is 25-40% quicker than the standard {@link Enum#valueOf(Class, String)} depending on
     * your Java version. It also means that you can avoid an IllegalArgumentException which let's
     * face it is always good.
     *
     * @param value
     *            The value which you would like to look up.
     * 
     * @return The {@link SlimefunTag} or null if it does not exist.
     */
    @Nullable
    public static SlimefunTag getTag(@Nonnull String value) {
        Validate.notNull(value, "A tag cannot be null!");
        return nameLookup.get(value);
    }

}
