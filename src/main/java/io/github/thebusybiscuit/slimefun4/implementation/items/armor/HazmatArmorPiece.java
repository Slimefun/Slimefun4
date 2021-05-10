package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectiveArmor;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * Represents 1 {@link SlimefunArmorPiece} of the Hazmat armor set.
 * One of the very few utilisations of {@link ProtectiveArmor}.
 *
 * @author Linox
 *
 * @see SlimefunArmorPiece
 * @see ProtectiveArmor
 *
 */
public class HazmatArmorPiece extends SlimefunArmorPiece implements ProtectiveArmor {

    private final NamespacedKey namespacedKey;
    private final ProtectionType[] types;

    @ParametersAreNonnullByDefault
    public HazmatArmorPiece(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects) {
        super(category, item, recipeType, recipe, effects);

        types = new ProtectionType[] { ProtectionType.BEES, ProtectionType.RADIATION };
        namespacedKey = new NamespacedKey(SlimefunPlugin.instance(), "hazmat_suit");
    }

    @Override
    public ProtectionType[] getProtectionTypes() {
        return types;
    }

    @Override
    public boolean isFullSetRequired() {
        return true;
    }

    @Override
    public NamespacedKey getArmorSetId() {
        return namespacedKey;
    }
}
