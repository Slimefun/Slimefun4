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

public class FoodFabricator extends AContainer {

    public FoodFabricator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(Material.WHEAT) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.WHEAT_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(Material.CARROT) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.CARROT_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(Material.POTATO) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.POTATO_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(XMaterial.WHEAT_SEEDS.parseMaterial()) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.SEEDS_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(XMaterial.BEETROOT.parseMaterial()) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.BEETROOT_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(XMaterial.MELON_SLICE.parseMaterial()) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.MELON_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(Material.APPLE) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.APPLE_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(XMaterial.DRIED_KELP.parseMaterial()) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.KELP_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(XMaterial.COCOA_BEANS.parseMaterial()) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.COCOA_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(XMaterial.SWEET_BERRIES.parseMaterial()) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.SWEET_BERRIES_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
        registerRecipe(12, new ItemStack[] { SlimefunItems.TIN_CAN.item(), new ItemStack(XMaterial.SEAGRASS.parseMaterial()) }, new ItemStack[] { new SlimefunItemStack(SlimefunItems.SEAGRASS_ORGANIC_FOOD, OrganicFood.OUTPUT).item() });
    }

    @Override
    public String getMachineIdentifier() {
        return "FOOD_FABRICATOR";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(XMaterial.GOLDEN_HOE.parseMaterial());
    }

}

