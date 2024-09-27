package io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.utils.compatibility.StackResolver;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;

public class CoalGenerator extends AGenerator {

    @ParametersAreNonnullByDefault
    public CoalGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultFuelTypes() {
        registerFuel(new MachineFuel(80, StackResolver.of(Material.COAL_BLOCK)));
        registerFuel(new MachineFuel(12, StackResolver.of(Material.BLAZE_ROD)));
        registerFuel(new MachineFuel(20, StackResolver.of(Material.DRIED_KELP_BLOCK)));

        // Boats
        for (Material mat : Tag.ITEMS_BOATS.getValues()) {
            registerFuel(new MachineFuel(5, StackResolver.of(mat)));
        }

        // Coal & Charcoal
        registerFuel(new MachineFuel(8, StackResolver.of(Material.COAL)));
        registerFuel(new MachineFuel(8, StackResolver.of(Material.CHARCOAL)));

        // Logs
        for (Material mat : Tag.LOGS.getValues()) {
            registerFuel(new MachineFuel(4, StackResolver.of(mat)));
        }

        // Wooden Planks
        for (Material mat : Tag.PLANKS.getValues()) {
            registerFuel(new MachineFuel(1, StackResolver.of(mat)));
        }

        // Wooden Slabs
        for (Material mat : Tag.WOODEN_SLABS.getValues()) {
            registerFuel(new MachineFuel(1, StackResolver.of(mat)));
        }

        // Wooden Buttons
        for (Material mat : Tag.WOODEN_BUTTONS.getValues()) {
            registerFuel(new MachineFuel(1, StackResolver.of(mat)));
        }

        // Wooden Fences
        for (Material mat : Tag.WOODEN_FENCES.getValues()) {
            registerFuel(new MachineFuel(1, StackResolver.of(mat)));
        }

        // wooden Trapdoors
        for (Material mat : Tag.WOODEN_TRAPDOORS.getValues()) {
            registerFuel(new MachineFuel(3, StackResolver.of(mat)));
        }

        // Wooden Pressure Plates
        for (Material mat : Tag.WOODEN_PRESSURE_PLATES.getValues()) {
            registerFuel(new MachineFuel(2, StackResolver.of(mat)));
        }

        // Wooden Doors
        for (Material mat : Tag.WOODEN_DOORS.getValues()) {
            registerFuel(new MachineFuel(2, StackResolver.of(mat)));
        }

        // Signs
        for (Material mat : Tag.STANDING_SIGNS.getValues()) {
            registerFuel(new MachineFuel(2, StackResolver.of(mat)));
        }
    }

    @Nonnull
    @Override
    public ItemStack getProgressBar() {
        return StackResolver.of(Material.FLINT_AND_STEEL);
    }

}
