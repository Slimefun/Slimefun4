package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.Collection;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeMatchResult;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotHopperable;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

public class ElectricOreGrinder extends AContainer implements RecipeDisplayItem, NotHopperable {

    private boolean isLegacy = false;

    @ParametersAreNonnullByDefault
    public ElectricOreGrinder(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public Collection<RecipeCategory> getCraftedCategories() {
        return List.of(RecipeCategory.GRIND_STONE, RecipeCategory.ORE_CRUSHER);
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_ORE_GRINDER";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.IRON_PICKAXE);
    }

    @Override
    protected boolean onRecipeFound(Recipe recipe, RecipeMatchResult matchResult) {
        // TODO Auto-generated method stub
        return super.onRecipeFound(recipe, matchResult);
    }

}
