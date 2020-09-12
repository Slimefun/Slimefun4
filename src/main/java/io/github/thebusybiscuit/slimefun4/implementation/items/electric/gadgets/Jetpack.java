package io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Jetpack extends SlimefunItem implements Rechargeable {

    private final double thrust;
    private final float capacity;

    public Jetpack(Category category, SlimefunItemStack item, ItemStack[] recipe, double thrust, float capacity) {
        super(category, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);

        this.thrust = thrust;
        this.capacity = capacity;
    }

    public double getThrust() {
        return thrust;
    }

    @Override
    public float getMaxItemCharge(ItemStack item) {
        return capacity;
    }

}
