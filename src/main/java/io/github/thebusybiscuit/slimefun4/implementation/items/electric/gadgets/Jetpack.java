package io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ChargableItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Jetpack extends ChargableItem {

    private final double thrust;

    public Jetpack(Category category, SlimefunItemStack item, ItemStack[] recipe, double thrust) {
        super(category, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        this.thrust = thrust;
    }

    public double getThrust() {
        return thrust;
    }

}
