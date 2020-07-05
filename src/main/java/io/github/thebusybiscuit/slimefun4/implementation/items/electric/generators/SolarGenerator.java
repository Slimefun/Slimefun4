package io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.GeneratorTicker;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public abstract class SolarGenerator extends SimpleSlimefunItem<GeneratorTicker> implements EnergyNetComponent {

    private static final int DEFAULT_NIGHT_ENERGY = 0;

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
        return DEFAULT_NIGHT_ENERGY;
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
                World world = l.getWorld();

                if (world.getEnvironment() != Environment.NORMAL) {
                    return 0;
                }

                boolean isDaytime = isDaytime(world);

                // Performance optimization for daytime-only solar generators
                if (!isDaytime && getNightEnergy() < 0.1) {
                    return 0;
                }

                if (!world.isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4) || l.getBlock().getLightFromSky() != 15) {
                    return 0;
                }

                if (isDaytime) {
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

    /**
     * This method returns whether a given {@link World} has daytime.
     * It will also return false if a thunderstorm is active in this world.
     * 
     * @param world
     *            The {@link World} to check
     * 
     * @return Whether the given {@link World} has daytime and no active thunderstorm
     */
    private boolean isDaytime(World world) {
        return !world.hasStorm() && !world.isThundering() && (world.getTime() < 12300 || world.getTime() > 23850);
    }

    @Override
    public void preRegister() {
        super.preRegister();

        // This prevents Players from toggling the Daylight sensor
        BlockUseHandler handler = PlayerRightClickEvent::cancel;
        addItemHandler(handler);
    }

}
