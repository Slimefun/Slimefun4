package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public abstract class ElectricPress extends AContainer implements RecipeDisplayItem {

    public ElectricPress(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }
	
	@Override
	public void registerDefaultRecipes() {
		registerRecipe(new MachineRecipe(4, new ItemStack[] {new CustomItem(SlimefunItems.STONE_CHUNK, 3)}, new ItemStack[] {new ItemStack(Material.COBBLESTONE)}));
		registerRecipe(new MachineRecipe(4, new ItemStack[] {new ItemStack(Material.FLINT, 6)}, new ItemStack[] {new ItemStack(Material.COBBLESTONE)}));
		
		registerRecipe(new MachineRecipe(6, new ItemStack[] {SlimefunItems.COPPER_INGOT}, new ItemStack[] {new CustomItem(SlimefunItems.COPPER_WIRE, 3)}));
		registerRecipe(new MachineRecipe(16, new ItemStack[] {new CustomItem(SlimefunItems.STEEL_INGOT, 8)}, new ItemStack[] {SlimefunItems.STEEL_PLATE}));
		registerRecipe(new MachineRecipe(18, new ItemStack[] {new CustomItem(SlimefunItems.REINFORCED_ALLOY_INGOT, 8)}, new ItemStack[] {SlimefunItems.REINFORCED_PLATE}));
		
		registerRecipe(new MachineRecipe(8, new ItemStack[] {new ItemStack(Material.NETHER_WART)}, new ItemStack[] {new CustomItem(SlimefunItems.MAGIC_LUMP_1, 2)}));
		registerRecipe(new MachineRecipe(10, new ItemStack[] {new CustomItem(SlimefunItems.MAGIC_LUMP_1, 4)}, new ItemStack[] {SlimefunItems.MAGIC_LUMP_2}));
		registerRecipe(new MachineRecipe(12, new ItemStack[] {new CustomItem(SlimefunItems.MAGIC_LUMP_2, 4)}, new ItemStack[] {SlimefunItems.MAGIC_LUMP_3}));
		
		registerRecipe(new MachineRecipe(10, new ItemStack[] {new ItemStack(Material.ENDER_EYE)}, new ItemStack[] {new CustomItem(SlimefunItems.ENDER_LUMP_1, 2)}));
		registerRecipe(new MachineRecipe(12, new ItemStack[] {new CustomItem(SlimefunItems.ENDER_LUMP_1, 4)}, new ItemStack[] {SlimefunItems.ENDER_LUMP_2}));
		registerRecipe(new MachineRecipe(14, new ItemStack[] {new CustomItem(SlimefunItems.ENDER_LUMP_2, 4)}, new ItemStack[] {SlimefunItems.ENDER_LUMP_3}));
		
		registerRecipe(new MachineRecipe(18, new ItemStack[] {new CustomItem(SlimefunItems.TINY_URANIUM, 9)}, new ItemStack[] {SlimefunItems.SMALL_URANIUM}));
		registerRecipe(new MachineRecipe(24, new ItemStack[] {new CustomItem(SlimefunItems.SMALL_URANIUM, 4)}, new ItemStack[] {SlimefunItems.URANIUM}));
		
		registerRecipe(new MachineRecipe(4, new ItemStack[] {new ItemStack(Material.QUARTZ, 4)}, new ItemStack[] {new ItemStack(Material.QUARTZ_BLOCK)}));
		registerRecipe(new MachineRecipe(4, new ItemStack[] {new ItemStack(Material.IRON_NUGGET, 9)}, new ItemStack[] {new ItemStack(Material.IRON_INGOT)}));
		registerRecipe(new MachineRecipe(4, new ItemStack[] {new ItemStack(Material.GOLD_NUGGET, 9)}, new ItemStack[] {new ItemStack(Material.GOLD_INGOT)}));
		registerRecipe(new MachineRecipe(4, new ItemStack[] {new ItemStack(Material.COAL, 9)}, new ItemStack[] {new ItemStack(Material.COAL_BLOCK)}));
		
		registerRecipe(new MachineRecipe(5, new ItemStack[] {new ItemStack(Material.IRON_INGOT, 9)}, new ItemStack[] {new ItemStack(Material.IRON_BLOCK)}));
		registerRecipe(new MachineRecipe(5, new ItemStack[] {new ItemStack(Material.GOLD_INGOT, 9)}, new ItemStack[] {new ItemStack(Material.GOLD_BLOCK)}));
		
		registerRecipe(new MachineRecipe(6, new ItemStack[] {new ItemStack(Material.REDSTONE, 9)}, new ItemStack[] {new ItemStack(Material.REDSTONE_BLOCK)}));
		registerRecipe(new MachineRecipe(6, new ItemStack[] {new ItemStack(Material.LAPIS_LAZULI, 9)}, new ItemStack[] {new ItemStack(Material.LAPIS_BLOCK)}));
		
		registerRecipe(new MachineRecipe(8, new ItemStack[] {new ItemStack(Material.EMERALD, 9)}, new ItemStack[] {new ItemStack(Material.EMERALD_BLOCK)}));
		registerRecipe(new MachineRecipe(8, new ItemStack[] {new ItemStack(Material.DIAMOND, 9)}, new ItemStack[] {new ItemStack(Material.DIAMOND_BLOCK)}));
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
