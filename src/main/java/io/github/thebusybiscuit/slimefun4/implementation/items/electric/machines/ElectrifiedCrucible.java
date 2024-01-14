package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

public class ElectrifiedCrucible extends AContainer {

    @Deprecated
    @ParametersAreNonnullByDefault
    public ElectrifiedCrucible(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        this(itemGroup, item, recipeType.asRecipeCategory(), recipe);
    }
    
    @ParametersAreNonnullByDefault
    public ElectrifiedCrucible(ItemGroup itemGroup, SlimefunItemStack item, RecipeCategory recipeCategory, ItemStack[] recipe) {
        super(itemGroup, item, recipeCategory, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.COBBLESTONE, 16) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
        registerRecipe(8, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.NETHERRACK, 16) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
        registerRecipe(8, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.STONE, 12) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
        registerRecipe(8, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.TERRACOTTA, 12) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
        registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.OBSIDIAN) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });

        for (Material terracotta : SlimefunTag.TERRACOTTA.getValues()) {
            registerRecipe(8, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(terracotta, 12) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
        }

        for (Material leaves : Tag.LEAVES.getValues()) {
            registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(leaves, 16) }, new ItemStack[] { new ItemStack(Material.WATER_BUCKET) });
        }

        registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.BLACKSTONE, 8) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
        registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.BASALT, 12) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.COBBLED_DEEPSLATE, 12) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
            registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.DEEPSLATE, 10) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
            registerRecipe(10, new ItemStack[] { new ItemStack(Material.BUCKET), new ItemStack(Material.TUFF, 8) }, new ItemStack[] { new ItemStack(Material.LAVA_BUCKET) });
        }
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIFIED_CRUCIBLE";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }

}
