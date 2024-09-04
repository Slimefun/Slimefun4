package io.github.thebusybiscuit.slimefun4.implementation.items.electric.item_generators;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AItemGenerator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class CrystalGrower extends AItemGenerator {
    public CrystalGrower(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        addRecipe(20, new ItemStack(Material.AMETHYST_SHARD), new ItemStack(Material.AMETHYST_SHARD));
        //add these for testing
        addRecipe(20, new ItemStack(Material.GLASS), new ItemStack(Material.GLASS));
        addRecipe(20, new ItemStack(Material.GUNPOWDER), new ItemStack(Material.GUNPOWDER));
        addRecipe(20, new ItemStack(Material.DIRT), new ItemStack(Material.DIRT));
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.IRON_PICKAXE);
    }

    @Override
    public @Nonnull String getMachineIdentifier() {
        return "CRYSTAL_GROWER";
    }
}
