package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import io.github.thebusybiscuit.slimefun4.utils.multiversion.StackResolver;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

public class ElectrifiedCrucible extends AContainer {

    public ElectrifiedCrucible(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(10, new ItemStack[] { StackResolver.of(Material.BUCKET), StackResolver.of(Material.COBBLESTONE, 16) }, new ItemStack[] { StackResolver.of(Material.LAVA_BUCKET) });
        registerRecipe(8, new ItemStack[] { StackResolver.of(Material.BUCKET), StackResolver.of(Material.NETHERRACK, 16) }, new ItemStack[] { StackResolver.of(Material.LAVA_BUCKET) });
        registerRecipe(8, new ItemStack[] { StackResolver.of(Material.BUCKET), StackResolver.of(Material.STONE, 12) }, new ItemStack[] { StackResolver.of(Material.LAVA_BUCKET) });
        registerRecipe(8, new ItemStack[] { StackResolver.of(Material.BUCKET), StackResolver.of(Material.TERRACOTTA, 12) }, new ItemStack[] { StackResolver.of(Material.LAVA_BUCKET) });
        registerRecipe(10, new ItemStack[] { StackResolver.of(Material.BUCKET), StackResolver.of(Material.OBSIDIAN) }, new ItemStack[] { StackResolver.of(Material.LAVA_BUCKET) });

        for (Material terracotta : SlimefunTag.TERRACOTTA.getValues()) {
            registerRecipe(8, new ItemStack[] { StackResolver.of(Material.BUCKET), StackResolver.of(terracotta, 12) }, new ItemStack[] { StackResolver.of(Material.LAVA_BUCKET) });
        }

        for (Material leaves : Tag.LEAVES.getValues()) {
            registerRecipe(10, new ItemStack[] { StackResolver.of(Material.BUCKET), StackResolver.of(leaves, 16) }, new ItemStack[] { StackResolver.of(Material.WATER_BUCKET) });
        }

        registerRecipe(10, new ItemStack[] { StackResolver.of(Material.BUCKET), StackResolver.of(Material.BLACKSTONE, 8) }, new ItemStack[] { StackResolver.of(Material.LAVA_BUCKET) });
        registerRecipe(10, new ItemStack[] { StackResolver.of(Material.BUCKET), StackResolver.of(Material.BASALT, 12) }, new ItemStack[] { StackResolver.of(Material.LAVA_BUCKET) });

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            registerRecipe(10, new ItemStack[] { StackResolver.of(Material.BUCKET), StackResolver.of(Material.COBBLED_DEEPSLATE, 12) }, new ItemStack[] { StackResolver.of(Material.LAVA_BUCKET) });
            registerRecipe(10, new ItemStack[] { StackResolver.of(Material.BUCKET), StackResolver.of(Material.DEEPSLATE, 10) }, new ItemStack[] { StackResolver.of(Material.LAVA_BUCKET) });
            registerRecipe(10, new ItemStack[] { StackResolver.of(Material.BUCKET), StackResolver.of(Material.TUFF, 8) }, new ItemStack[] { StackResolver.of(Material.LAVA_BUCKET) });
        }
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIFIED_CRUCIBLE";
    }

    @Override
    public ItemStack getProgressBar() {
        return StackResolver.of(Material.FLINT_AND_STEEL);
    }

}
