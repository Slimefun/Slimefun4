package io.github.thebusybiscuit.slimefun4.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

/**
 * This class holds a few ordered {@link List Lists} that hold colored variants
 * of {@link Material}.
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
    WOOL(new Material[] {
            Material.WHITE_WOOL,
            Material.ORANGE_WOOL,
            Material.MAGENTA_WOOL,
            Material.LIGHT_BLUE_WOOL,
            Material.YELLOW_WOOL,
            Material.LIME_WOOL,
            Material.PINK_WOOL,
            Material.GRAY_WOOL,
            Material.LIGHT_GRAY_WOOL,
            Material.CYAN_WOOL,
            Material.PURPLE_WOOL,
            Material.BLUE_WOOL,
            Material.BROWN_WOOL,
            Material.GREEN_WOOL,
            Material.RED_WOOL,
            Material.BLACK_WOOL
    }),

    /**
     * This {@link List} contains all carpet colors ordered by their appearance ingame.
     */
    CARPET(new Material[] {
            Material.WHITE_CARPET,
            Material.ORANGE_CARPET,
            Material.MAGENTA_CARPET,
            Material.LIGHT_BLUE_CARPET,
            Material.YELLOW_CARPET,
            Material.LIME_CARPET,
            Material.PINK_CARPET,
            Material.GRAY_CARPET,
            Material.LIGHT_GRAY_CARPET,
            Material.CYAN_CARPET,
            Material.PURPLE_CARPET,
            Material.BLUE_CARPET,
            Material.BROWN_CARPET,
            Material.GREEN_CARPET,
            Material.RED_CARPET,
            Material.BLACK_CARPET
    }),

    /**
     * This {@link List} contains all stained glass colors ordered by their appearance ingame.
     */
    STAINED_GLASS(new Material[] {
            Material.WHITE_STAINED_GLASS,
            Material.ORANGE_STAINED_GLASS,
            Material.MAGENTA_STAINED_GLASS,
            Material.LIGHT_BLUE_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS,
            Material.LIME_STAINED_GLASS,
            Material.PINK_STAINED_GLASS,
            Material.GRAY_STAINED_GLASS,
            Material.LIGHT_GRAY_STAINED_GLASS,
            Material.CYAN_STAINED_GLASS,
            Material.PURPLE_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS,
            Material.BROWN_STAINED_GLASS,
            Material.GREEN_STAINED_GLASS,
            Material.RED_STAINED_GLASS,
            Material.BLACK_STAINED_GLASS
    }),

    /**
     * This {@link List} contains all stained glass pane colors ordered by their appearance ingame.
     */
    STAINED_GLASS_PANE(new Material[] {
            Material.WHITE_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.MAGENTA_STAINED_GLASS_PANE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE,
            Material.PINK_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE,
            Material.LIGHT_GRAY_STAINED_GLASS_PANE,
            Material.CYAN_STAINED_GLASS_PANE,
            Material.PURPLE_STAINED_GLASS_PANE,
            Material.BLUE_STAINED_GLASS_PANE,
            Material.BROWN_STAINED_GLASS_PANE,
            Material.GREEN_STAINED_GLASS_PANE,
            Material.RED_STAINED_GLASS_PANE,
            Material.BLACK_STAINED_GLASS_PANE
    }),

    /**
     * This {@link List} contains all terracotta colors ordered by their appearance ingame.
     */
    TERRACOTTA(new Material[] {
            Material.WHITE_TERRACOTTA,
            Material.ORANGE_TERRACOTTA,
            Material.MAGENTA_TERRACOTTA,
            Material.LIGHT_BLUE_TERRACOTTA,
            Material.YELLOW_TERRACOTTA,
            Material.LIME_TERRACOTTA,
            Material.PINK_TERRACOTTA,
            Material.GRAY_TERRACOTTA,
            Material.LIGHT_GRAY_TERRACOTTA,
            Material.CYAN_TERRACOTTA,
            Material.PURPLE_TERRACOTTA,
            Material.BLUE_TERRACOTTA,
            Material.BROWN_TERRACOTTA,
            Material.GREEN_TERRACOTTA,
            Material.RED_TERRACOTTA,
            Material.BLACK_TERRACOTTA
    }),

    /**
     * This {@link List} contains all glazed terracotta colors ordered by their appearance ingame.
     */
    GLAZED_TERRACOTTA(new Material[] {
            Material.WHITE_GLAZED_TERRACOTTA,
            Material.ORANGE_GLAZED_TERRACOTTA,
            Material.MAGENTA_GLAZED_TERRACOTTA,
            Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
            Material.YELLOW_GLAZED_TERRACOTTA,
            Material.LIME_GLAZED_TERRACOTTA,
            Material.PINK_GLAZED_TERRACOTTA,
            Material.GRAY_GLAZED_TERRACOTTA,
            Material.LIGHT_GRAY_GLAZED_TERRACOTTA,
            Material.CYAN_GLAZED_TERRACOTTA,
            Material.PURPLE_GLAZED_TERRACOTTA,
            Material.BLUE_GLAZED_TERRACOTTA,
            Material.BROWN_GLAZED_TERRACOTTA,
            Material.GREEN_GLAZED_TERRACOTTA,
            Material.RED_GLAZED_TERRACOTTA,
            Material.BLACK_GLAZED_TERRACOTTA
    }),

    /**
     * This {@link List} contains all concrete colors ordered by their appearance ingame.
     */
    CONCRETE(new Material[] {
            Material.WHITE_CONCRETE,
            Material.ORANGE_CONCRETE,
            Material.MAGENTA_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE,
            Material.YELLOW_CONCRETE,
            Material.LIME_CONCRETE,
            Material.PINK_CONCRETE,
            Material.GRAY_CONCRETE,
            Material.LIGHT_GRAY_CONCRETE,
            Material.CYAN_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.BLUE_CONCRETE,
            Material.BROWN_CONCRETE,
            Material.GREEN_CONCRETE,
            Material.RED_CONCRETE,
            Material.BLACK_CONCRETE
    });
    
    // @formatter:on

    /**
     * This is our {@link List} of {@link Material Materials}, the backbone of this enum.
     */
    private final List<Material> list;

    /**
     * This creates a new constant of {@link ColoredMaterial}.
     * The array must have a length of 16 and cannot contain null elements!
     * 
     * @param materials
     *            The {@link Material Materials} for this {@link ColoredMaterial}.
     */
    ColoredMaterial(@Nonnull Material[] materials) {
        Validate.noNullElements(materials, "The List cannot contain any null elements");
        Validate.isTrue(materials.length == 16, "Expected 16, received: " + materials.length + ". Did you miss a color?");

        list = Collections.unmodifiableList(Arrays.asList(materials));
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
     * @return The {@link Material} at that index
     */
    public @Nonnull Material get(int index) {
        Validate.isTrue(index >= 0 && index < 16, "The index must be between 0 and 15 (inclusive).");

        return list.get(index);
    }

    /**
     * This returns the {@link Material} with the given {@link DyeColor}.
     * 
     * @param color
     *            The {@link DyeColor}
     * 
     * @return The {@link Material} with that {@link DyeColor}
     */
    public @Nonnull Material get(@Nonnull DyeColor color) {
        Validate.notNull(color, "Color cannot be null!");

        return get(color.ordinal());
    }

}
