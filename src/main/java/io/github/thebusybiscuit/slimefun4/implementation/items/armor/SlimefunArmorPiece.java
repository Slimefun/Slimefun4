package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class SlimefunArmorPiece extends SlimefunItem {

    private final PotionEffect[] effects;

    @ParametersAreNonnullByDefault
    public SlimefunArmorPiece(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable PotionEffect[] effects) {
        super(itemGroup, item, recipeType, recipe);

        this.effects = effects == null ? new PotionEffect[0] : effects;
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
}
