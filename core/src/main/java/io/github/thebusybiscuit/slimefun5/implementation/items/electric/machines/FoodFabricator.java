package io.github.thebusybiscuit.slimefun5.implementation.items.electric.machines;

import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.implementation.items.misc.OrganicFood;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

public class FoodFabricator extends AContainer {

    public FoodFabricator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(Material.WHEAT) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.WHEAT_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(Material.CARROT) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.CARROT_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(Material.POTATO) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.POTATO_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), MaterialCompat.stack(XMaterial.WHEAT_SEEDS) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.SEEDS_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), MaterialCompat.stack(XMaterial.BEETROOT) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.BEETROOT_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), MaterialCompat.stack(XMaterial.MELON_SLICE) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.MELON_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(Material.APPLE) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.APPLE_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), MaterialCompat.stack(XMaterial.DRIED_KELP) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.KELP_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), MaterialCompat.stack(XMaterial.COCOA_BEANS) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.COCOA_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), MaterialCompat.stack(XMaterial.SWEET_BERRIES) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.SWEET_BERRIES_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), MaterialCompat.stack(XMaterial.SEAGRASS) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.SEAGRASS_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
    }

    @Override
    public String getMachineIdentifier() {
        return "FOOD_FABRICATOR";
    }

    @Override
    public ItemStack getProgressBar() {
        return MaterialCompat.stack(XMaterial.GOLDEN_HOE);
    }

}

