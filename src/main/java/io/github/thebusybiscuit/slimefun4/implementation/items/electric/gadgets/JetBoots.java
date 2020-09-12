package io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class JetBoots extends SlimefunItem implements Rechargeable {

    private final double speed;
    private final float capacity;

    public JetBoots(Category category, SlimefunItemStack item, ItemStack[] recipe, double speed, float capacity) {
        super(category, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);

        this.speed = speed;
        this.capacity = capacity;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public float getMaxItemCharge(ItemStack item) {
        return capacity;
    }

}
