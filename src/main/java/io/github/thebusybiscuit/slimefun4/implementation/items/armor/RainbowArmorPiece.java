package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

/**
 * Represents a {@link SlimefunArmorPiece} with rainbow properties (leather armor changing color).
 *
 * @author martinbrom
 */
public class RainbowArmorPiece extends SlimefunArmorPiece {

    private final Color[] colors;

    /**
     * This creates a new {@link RainbowArmorPiece} from the given arguments.
     *
     * @param itemGroup The {@link ItemGroup} this {@link RainbowArmorPiece} belongs to
     * @param item The {@link SlimefunItemStack} that describes the visual features of our {@link RainbowArmorPiece}
     * @param recipeType the {@link RecipeType} that determines how this {@link RainbowArmorPiece} is crafted
     * @param recipe An Array representing the recipe of this {@link RainbowArmorPiece}
     * @param dyeColors An Array representing the {@link DyeColor}s this {@link RainbowArmorPiece} will cycle between
     */
    @ParametersAreNonnullByDefault
    public RainbowArmorPiece(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, DyeColor[] dyeColors) {
        super(itemGroup, item, recipeType, recipe, new PotionEffect[0]);

        // TODO Change this validation over to our custom validation blocked by https://github.com/baked-libs/dough/pull/184
        Validate.notEmpty(dyeColors, "RainbowArmorPiece colors cannot be empty!");

        if (!SlimefunTag.LEATHER_ARMOR.isTagged(item.getType())) {
            throw new IllegalArgumentException("Rainbow armor needs to be a leather armor piece!");
        }

        colors = Arrays.stream(dyeColors)
            .map(DyeColor::getColor)
            .toArray(Color[]::new);
    }

    /**
     * Returns the {@link Color}s this {@link RainbowArmorPiece} cycles between
     *
     * @return The {@link Color}s of this {@link RainbowArmorPiece}
     */
    public @Nonnull Color[] getColors() {
        return colors;
    }

}
