package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.settings.DoubleRangeSetting;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The {@link ArcaneMagnet} is a {@link SlimefunItem} that allows a {@link Player} to
 * automatically pick up {@link Item items} and {@link ExperienceOrb experience} in a certain radius while holding shift and having an
 * {@link ArcaneMagnet} in their {@link Inventory}.
 *
 * @author TheBusyBiscuit
 * @author JustAHuman
 *
 * @see InfusedMagnet
 */
public class ArcaneMagnet extends UnplaceableBlock {
    private final ItemSetting<Double> itemRadius = new DoubleRangeSetting(this, "items-pickup-radius", 0.1, 6.0, Double.MAX_VALUE);
    private final ItemSetting<Double> experienceRadius = new DoubleRangeSetting(this, "xp-pickup-radius", 0.1, 10.0, Double.MAX_VALUE);

    @ParametersAreNonnullByDefault
    public ArcaneMagnet(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemSetting(itemRadius, experienceRadius);
    }

    /**
     * This returns the radius in which {@link Item items} are picked up
     *
     * @return The radius of the {@link ArcaneMagnet}
     */
    public double getItemRadius() {
        return itemRadius.getValue();
    }

    /**
     * This returns the radius in which {@link ExperienceOrb experience} is picked up
     *
     * @return The radius of the {@link ArcaneMagnet}
     */
    public double getExperienceRadius() {
        return experienceRadius.getValue();
    }
}
