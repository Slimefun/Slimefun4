package io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public abstract class CoalGenerator extends AGenerator {

    public CoalGenerator(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultFuelTypes() {
        registerFuel(new MachineFuel(80, new ItemStack(Material.COAL_BLOCK)));
        registerFuel(new MachineFuel(12, new ItemStack(Material.BLAZE_ROD)));
        registerFuel(new MachineFuel(20, new ItemStack(Material.DRIED_KELP_BLOCK)));

        // Coal & Charcoal
        registerFuel(new MachineFuel(8, new ItemStack(Material.COAL)));
        registerFuel(new MachineFuel(8, new ItemStack(Material.CHARCOAL)));

        // Logs
        for (Material mat : Tag.LOGS.getValues()) {
            registerFuel(new MachineFuel(2, new ItemStack(mat)));
        }

        // Wooden Planks
        for (Material mat : Tag.PLANKS.getValues()) {
            registerFuel(new MachineFuel(1, new ItemStack(mat)));
        }
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }

    @Override
    public String getInventoryTitle() {
        return "&cCoal Generator";
    }

}
