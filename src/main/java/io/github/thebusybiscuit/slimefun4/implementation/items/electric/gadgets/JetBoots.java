package io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.JetBootsTask;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * {@link JetBoots} allow you to hover for a bit.
 * You can find the actual behaviour in the {@link JetBootsTask} class.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Jetpack
 * @see JetBootsTask
 *
 */
public class JetBoots extends SlimefunItem implements Rechargeable {

    private final double speed;
    private final float capacity;

    @ParametersAreNonnullByDefault
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
