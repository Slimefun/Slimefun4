package io.github.thebusybiscuit.slimefun4.utils.tags;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;

import io.github.thebusybiscuit.slimefun4.api.exceptions.TagMisconfigurationException;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

public enum SlimefunTag implements Tag<Material> {

    SENSITIVE_MATERIALS,

    // Expanding upon Minecraft's tags:

    ORES,
    TERRACOTTA,
    ICE_VARIANTS,
    STONE_VARIANTS,
    CONCRETE_POWDERS,

    // Actual Slimefun tags:

    EXPLOSIVE_SHOVEL_BLOCKS,
    PICKAXE_OF_VEIN_MINING_BLOCKS,
    CROP_GROWTH_ACCELERATOR_BLOCKS,
    CLIMBING_PICK_SURFACES;

    private final NamespacedKey key;
    private final Set<Material> includedMaterials = new HashSet<>();
    private final Set<Tag<Material>> additionalTags = new HashSet<>();

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
        new TagParser(this).parse((materials, tags) -> {
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

}
