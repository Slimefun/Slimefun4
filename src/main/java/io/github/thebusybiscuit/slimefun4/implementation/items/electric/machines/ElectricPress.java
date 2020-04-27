package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public abstract class ElectricPress extends AContainer implements RecipeDisplayItem {

    public ElectricPress(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        addRecipe(4, new CustomItem(SlimefunItems.STONE_CHUNK, 3), new ItemStack(Material.COBBLESTONE));
        addRecipe(4, new ItemStack(Material.FLINT, 6), new ItemStack(Material.COBBLESTONE));
        addRecipe(5, new ItemStack(Material.GLASS), new ItemStack(Material.GLASS_PANE, 3));
        addRecipe(4, new ItemStack(Material.SNOWBALL, 4), new ItemStack(Material.SNOW_BLOCK));
        addRecipe(4, new ItemStack(Material.MAGMA_CREAM, 4), new ItemStack(Material.MAGMA_BLOCK));

        addRecipe(6, SlimefunItems.COPPER_INGOT, new CustomItem(SlimefunItems.COPPER_WIRE, 3));
        addRecipe(16, new CustomItem(SlimefunItems.STEEL_INGOT, 8), SlimefunItems.STEEL_PLATE);
        addRecipe(16, new CustomItem(SlimefunItems.LEAD_INGOT, 8), SlimefunItems.LEAD_PLATE);
        addRecipe(18, new CustomItem(SlimefunItems.REINFORCED_ALLOY_INGOT, 8), SlimefunItems.REINFORCED_PLATE);

        addRecipe(8, new ItemStack(Material.NETHER_WART), new CustomItem(SlimefunItems.MAGIC_LUMP_1, 2));
        addRecipe(10, new CustomItem(SlimefunItems.MAGIC_LUMP_1, 4), SlimefunItems.MAGIC_LUMP_2);
        addRecipe(12, new CustomItem(SlimefunItems.MAGIC_LUMP_2, 4), SlimefunItems.MAGIC_LUMP_3);

        addRecipe(10, new ItemStack(Material.ENDER_EYE), new CustomItem(SlimefunItems.ENDER_LUMP_1, 2));
        addRecipe(12, new CustomItem(SlimefunItems.ENDER_LUMP_1, 4), SlimefunItems.ENDER_LUMP_2);
        addRecipe(14, new CustomItem(SlimefunItems.ENDER_LUMP_2, 4), SlimefunItems.ENDER_LUMP_3);

        addRecipe(18, new CustomItem(SlimefunItems.TINY_URANIUM, 9), SlimefunItems.SMALL_URANIUM);
        addRecipe(24, new CustomItem(SlimefunItems.SMALL_URANIUM, 4), SlimefunItems.URANIUM);

        addRecipe(4, new ItemStack(Material.QUARTZ, 4), new ItemStack(Material.QUARTZ_BLOCK));
        addRecipe(4, new ItemStack(Material.IRON_NUGGET, 9), new ItemStack(Material.IRON_INGOT));
        addRecipe(4, new ItemStack(Material.GOLD_NUGGET, 9), new ItemStack(Material.GOLD_INGOT));
        addRecipe(4, new ItemStack(Material.COAL, 9), new ItemStack(Material.COAL_BLOCK));

        addRecipe(5, new ItemStack(Material.IRON_INGOT, 9), new ItemStack(Material.IRON_BLOCK));
        addRecipe(5, new ItemStack(Material.GOLD_INGOT, 9), new ItemStack(Material.GOLD_BLOCK));

        addRecipe(6, new ItemStack(Material.REDSTONE, 9), new ItemStack(Material.REDSTONE_BLOCK));
        addRecipe(6, new ItemStack(Material.LAPIS_LAZULI, 9), new ItemStack(Material.LAPIS_BLOCK));

        addRecipe(8, new ItemStack(Material.EMERALD, 9), new ItemStack(Material.EMERALD_BLOCK));
        addRecipe(8, new ItemStack(Material.DIAMOND, 9), new ItemStack(Material.DIAMOND_BLOCK));
    }

    private void addRecipe(int seconds, ItemStack input, ItemStack output) {
        registerRecipe(seconds, new ItemStack[] { input }, new ItemStack[] { output });
    }

    @Override
    public String getInventoryTitle() {
        return "&eElectric Press";

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
