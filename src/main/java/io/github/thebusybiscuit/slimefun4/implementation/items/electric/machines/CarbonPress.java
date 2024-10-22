package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

public class CarbonPress extends AContainer implements RecipeDisplayItem {

    @ParametersAreNonnullByDefault
    public CarbonPress(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(15, new ItemStack[] { new ItemStack(Material.CHARCOAL, 4) }, new ItemStack[] { new ItemStack(Material.COAL) });
        registerRecipe(20, new ItemStack[] { new ItemStack(Material.COAL, 8) }, new ItemStack[] { SlimefunItems.CARBON.getDelegate() });
        registerRecipe(180, new ItemStack[] { new ItemStack(Material.COAL_BLOCK, 8) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.CARBON, 9).getDelegate() });
        registerRecipe(30, new ItemStack[] { new CustomItemStack(SlimefunItems.CARBON.getDelegate(), 4) }, new ItemStack[] { SlimefunItems.COMPRESSED_CARBON.getDelegate() });
        registerRecipe(60, new ItemStack[] { SlimefunItems.CARBON_CHUNK.getDelegate(), SlimefunItems.SYNTHETIC_DIAMOND.getDelegate() }, new ItemStack[] { SlimefunItems.RAW_CARBONADO.getDelegate() });
        registerRecipe(60, new ItemStack[] { SlimefunItems.CARBON_CHUNK.getDelegate() }, new ItemStack[] { SlimefunItems.SYNTHETIC_DIAMOND.getDelegate() });
        registerRecipe(90, new ItemStack[] { SlimefunItems.RAW_CARBONADO.getDelegate() }, new ItemStack[] { SlimefunItems.CARBONADO.getDelegate() });
    }

    @Override
    public String getMachineIdentifier() {
        return "CARBON_PRESS";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

}
