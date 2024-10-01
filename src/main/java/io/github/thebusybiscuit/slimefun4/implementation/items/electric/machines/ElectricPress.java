package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.utils.compatibility.StackResolver;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

/**
 * The {@link ElectricPress} is a pretty simple electrical machine.
 * It allows you to compact items into their block variant, e.g. 9 diamonds into a diamond block.
 * 
 * @author TheBusyBiscuit
 *
 */
public class ElectricPress extends AContainer implements RecipeDisplayItem {

    @ParametersAreNonnullByDefault
    public ElectricPress(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        addRecipe(4, new SlimefunItemStack(SlimefunItems.STONE_CHUNK, 3), StackResolver.of(Material.COBBLESTONE));
        addRecipe(4, StackResolver.of(Material.FLINT, 6), StackResolver.of(Material.COBBLESTONE));
        addRecipe(5, StackResolver.of(Material.GLASS), StackResolver.of(Material.GLASS_PANE, 3));
        addRecipe(4, StackResolver.of(Material.SNOWBALL, 4), StackResolver.of(Material.SNOW_BLOCK));
        addRecipe(4, StackResolver.of(Material.MAGMA_CREAM, 4), StackResolver.of(Material.MAGMA_BLOCK));
        addRecipe(4, StackResolver.of(Material.SLIME_BALL, 9), StackResolver.of(Material.SLIME_BLOCK));

        addRecipe(3, StackResolver.of(Material.DRIED_KELP, 9), StackResolver.of(Material.DRIED_KELP_BLOCK));
        addRecipe(3, StackResolver.of(Material.BONE_MEAL, 9), StackResolver.of(Material.BONE_BLOCK));
        addRecipe(3, StackResolver.of(Material.CLAY_BALL, 4), StackResolver.of(Material.CLAY));
        addRecipe(3, StackResolver.of(Material.BRICK, 4), StackResolver.of(Material.BRICKS));

        addRecipe(6, SlimefunItems.COPPER_INGOT, new CustomItemStack(SlimefunItems.COPPER_WIRE, 3));
        addRecipe(16, new SlimefunItemStack(SlimefunItems.STEEL_INGOT, 8), SlimefunItems.STEEL_PLATE);
        addRecipe(18, new SlimefunItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 8), SlimefunItems.REINFORCED_PLATE);

        addRecipe(8, StackResolver.of(Material.NETHER_WART), new CustomItemStack(SlimefunItems.MAGIC_LUMP_1, 2));
        addRecipe(10, new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_1, 4), SlimefunItems.MAGIC_LUMP_2);
        addRecipe(12, new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_2, 4), SlimefunItems.MAGIC_LUMP_3);

        addRecipe(10, StackResolver.of(Material.ENDER_EYE), new CustomItemStack(SlimefunItems.ENDER_LUMP_1, 2));
        addRecipe(12, new SlimefunItemStack(SlimefunItems.ENDER_LUMP_1, 4), SlimefunItems.ENDER_LUMP_2);
        addRecipe(14, new SlimefunItemStack(SlimefunItems.ENDER_LUMP_2, 4), SlimefunItems.ENDER_LUMP_3);

        addRecipe(18, new SlimefunItemStack(SlimefunItems.TINY_URANIUM, 9), SlimefunItems.SMALL_URANIUM);
        addRecipe(24, new SlimefunItemStack(SlimefunItems.SMALL_URANIUM, 4), SlimefunItems.URANIUM);

        addRecipe(4, StackResolver.of(Material.QUARTZ, 4), StackResolver.of(Material.QUARTZ_BLOCK));
        addRecipe(4, StackResolver.of(Material.IRON_NUGGET, 9), StackResolver.of(Material.IRON_INGOT));
        addRecipe(4, StackResolver.of(Material.GOLD_NUGGET, 9), StackResolver.of(Material.GOLD_INGOT));
        addRecipe(4, StackResolver.of(Material.COAL, 9), StackResolver.of(Material.COAL_BLOCK));
        addRecipe(4, StackResolver.of(Material.SAND, 4), StackResolver.of(Material.SANDSTONE));
        addRecipe(4, StackResolver.of(Material.RED_SAND, 4), StackResolver.of(Material.RED_SANDSTONE));

        addRecipe(5, StackResolver.of(Material.IRON_INGOT, 9), StackResolver.of(Material.IRON_BLOCK));
        addRecipe(5, StackResolver.of(Material.GOLD_INGOT, 9), StackResolver.of(Material.GOLD_BLOCK));

        addRecipe(6, StackResolver.of(Material.REDSTONE, 9), StackResolver.of(Material.REDSTONE_BLOCK));
        addRecipe(6, StackResolver.of(Material.LAPIS_LAZULI, 9), StackResolver.of(Material.LAPIS_BLOCK));

        addRecipe(8, StackResolver.of(Material.EMERALD, 9), StackResolver.of(Material.EMERALD_BLOCK));
        addRecipe(8, StackResolver.of(Material.DIAMOND, 9), StackResolver.of(Material.DIAMOND_BLOCK));

        addRecipe(16, StackResolver.of(Material.NETHERITE_INGOT, 9), StackResolver.of(Material.NETHERITE_BLOCK));

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            addRecipe(4, StackResolver.of(Material.AMETHYST_SHARD, 4), StackResolver.of(Material.AMETHYST_BLOCK));

            addRecipe(5, StackResolver.of(Material.COPPER_INGOT, 9), StackResolver.of(Material.COPPER_BLOCK));
            addRecipe(5, StackResolver.of(Material.RAW_IRON, 9), StackResolver.of(Material.RAW_IRON_BLOCK));
            addRecipe(5, StackResolver.of(Material.RAW_GOLD, 9), StackResolver.of(Material.RAW_GOLD_BLOCK));
            addRecipe(5, StackResolver.of(Material.RAW_COPPER, 9), StackResolver.of(Material.RAW_COPPER_BLOCK));
        }
    }

    @ParametersAreNonnullByDefault
    private void addRecipe(int seconds, ItemStack input, ItemStack output) {
        registerRecipe(seconds, new ItemStack[] { input }, new ItemStack[] { output });
    }

    @Override
    public ItemStack getProgressBar() {
        return StackResolver.of(Material.IRON_HOE);

    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_PRESS";

    }
}
