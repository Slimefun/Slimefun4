package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

@Deprecated
public class SlimefunGadget extends SlimefunItem implements RecipeDisplayItem {
	
	private List<ItemStack[]> recipes;
	private List<ItemStack> displayRecipes;

	public SlimefunGadget(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack[] machineRecipes) {
		this(category, item, id, recipeType, recipe, machineRecipes, null, null);
	}

	public SlimefunGadget(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack[] machineRecipes) {
		this(category, item, item.getItemID(), recipeType, recipe, machineRecipes, null, null);
	}
	
	public SlimefunGadget(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack[] machineRecipes, String[] keys, Object[] values) {
		super(category, item, id, recipeType, recipe, keys, values);
		this.recipes = new ArrayList<>();
		this.displayRecipes = new ArrayList<>();
		
		for (ItemStack i: machineRecipes) {
			this.recipes.add(new ItemStack[] {i});
			this.displayRecipes.add(i);
		}
	}
	
	public List<ItemStack[]> getRecipes() {
		return this.recipes;
	}
	
	@Override
	public List<ItemStack> getDisplayRecipes() {
		return this.displayRecipes;
	}
	
	public void addRecipe(ItemStack input, ItemStack output) {
		this.recipes.add(new ItemStack[] {input});
		this.recipes.add(new ItemStack[] {output});
		this.displayRecipes.add(input);
		this.displayRecipes.add(output);
	}
}
