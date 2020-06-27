package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SlimefunArmorPiece extends SlimefunItem {

    private String setID = null;
    private final PotionEffect[] effects;

    public SlimefunArmorPiece(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects) {
        super(category, item, recipeType, recipe);

        this.effects = effects == null ? new PotionEffect[0] : effects;
    }

    public SlimefunArmorPiece(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects, String setID) {
        this(category, item, recipeType, recipe, effects);

        this.setID = setID;
    }

    /**
     * An Array of {@link PotionEffect PotionEffects} which get applied to a {@link Player} wearing
     * this {@link SlimefunArmorPiece}.
     * 
     * @return An array of effects
     */
    public PotionEffect[] getPotionEffects() {
        return effects;
    }

    /**
     * This returns the armor set ID of this {@link SlimefunArmorPiece}.
     *
     * @return The set ID, <code>null</code> if no set ID is found.
     */
    public String getSetID() {
        return setID;
    }
}
