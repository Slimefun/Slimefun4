package io.github.thebusybiscuit.slimefun5.implementation.items.armor;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;

/**
 * A {@link SlimefunArmorPiece} of the Glowstone set. Besides its potion effects, wearing a piece
 * spawns a subtle glow aura around the player on every armor tick.
 *
 * @author TheBusyBiscuit
 */
public class GlowstoneArmorPiece extends SlimefunArmorPiece {

    @ParametersAreNonnullByDefault
    public GlowstoneArmorPiece(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable PotionEffect[] effects) {
        super(itemGroup, item, recipeType, recipe, effects);
    }
}
