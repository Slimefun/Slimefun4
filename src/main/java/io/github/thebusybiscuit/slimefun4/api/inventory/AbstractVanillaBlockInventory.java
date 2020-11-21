package io.github.thebusybiscuit.slimefun4.api.inventory;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.AutoBrewer;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.AutoDrier;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricFurnace;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricIngotPulverizer;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricOreGrinder;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricSmeltery;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link AbstractVanillaBlockInventory} is an
 * implementation of {@link AContainer} for machines that use
 * a vanilla block inventory holder.
 *
 * @author CURVX
 *
 * @see AutoBrewer
 * @see AutoDrier
 * @see ElectricFurnace
 * @see ElectricIngotPulverizer
 * @see ElectricOreGrinder
 * @see ElectricSmeltery
 *
 */
public abstract class AbstractVanillaBlockInventory extends AContainer {

    @ParametersAreNonnullByDefault
    public AbstractVanillaBlockInventory(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }
}
