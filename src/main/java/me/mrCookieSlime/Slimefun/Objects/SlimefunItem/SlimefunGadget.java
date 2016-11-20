package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.inventory.ItemStack;

public class SlimefunGadget extends SlimefunItem {
	
	List<ItemStack[]> recipes;
	List<ItemStack> display_recipes;

	public SlimefunGadget(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, ItemStack[] machineRecipes) {
		super(category, item, name, recipeType, recipe);
		this.recipes = new ArrayList<ItemStack[]>();
		this.display_recipes = new ArrayList<ItemStack>();
		for (ItemStack i: machineRecipes) {
			this.recipes.add(new ItemStack[] {i});
			this.display_recipes.add(i);
		}
	}
	
	public SlimefunGadget(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, ItemStack[] machineRecipes, String[] keys, Object[] values) {
		super(category, item, name, recipeType, recipe, keys, values);
		this.recipes = new ArrayList<ItemStack[]>();
		this.display_recipes = new ArrayList<ItemStack>();
		for (ItemStack i: machineRecipes) {
			this.recipes.add(new ItemStack[] {i});
			this.display_recipes.add(i);
		}
	}
	
	public List<ItemStack[]> getRecipes() {
		return this.recipes;
	}
	
	public List<ItemStack> getDisplayRecipes() {
		return this.display_recipes;
	}
	
	public void addRecipe(ItemStack input, ItemStack output) {
		this.recipes.add(new ItemStack[] {input});
		this.recipes.add(new ItemStack[] {output});
		this.display_recipes.add(input);
		this.display_recipes.add(output);
	}
}
