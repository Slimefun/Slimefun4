package io.github.thebusybiscuit.slimefun5.implementation.items.electric.machines;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;

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
        addRecipe(4, new SlimefunItemStack(SlimefunItems.STONE_CHUNK, 3).item(), new ItemStack(Material.COBBLESTONE));
        addRecipe(4, new ItemStack(Material.FLINT, 6), new ItemStack(Material.COBBLESTONE));
        addRecipe(5, new ItemStack(Material.GLASS), new ItemStack(XMaterial.GLASS_PANE.parseMaterial(), 3));
        addRecipe(4, new ItemStack(XMaterial.SNOWBALL.parseMaterial(), 4), new ItemStack(Material.SNOW_BLOCK));
        addRecipe(4, new ItemStack(Material.MAGMA_CREAM, 4), new ItemStack(XMaterial.MAGMA_BLOCK.parseMaterial()));
        addRecipe(4, new ItemStack(Material.SLIME_BALL, 9), new ItemStack(Material.SLIME_BLOCK));

        addRecipe(3, new ItemStack(XMaterial.DRIED_KELP.parseMaterial(), 9), new ItemStack(XMaterial.DRIED_KELP_BLOCK.parseMaterial()));
        addRecipe(3, new ItemStack(XMaterial.BONE_MEAL.parseMaterial(), 9), new ItemStack(XMaterial.BONE_BLOCK.parseMaterial()));
        addRecipe(3, new ItemStack(Material.CLAY_BALL, 4), new ItemStack(Material.CLAY));
        addRecipe(3, new ItemStack(Material.BRICK, 4), new ItemStack(XMaterial.BRICKS.parseMaterial()));

        addRecipe(6, SlimefunItems.COPPER_INGOT.item(), CustomItemStack.create(SlimefunItems.COPPER_WIRE.item(), 3));
        addRecipe(16, new SlimefunItemStack(SlimefunItems.STEEL_INGOT, 8), SlimefunItems.STEEL_PLATE);
        addRecipe(18, new SlimefunItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 8), SlimefunItems.REINFORCED_PLATE);

        addRecipe(8, new ItemStack(XMaterial.NETHER_WART.parseMaterial()), CustomItemStack.create(SlimefunItems.MAGIC_LUMP_1.item(), 2));
        addRecipe(10, new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_1, 4), SlimefunItems.MAGIC_LUMP_2);
        addRecipe(12, new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_2, 4), SlimefunItems.MAGIC_LUMP_3);

        addRecipe(10, new ItemStack(XMaterial.ENDER_EYE.parseMaterial()), CustomItemStack.create(SlimefunItems.ENDER_LUMP_1.item(), 2));
        addRecipe(12, new SlimefunItemStack(SlimefunItems.ENDER_LUMP_1, 4), SlimefunItems.ENDER_LUMP_2);
        addRecipe(14, new SlimefunItemStack(SlimefunItems.ENDER_LUMP_2, 4), SlimefunItems.ENDER_LUMP_3);

        addRecipe(18, new SlimefunItemStack(SlimefunItems.TINY_URANIUM, 9), SlimefunItems.SMALL_URANIUM);
        addRecipe(24, new SlimefunItemStack(SlimefunItems.SMALL_URANIUM, 4), SlimefunItems.URANIUM);

        addRecipe(4, new ItemStack(Material.QUARTZ, 4), new ItemStack(Material.QUARTZ_BLOCK));
        addRecipe(4, new ItemStack(XMaterial.IRON_NUGGET.parseMaterial(), 9), new ItemStack(Material.IRON_INGOT));
        addRecipe(4, new ItemStack(Material.GOLD_NUGGET, 9), new ItemStack(Material.GOLD_INGOT));
        addRecipe(4, new ItemStack(Material.COAL, 9), new ItemStack(Material.COAL_BLOCK));
        addRecipe(4, new ItemStack(Material.SAND, 4), new ItemStack(Material.SANDSTONE));
        addRecipe(4, new ItemStack(XMaterial.RED_SAND.parseMaterial(), 4), new ItemStack(Material.RED_SANDSTONE));

        addRecipe(5, new ItemStack(Material.IRON_INGOT, 9), new ItemStack(Material.IRON_BLOCK));
        addRecipe(5, new ItemStack(Material.GOLD_INGOT, 9), new ItemStack(Material.GOLD_BLOCK));

        addRecipe(6, new ItemStack(Material.REDSTONE, 9), new ItemStack(Material.REDSTONE_BLOCK));
        addRecipe(6, new ItemStack(XMaterial.LAPIS_LAZULI.parseMaterial(), 9), new ItemStack(Material.LAPIS_BLOCK));

        addRecipe(8, new ItemStack(Material.EMERALD, 9), new ItemStack(Material.EMERALD_BLOCK));
        addRecipe(8, new ItemStack(Material.DIAMOND, 9), new ItemStack(Material.DIAMOND_BLOCK));

        addRecipe(16, new ItemStack(XMaterial.NETHERITE_INGOT.parseMaterial(), 9), new ItemStack(XMaterial.NETHERITE_BLOCK.parseMaterial()));

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            addRecipe(4, new ItemStack(XMaterial.AMETHYST_SHARD.parseMaterial(), 4), new ItemStack(XMaterial.AMETHYST_BLOCK.parseMaterial()));

            addRecipe(5, new ItemStack(XMaterial.COPPER_INGOT.parseMaterial(), 9), new ItemStack(XMaterial.COPPER_BLOCK.parseMaterial()));
            addRecipe(5, new ItemStack(XMaterial.RAW_IRON.parseMaterial(), 9), new ItemStack(XMaterial.RAW_IRON_BLOCK.parseMaterial()));
            addRecipe(5, new ItemStack(XMaterial.RAW_GOLD.parseMaterial(), 9), new ItemStack(XMaterial.RAW_GOLD_BLOCK.parseMaterial()));
            addRecipe(5, new ItemStack(XMaterial.RAW_COPPER.parseMaterial(), 9), new ItemStack(XMaterial.RAW_COPPER_BLOCK.parseMaterial()));
        }
    }

    @ParametersAreNonnullByDefault
    private void addRecipe(int seconds, ItemStack input, ItemStack output) {
        registerRecipe(seconds, new ItemStack[] { input }, new ItemStack[] { output });
    }

    @ParametersAreNonnullByDefault
    private void addRecipe(int seconds, SlimefunItemStack sfItem1, SlimefunItemStack sfItem2) {
        registerRecipe(seconds, sfItem1.item(), sfItem2.item());
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.IRON_HOE);

    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_PRESS";

    }
}

