package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

public abstract class ElectrifiedCrucible extends AContainer {

	public ElectrifiedCrucible(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}
	
	@Override
	public void registerDefaultRecipes() {
		registerRecipe(10, new ItemStack[] {new ItemStack(Material.BUCKET), new ItemStack(Material.COBBLESTONE, 16)}, new ItemStack[]{new ItemStack(Material.LAVA_BUCKET)});
		registerRecipe(8, new ItemStack[] {new ItemStack(Material.BUCKET), new ItemStack(Material.TERRACOTTA, 12)}, new ItemStack[]{new ItemStack(Material.LAVA_BUCKET)});
		
		for (Material coloured_terracotta : MaterialCollections.getAllTerracottaColors()){
		    registerRecipe(8, new ItemStack[] {new ItemStack(Material.BUCKET), new ItemStack(coloured_terracotta, 12)}, new ItemStack[]{new ItemStack(Material.LAVA_BUCKET)});
        }
		
		registerRecipe(10, new ItemStack[] {new ItemStack(Material.BUCKET), new ItemStack(Material.OAK_LEAVES, 16)}, new ItemStack[]{new ItemStack(Material.WATER_BUCKET)});
		registerRecipe(10, new ItemStack[] {new ItemStack(Material.BUCKET), new ItemStack(Material.BIRCH_LEAVES, 16)}, new ItemStack[]{new ItemStack(Material.WATER_BUCKET)});
		registerRecipe(10, new ItemStack[] {new ItemStack(Material.BUCKET), new ItemStack(Material.SPRUCE_LEAVES, 16)}, new ItemStack[]{new ItemStack(Material.WATER_BUCKET)});
		registerRecipe(10, new ItemStack[] {new ItemStack(Material.BUCKET), new ItemStack(Material.JUNGLE_LEAVES, 16)}, new ItemStack[]{new ItemStack(Material.WATER_BUCKET)});
		registerRecipe(10, new ItemStack[] {new ItemStack(Material.BUCKET), new ItemStack(Material.ACACIA_LEAVES, 16)}, new ItemStack[]{new ItemStack(Material.WATER_BUCKET)});
		registerRecipe(10, new ItemStack[] {new ItemStack(Material.BUCKET), new ItemStack(Material.DARK_OAK_LEAVES, 16)}, new ItemStack[]{new ItemStack(Material.WATER_BUCKET)});
	}
	
	@Override
	public String getMachineIdentifier() {
		return "ELECTRIFIED_CRUCIBLE";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}
	
	@Override
	public String getInventoryTitle() {
		return "&4Electrified Crucible";
	}

}
