package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.settings.DoubleRangeSetting;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link InfusedMagnet} is a {@link SlimefunItem} that allows a {@link Player} to
 * automatically pick up items in a certain radius while holding shift and having an
 * {@link InfusedMagnet} in their {@link Inventory}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class InfusedMagnet extends UnplaceableBlock {

    private final ItemSetting<Double> radius = new DoubleRangeSetting(this, "pickup-radius", 0.1, 6.0, Double.MAX_VALUE);

    @ParametersAreNonnullByDefault
    public InfusedMagnet(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(radius);
    }

    /**
     * This returns the radius in which items are picked up
     * 
     * @return The radius of the {@link InfusedMagnet}
     */
    public double getRadius() {
        return radius.getValue();
    }

}
