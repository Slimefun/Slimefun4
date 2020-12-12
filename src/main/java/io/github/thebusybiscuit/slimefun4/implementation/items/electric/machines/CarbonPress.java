package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class CarbonPress extends AContainer implements RecipeDisplayItem {

    public CarbonPress(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(15, new ItemStack[] { new ItemStack(Material.CHARCOAL, 4) }, new ItemStack[] { new ItemStack(Material.COAL) });
        registerRecipe(20, new ItemStack[] { new ItemStack(Material.COAL, 8) }, new ItemStack[] { SlimefunItems.CARBON });
        registerRecipe(30, new ItemStack[] { new CustomItem(SlimefunItems.CARBON, 4) }, new ItemStack[] { SlimefunItems.COMPRESSED_CARBON });
        registerRecipe(60, new ItemStack[] { SlimefunItems.CARBON_CHUNK, SlimefunItems.SYNTHETIC_DIAMOND }, new ItemStack[] { SlimefunItems.RAW_CARBONADO });
        registerRecipe(60, new ItemStack[] { SlimefunItems.CARBON_CHUNK }, new ItemStack[] { SlimefunItems.SYNTHETIC_DIAMOND });
        registerRecipe(90, new ItemStack[] { SlimefunItems.RAW_CARBONADO }, new ItemStack[] { SlimefunItems.CARBONADO });
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
