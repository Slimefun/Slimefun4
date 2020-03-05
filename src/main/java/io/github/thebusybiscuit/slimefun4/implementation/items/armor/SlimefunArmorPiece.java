package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SlimefunArmorPiece extends SlimefunItem {

    private final PotionEffect[] effects;

    public SlimefunArmorPiece(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects) {
        super(category, item, recipeType, recipe);
        this.effects = effects;
    }

    public PotionEffect[] getPotionEffects() {
        return this.effects;
    }

}
