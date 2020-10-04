package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 * The {@link ElytraCap} negates damage taken when crashing into a wall using elytra.
 *
 * @author Seggan
 */
public class ElytraCap extends SlimefunArmorPiece implements DamageableItem {

    public ElytraCap(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects) {
        super(category, item, recipeType, recipe, effects);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }
}
