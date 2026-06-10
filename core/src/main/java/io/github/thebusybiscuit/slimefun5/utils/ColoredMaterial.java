package io.github.thebusybiscuit.slimefun5.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import com.cryptomorin.xseries.XMaterial;

import io.github.thebusybiscuit.slimefun5.utils.tags.SlimefunTag;

/**
 * This class holds a few ordered {@link List Lists} that hold colored variants
 * of {@link Material}.
 * <p>
 * Java-8 universal port: the colored variants are declared as {@link XMaterial} and resolved to the
 * running server's {@link Material} at class-load. On 1.13+ all 16 colors resolve. On legacy servers
 * (pre-flattening) some color families did not exist yet (e.g. concrete = 1.12, shulker boxes = 1.11),
 * and individual colors were data values on a shared {@link Material} rather than distinct materials;
 * those entries resolve to {@code null} here. Callers running below 1.13 must tolerate null entries.
 *
 * @author TheBusyBiscuit
 *
 * @see SlimefunTag
 *
 */
public enum ColoredMaterial {

    // @formatter:off (We want this to stay formatted like this)

    /**
     * This {@link List} contains all wool colors ordered by their appearance ingame.
     */
    WOOL(new XMaterial[] {
            XMaterial.WHITE_WOOL,
            XMaterial.ORANGE_WOOL,
            XMaterial.MAGENTA_WOOL,
            XMaterial.LIGHT_BLUE_WOOL,
            XMaterial.YELLOW_WOOL,
            XMaterial.LIME_WOOL,
            XMaterial.PINK_WOOL,
            XMaterial.GRAY_WOOL,
            XMaterial.LIGHT_GRAY_WOOL,
            XMaterial.CYAN_WOOL,
            XMaterial.PURPLE_WOOL,
            XMaterial.BLUE_WOOL,
            XMaterial.BROWN_WOOL,
            XMaterial.GREEN_WOOL,
            XMaterial.RED_WOOL,
            XMaterial.BLACK_WOOL
    }),

    /**
     * This {@link List} contains all carpet colors ordered by their appearance ingame.
     */
    CARPET(new XMaterial[] {
            XMaterial.WHITE_CARPET,
            XMaterial.ORANGE_CARPET,
            XMaterial.MAGENTA_CARPET,
            XMaterial.LIGHT_BLUE_CARPET,
            XMaterial.YELLOW_CARPET,
            XMaterial.LIME_CARPET,
            XMaterial.PINK_CARPET,
            XMaterial.GRAY_CARPET,
            XMaterial.LIGHT_GRAY_CARPET,
            XMaterial.CYAN_CARPET,
            XMaterial.PURPLE_CARPET,
            XMaterial.BLUE_CARPET,
            XMaterial.BROWN_CARPET,
            XMaterial.GREEN_CARPET,
            XMaterial.RED_CARPET,
            XMaterial.BLACK_CARPET
    }),

    /**
     * This {@link List} contains all stained glass colors ordered by their appearance ingame.
     */
    STAINED_GLASS(new XMaterial[] {
            XMaterial.WHITE_STAINED_GLASS,
            XMaterial.ORANGE_STAINED_GLASS,
            XMaterial.MAGENTA_STAINED_GLASS,
            XMaterial.LIGHT_BLUE_STAINED_GLASS,
            XMaterial.YELLOW_STAINED_GLASS,
            XMaterial.LIME_STAINED_GLASS,
            XMaterial.PINK_STAINED_GLASS,
            XMaterial.GRAY_STAINED_GLASS,
            XMaterial.LIGHT_GRAY_STAINED_GLASS,
            XMaterial.CYAN_STAINED_GLASS,
            XMaterial.PURPLE_STAINED_GLASS,
            XMaterial.BLUE_STAINED_GLASS,
            XMaterial.BROWN_STAINED_GLASS,
            XMaterial.GREEN_STAINED_GLASS,
            XMaterial.RED_STAINED_GLASS,
            XMaterial.BLACK_STAINED_GLASS
    }),

