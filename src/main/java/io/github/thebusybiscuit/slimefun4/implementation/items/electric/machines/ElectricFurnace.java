package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public abstract class ElectricFurnace extends AContainer {

    public ElectricFurnace(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public void registerDefaultRecipes() {
        SlimefunPlugin.getMinecraftRecipeService().subscribe(snapshot -> {
            for (FurnaceRecipe recipe : snapshot.getRecipes(FurnaceRecipe.class)) {
                RecipeChoice choice = recipe.getInputChoice();

                if (choice instanceof MaterialChoice) {
                    for (Material input : ((MaterialChoice) choice).getChoices()) {
                        registerRecipe(4, new ItemStack[] { new ItemStack(input) }, new ItemStack[] { recipe.getResult() });
                    }
                }
            }
        });
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_FURNACE";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }

}
