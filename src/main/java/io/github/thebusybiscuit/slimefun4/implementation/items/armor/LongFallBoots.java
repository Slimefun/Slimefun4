package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * {@link LongFallBoots} are a pair of boots which negate fall damage.
 * Nameworthy examples of this are Slime Boots and Bee Boots.
 * 
 * <i>Yes, you just found a Portal reference :P</i>
 * 
 * @author TheBusyBiscuit
 *
 */
public class LongFallBoots extends SlimefunArmorPiece {

    @ParametersAreNonnullByDefault
    public LongFallBoots(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects) {
        super(category, item, recipeType, recipe, effects);
    }

}
