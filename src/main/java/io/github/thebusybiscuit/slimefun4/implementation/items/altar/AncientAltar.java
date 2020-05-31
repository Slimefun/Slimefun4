package io.github.thebusybiscuit.slimefun4.implementation.items.altar;

import io.github.thebusybiscuit.slimefun4.api.events.AncientAltarCraftEvent;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.AncientAltarListener;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.AncientAltarTask;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * The {@link AncientAltar} is a multiblock structure.
 * The altar itself stands in the center, surrounded by {@link AncientPedestal Pedestals}, it is used
 * to craft various magical items.
 *
 * @author TheBusyBiscuit
 * @see AncientAltarListener
 * @see AncientAltarTask
 * @see AncientAltarCraftEvent
 * @see AncientPedestal
 */
public class AncientAltar extends SlimefunItem {

    private final int speed;

    public AncientAltar(Category category, int speed, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        if (speed < 1) {
            throw new IllegalArgumentException("速度必须大于 1");
        }

        this.speed = speed;
    }

    /**
     * This returns the speed of this {@link AncientAltar}.
     * This number determines how much ticks happen inbetween a step in the ritual animation.
     * The default is 8 ticks.
     *
     * @return The speed of this {@link AncientAltar}
     */
    public int getSpeed() {
        return speed;
    }

}