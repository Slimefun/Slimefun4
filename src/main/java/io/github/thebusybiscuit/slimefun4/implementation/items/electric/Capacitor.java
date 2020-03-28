package io.github.thebusybiscuit.slimefun4.implementation.items.electric;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * A {@link Capacitor} is an {@link EnergyNetComponent} that serves as the energy
 * storage of an {@link EnergyNet}.
 * 
 * It is represented by {@code EnergyNetComponentType.CAPACITOR}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see EnergyNet
 * @see EnergyNetComponent
 *
 */
public class Capacitor extends SlimefunItem implements EnergyNetComponent {

    private final int capacity;

    public Capacitor(int capacity, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(Categories.ELECTRICITY, item, recipeType, recipe);

        this.capacity = capacity;
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CAPACITOR;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

}
