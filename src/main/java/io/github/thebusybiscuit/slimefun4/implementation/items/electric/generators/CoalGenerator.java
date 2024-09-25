package io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

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
        registerFuel(new MachineFuel(80, ItemStack.of(Material.COAL_BLOCK)));
        registerFuel(new MachineFuel(12, ItemStack.of(Material.BLAZE_ROD)));
        registerFuel(new MachineFuel(20, ItemStack.of(Material.DRIED_KELP_BLOCK)));

        // Boats
        for (Material mat : Tag.ITEMS_BOATS.getValues()) {
            registerFuel(new MachineFuel(5, ItemStack.of(mat)));
        }

        // Coal & Charcoal
        registerFuel(new MachineFuel(8, ItemStack.of(Material.COAL)));
        registerFuel(new MachineFuel(8, ItemStack.of(Material.CHARCOAL)));

        // Logs
        for (Material mat : Tag.LOGS.getValues()) {
            registerFuel(new MachineFuel(4, ItemStack.of(mat)));
        }

        // Wooden Planks
        for (Material mat : Tag.PLANKS.getValues()) {
            registerFuel(new MachineFuel(1, ItemStack.of(mat)));
        }

        // Wooden Slabs
        for (Material mat : Tag.WOODEN_SLABS.getValues()) {
            registerFuel(new MachineFuel(1, ItemStack.of(mat)));
        }

        // Wooden Buttons
        for (Material mat : Tag.WOODEN_BUTTONS.getValues()) {
            registerFuel(new MachineFuel(1, ItemStack.of(mat)));
        }

        // Wooden Fences
        for (Material mat : Tag.WOODEN_FENCES.getValues()) {
            registerFuel(new MachineFuel(1, ItemStack.of(mat)));
        }

        // wooden Trapdoors
        for (Material mat : Tag.WOODEN_TRAPDOORS.getValues()) {
            registerFuel(new MachineFuel(3, ItemStack.of(mat)));
        }

        // Wooden Pressure Plates
        for (Material mat : Tag.WOODEN_PRESSURE_PLATES.getValues()) {
            registerFuel(new MachineFuel(2, ItemStack.of(mat)));
        }

        // Wooden Doors
        for (Material mat : Tag.WOODEN_DOORS.getValues()) {
            registerFuel(new MachineFuel(2, ItemStack.of(mat)));
        }

        // Signs
        for (Material mat : Tag.STANDING_SIGNS.getValues()) {
            registerFuel(new MachineFuel(2, ItemStack.of(mat)));
        }
    }

    @Nonnull
    @Override
    public ItemStack getProgressBar() {
        return ItemStack.of(Material.FLINT_AND_STEEL);
    }

}