    /**
     * This {@link List} contains all stained glass pane colors ordered by their appearance ingame.
     */
    STAINED_GLASS_PANE(new XMaterial[] {
            XMaterial.WHITE_STAINED_GLASS_PANE,
            XMaterial.ORANGE_STAINED_GLASS_PANE,
            XMaterial.MAGENTA_STAINED_GLASS_PANE,
            XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE,
            XMaterial.YELLOW_STAINED_GLASS_PANE,
            XMaterial.LIME_STAINED_GLASS_PANE,
            XMaterial.PINK_STAINED_GLASS_PANE,
            XMaterial.GRAY_STAINED_GLASS_PANE,
            XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE,
            XMaterial.CYAN_STAINED_GLASS_PANE,
            XMaterial.PURPLE_STAINED_GLASS_PANE,
            XMaterial.BLUE_STAINED_GLASS_PANE,
            XMaterial.BROWN_STAINED_GLASS_PANE,
            XMaterial.GREEN_STAINED_GLASS_PANE,
            XMaterial.RED_STAINED_GLASS_PANE,
            XMaterial.BLACK_STAINED_GLASS_PANE
    }),

    /**
     * This {@link List} contains all terracotta colors ordered by their appearance ingame.
     */
    TERRACOTTA(new XMaterial[] {
            XMaterial.WHITE_TERRACOTTA,
            XMaterial.ORANGE_TERRACOTTA,
            XMaterial.MAGENTA_TERRACOTTA,
            XMaterial.LIGHT_BLUE_TERRACOTTA,
            XMaterial.YELLOW_TERRACOTTA,
            XMaterial.LIME_TERRACOTTA,
            XMaterial.PINK_TERRACOTTA,
            XMaterial.GRAY_TERRACOTTA,
            XMaterial.LIGHT_GRAY_TERRACOTTA,
            XMaterial.CYAN_TERRACOTTA,
            XMaterial.PURPLE_TERRACOTTA,
            XMaterial.BLUE_TERRACOTTA,
            XMaterial.BROWN_TERRACOTTA,
            XMaterial.GREEN_TERRACOTTA,
            XMaterial.RED_TERRACOTTA,
            XMaterial.BLACK_TERRACOTTA
    }),

    /**
     * This {@link List} contains all glazed terracotta colors ordered by their appearance ingame.
     */
    GLAZED_TERRACOTTA(new XMaterial[] {
            XMaterial.WHITE_GLAZED_TERRACOTTA,
            XMaterial.ORANGE_GLAZED_TERRACOTTA,
            XMaterial.MAGENTA_GLAZED_TERRACOTTA,
            XMaterial.LIGHT_BLUE_GLAZED_TERRACOTTA,
            XMaterial.YELLOW_GLAZED_TERRACOTTA,
            XMaterial.LIME_GLAZED_TERRACOTTA,
            XMaterial.PINK_GLAZED_TERRACOTTA,
            XMaterial.GRAY_GLAZED_TERRACOTTA,
            XMaterial.LIGHT_GRAY_GLAZED_TERRACOTTA,
            XMaterial.CYAN_GLAZED_TERRACOTTA,
            XMaterial.PURPLE_GLAZED_TERRACOTTA,
            XMaterial.BLUE_GLAZED_TERRACOTTA,
            XMaterial.BROWN_GLAZED_TERRACOTTA,
            XMaterial.GREEN_GLAZED_TERRACOTTA,
            XMaterial.RED_GLAZED_TERRACOTTA,
            XMaterial.BLACK_GLAZED_TERRACOTTA
    }),

