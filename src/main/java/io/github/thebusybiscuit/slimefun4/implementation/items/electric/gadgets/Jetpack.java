package io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ChargableItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Jetpack extends ChargableItem {

    private final double thrust;

    public Jetpack(SlimefunItemStack item, ItemStack[] recipe, double thrust) {
        super(Categories.TECH, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        this.thrust = thrust;
    }

    public double getThrust() {
        return thrust;
    }

}
