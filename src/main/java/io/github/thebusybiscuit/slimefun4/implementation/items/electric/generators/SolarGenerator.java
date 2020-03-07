package io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.GeneratorTicker;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public abstract class SolarGenerator extends SimpleSlimefunItem<GeneratorTicker> implements EnergyNetComponent {

    public SolarGenerator(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    /**
     * This method returns the amount of energy that this {@link SolarGenerator}
     * produces during the day.
     * 
     * @return The amount of energy generated at daylight
     */
    public abstract double getDayEnergy();

    /**
     * This method returns the amount of energy that this {@link SolarGenerator}
     * produces during the night.
     * 
     * This is 0 by default.
     * 
     * @return The amount of energy generated at night time
     */
    public double getNightEnergy() {
        // Override this as necessary for highly advanced Solar Generators
        return 0;
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.GENERATOR;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public GeneratorTicker getItemHandler() {
        return new GeneratorTicker() {

            @Override
            public double generateEnergy(Location l, SlimefunItem item, Config data) {
                if (!l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4) || l.getBlock().getLightFromSky() != 15) {
                    return 0D;
                }

                if (l.getWorld().getTime() < 12300 || l.getWorld().getTime() > 23850) {
                    return getDayEnergy();
                }

                return getNightEnergy();
            }

            @Override
            public boolean explode(Location l) {
                return false;
            }
        };
    }

}
