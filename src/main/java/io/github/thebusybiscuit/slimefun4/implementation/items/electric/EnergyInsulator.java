package io.github.thebusybiscuit.slimefun4.implementation.items.electric;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;

/**
 * This {@link EnergyNetComponent} is node that stops {@link EnergyNetComponent}s from connecting
 * when placed in between them.
 *
 * @author iTwins
 *
 * @see EnergyNet
 * @see EnergyNetComponent
 */
public class EnergyInsulator extends SlimefunItem implements EnergyNetComponent {

    @ParametersAreNonnullByDefault
    public EnergyInsulator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public @Nonnull EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.INSULATOR;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

}