    /**
     * This {@link List} contains all concrete colors ordered by their appearance ingame.
     */
    CONCRETE(new XMaterial[] {
            XMaterial.WHITE_CONCRETE,
            XMaterial.ORANGE_CONCRETE,
            XMaterial.MAGENTA_CONCRETE,
            XMaterial.LIGHT_BLUE_CONCRETE,
            XMaterial.YELLOW_CONCRETE,
            XMaterial.LIME_CONCRETE,
            XMaterial.PINK_CONCRETE,
            XMaterial.GRAY_CONCRETE,
            XMaterial.LIGHT_GRAY_CONCRETE,
            XMaterial.CYAN_CONCRETE,
            XMaterial.PURPLE_CONCRETE,
            XMaterial.BLUE_CONCRETE,
            XMaterial.BROWN_CONCRETE,
            XMaterial.GREEN_CONCRETE,
            XMaterial.RED_CONCRETE,
            XMaterial.BLACK_CONCRETE
    }),

    /**
     * This {@link List} contains all shulker box colors ordered by their appearance ingame.
     */
    SHULKER_BOX(new XMaterial[] {
            XMaterial.WHITE_SHULKER_BOX,
            XMaterial.ORANGE_SHULKER_BOX,
            XMaterial.MAGENTA_SHULKER_BOX,
            XMaterial.LIGHT_BLUE_SHULKER_BOX,
            XMaterial.YELLOW_SHULKER_BOX,
            XMaterial.LIME_SHULKER_BOX,
            XMaterial.PINK_SHULKER_BOX,
            XMaterial.GRAY_SHULKER_BOX,
            XMaterial.LIGHT_GRAY_SHULKER_BOX,
            XMaterial.CYAN_SHULKER_BOX,
            XMaterial.PURPLE_SHULKER_BOX,
            XMaterial.BLUE_SHULKER_BOX,
            XMaterial.BROWN_SHULKER_BOX,
            XMaterial.GREEN_SHULKER_BOX,
            XMaterial.RED_SHULKER_BOX,
            XMaterial.BLACK_SHULKER_BOX
    });

    // @formatter:on

    /**
     * This is our {@link List} of {@link Material Materials}, the backbone of this enum.
     * Entries may be {@code null} on servers older than the version that introduced a given color
     * (see the class-level note).
     */
    private final List<Material> list;

    /**
     * This creates a new constant of {@link ColoredMaterial}.
     * The array must have a length of 16. Each {@link XMaterial} is resolved to the running
     * server's {@link Material}; colors that do not exist on the current version resolve to
     * {@code null} (kept in the list so colour-index ordering is preserved).
     *
     * @param materials
     *            The {@link XMaterial} variants for this {@link ColoredMaterial}.
     */
    ColoredMaterial(@Nonnull XMaterial[] materials) {
        Validate.isTrue(materials.length == 16, "Expected 16, received: " + materials.length + ". Did you miss a color?");

        List<Material> resolved = new ArrayList<>(materials.length);

        for (XMaterial material : materials) {
            // Resolves to null on versions where this colored variant does not exist (pre-flattening).
            resolved.add(material.parseMaterial());
        }

        list = Collections.unmodifiableList(resolved);
    }

    /**
     * This returns an ordered {@link List} of {@link Material Materials}
     * that are part o this {@link ColoredMaterial}.
     *
     * @return An ordered {@link List} of {@link Material Materials}
     */
    public @Nonnull List<Material> asList() {
        return list;
    }

    /**
     * This returns the {@link Material} at the given index.
     *
     * @param index
     *            The index
     *
     * @return The {@link Material} at that index (may be {@code null} on servers predating this color)
     */
    public Material get(int index) {
        Validate.isTrue(index >= 0 && index < 16, "The index must be between 0 and 15 (inclusive).");

        return list.get(index);
    }

    /**
     * This returns the {@link Material} with the given {@link DyeColor}.
     *
     * @param color
     *            The {@link DyeColor}
     *
     * @return The {@link Material} with that {@link DyeColor} (may be {@code null} on servers predating this color)
     */
    public Material get(@Nonnull DyeColor color) {
        Validate.notNull(color, "Color cannot be null!");

        return get(color.ordinal());
    }

}
