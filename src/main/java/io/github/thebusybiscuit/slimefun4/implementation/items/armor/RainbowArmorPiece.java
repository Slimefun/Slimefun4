package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * Represents a {@link SlimefunArmorPiece} with rainbow properties (leather armor changing color).
 *
 * @author martinbrom
 */
public class RainbowArmorPiece extends SlimefunArmorPiece {

    /**
     * This creates a new {@link RainbowArmorPiece} from the given arguments.
     *
     * @param category The {@link Category} this {@link RainbowArmorPiece} belongs to
     * @param item The {@link SlimefunItemStack} that describes the visual features of our {@link RainbowArmorPiece}
     * @param recipeType the {@link RecipeType} that determines how this {@link RainbowArmorPiece} is crafted
     * @param recipe An Array representing the recipe of this {@link RainbowArmorPiece}
     */
    @ParametersAreNonnullByDefault
    public RainbowArmorPiece(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe, new PotionEffect[0]);

        if (!SlimefunTag.LEATHER_ARMOR.isTagged(item.getType())) {
            throw new IllegalArgumentException("Rainbow armor needs to be a leather armor piece!");
        }
    }

}
