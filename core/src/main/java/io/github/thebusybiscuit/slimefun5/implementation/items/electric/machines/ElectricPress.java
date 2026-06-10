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
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

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
        addRecipe(5, new ItemStack(Material.GLASS), MaterialCompat.stack(XMaterial.GLASS_PANE, 3));
        addRecipe(4, MaterialCompat.stack(XMaterial.SNOWBALL, 4), new ItemStack(Material.SNOW_BLOCK));
        addRecipe(4, new ItemStack(Material.MAGMA_CREAM, 4), MaterialCompat.stack(XMaterial.MAGMA_BLOCK));
        addRecipe(4, new ItemStack(Material.SLIME_BALL, 9), new ItemStack(Material.SLIME_BLOCK));

        addRecipe(3, MaterialCompat.stack(XMaterial.DRIED_KELP, 9), MaterialCompat.stack(XMaterial.DRIED_KELP_BLOCK));
        addRecipe(3, MaterialCompat.stack(XMaterial.BONE_MEAL, 9), MaterialCompat.stack(XMaterial.BONE_BLOCK));
        addRecipe(3, new ItemStack(Material.CLAY_BALL, 4), new ItemStack(Material.CLAY));
        addRecipe(3, new ItemStack(Material.BRICK, 4), MaterialCompat.stack(XMaterial.BRICKS));

        addRecipe(6, SlimefunItems.COPPER_INGOT.item(), CustomItemStack.create(SlimefunItems.COPPER_WIRE.item(), 3));
        addRecipe(16, new SlimefunItemStack(SlimefunItems.STEEL_INGOT, 8), SlimefunItems.STEEL_PLATE);
        addRecipe(18, new SlimefunItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 8), SlimefunItems.REINFORCED_PLATE);

        addRecipe(8, MaterialCompat.stack(XMaterial.NETHER_WART), CustomItemStack.create(SlimefunItems.MAGIC_LUMP_1.item(), 2));
        addRecipe(10, new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_1, 4), SlimefunItems.MAGIC_LUMP_2);
        addRecipe(12, new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_2, 4), SlimefunItems.MAGIC_LUMP_3);

        addRecipe(10, MaterialCompat.stack(XMaterial.ENDER_EYE), CustomItemStack.create(SlimefunItems.ENDER_LUMP_1.item(), 2));
        addRecipe(12, new SlimefunItemStack(SlimefunItems.ENDER_LUMP_1, 4), SlimefunItems.ENDER_LUMP_2);
        addRecipe(14, new SlimefunItemStack(SlimefunItems.ENDER_LUMP_2, 4), SlimefunItems.ENDER_LUMP_3);

        addRecipe(18, new SlimefunItemStack(SlimefunItems.TINY_URANIUM, 9), SlimefunItems.SMALL_URANIUM);
        addRecipe(24, new SlimefunItemStack(SlimefunItems.SMALL_URANIUM, 4), SlimefunItems.URANIUM);

        addRecipe(4, new ItemStack(Material.QUARTZ, 4), new ItemStack(Material.QUARTZ_BLOCK));
        addRecipe(4, MaterialCompat.stack(XMaterial.IRON_NUGGET, 9), new ItemStack(Material.IRON_INGOT));
        addRecipe(4, new ItemStack(Material.GOLD_NUGGET, 9), new ItemStack(Material.GOLD_INGOT));
        addRecipe(4, new ItemStack(Material.COAL, 9), new ItemStack(Material.COAL_BLOCK));
        addRecipe(4, new ItemStack(Material.SAND, 4), new ItemStack(Material.SANDSTONE));
        addRecipe(4, MaterialCompat.stack(XMaterial.RED_SAND, 4), new ItemStack(Material.RED_SANDSTONE));

        addRecipe(5, new ItemStack(Material.IRON_INGOT, 9), new ItemStack(Material.IRON_BLOCK));
        addRecipe(5, new ItemStack(Material.GOLD_INGOT, 9), new ItemStack(Material.GOLD_BLOCK));

        addRecipe(6, new ItemStack(Material.REDSTONE, 9), new ItemStack(Material.REDSTONE_BLOCK));
        addRecipe(6, MaterialCompat.stack(XMaterial.LAPIS_LAZULI, 9), new ItemStack(Material.LAPIS_BLOCK));

        addRecipe(8, new ItemStack(Material.EMERALD, 9), new ItemStack(Material.EMERALD_BLOCK));
        addRecipe(8, new ItemStack(Material.DIAMOND, 9), new ItemStack(Material.DIAMOND_BLOCK));

        addRecipe(16, MaterialCompat.stack(XMaterial.NETHERITE_INGOT, 9), MaterialCompat.stack(XMaterial.NETHERITE_BLOCK));

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            addRecipe(4, MaterialCompat.stack(XMaterial.AMETHYST_SHARD, 4), MaterialCompat.stack(XMaterial.AMETHYST_BLOCK));

            addRecipe(5, MaterialCompat.stack(XMaterial.COPPER_INGOT, 9), MaterialCompat.stack(XMaterial.COPPER_BLOCK));
            addRecipe(5, MaterialCompat.stack(XMaterial.RAW_IRON, 9), MaterialCompat.stack(XMaterial.RAW_IRON_BLOCK));
            addRecipe(5, MaterialCompat.stack(XMaterial.RAW_GOLD, 9), MaterialCompat.stack(XMaterial.RAW_GOLD_BLOCK));
            addRecipe(5, MaterialCompat.stack(XMaterial.RAW_COPPER, 9), MaterialCompat.stack(XMaterial.RAW_COPPER_BLOCK));
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

