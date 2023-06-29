package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

/**
 * {@link LongFallBoots} are a pair of boots which negate fall damage.
 * Nameworthy examples of this are Slime Boots and Bee Boots.
 * <p>
 * <i>Yes, you just found a Portal reference :P</i>
 * 
 * @author TheBusyBiscuit
 *
 */
public class LongFallBoots extends SlimefunArmorPiece {

    private final SoundEffect soundEffect;

    @ParametersAreNonnullByDefault
    public LongFallBoots(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects, SoundEffect soundEffect) {
        super(itemGroup, item, recipeType, recipe, effects);

        this.soundEffect = soundEffect;
    }

    /**
     * This returns the {@link SoundEffect} that is played upon landing with these boots.
     * 
     * @return The {@link SoundEffect} played when landing
     */
    @Nonnull
    public SoundEffect getSoundEffect() {
        return soundEffect;
    }
}
