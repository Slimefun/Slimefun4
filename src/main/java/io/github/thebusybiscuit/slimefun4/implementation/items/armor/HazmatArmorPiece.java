package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.core.attributes.CustomProtection;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class HazmatArmorPiece extends SlimefunArmorPiece implements CustomProtection {

    private final ProtectionType[] types;

    public HazmatArmorPiece(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects, String setID) {
        super(category, item, recipeType, recipe, effects, setID);

        types = new ProtectionType[] {ProtectionType.BEES, ProtectionType.RADIATION};
    }

    @Override
    public ProtectionType[] getProtectionTypes() {
        return types;
    }

    @Override
    public boolean requireFullSet() {
        return true;
    }
}
