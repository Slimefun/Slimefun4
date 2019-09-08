package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.MultiBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;

public class SlimefunMachine extends SlimefunItem implements RecipeDisplayItem {
	
	private HashMap<ItemStack[], ItemStack> recipes;
	private Material trigger;

	protected List<ItemStack> shownRecipes;

	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger) {
		super(category, item, id, RecipeType.MULTIBLOCK, recipe);
		this.recipes = new HashMap<>();
		this.shownRecipes = new ArrayList<>();
		this.shownRecipes.addAll(Arrays.asList(machineRecipes));
		this.trigger = trigger;
	}
	
	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger, boolean ghost) {
		super(category, item, id, RecipeType.MULTIBLOCK, recipe, ghost);
		this.recipes = new HashMap<>();
		this.shownRecipes = new ArrayList<>();
		this.shownRecipes.addAll(Arrays.asList(machineRecipes));
		this.trigger = trigger;
	}
	
	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger, String[] keys, Object[] values) {
		super(category, item, id, RecipeType.MULTIBLOCK, recipe, keys, values);
		this.recipes = new HashMap<>();
		this.shownRecipes = new ArrayList<>();
		this.shownRecipes.addAll(Arrays.asList(machineRecipes));
		this.trigger = trigger;
	}
	
	public HashMap<ItemStack[], ItemStack> getRecipes() {
		return recipes;
	}
	
	@Override
	public List<ItemStack> getDisplayRecipes() {
		return shownRecipes;
	}
	
	public void addRecipe(ItemStack[] input, ItemStack output) {
		recipes.put(input, output);
	}
	
	@Override
	public void postRegister() {
		this.toMultiBlock().register();
	}

	/*
	*@Override
	*public void install() {
	*	for (ItemStack i: this.getDisplayRecipes()) {
	*		SlimefunItem item = SlimefunItem.getByItem(i);
	*		if (item == null || !SlimefunItem.isDisabled(i)) {
	*			recipes.put(new ItemStack[] {i});
	*		}
	*	}
	*}
	*/
	
	public MultiBlock toMultiBlock() {
		List<Material> mats = new ArrayList<>();
		for (ItemStack i: this.getRecipe()) {
			if (i == null) mats.add(null);
			else if (i.getType() == Material.FLINT_AND_STEEL) mats.add(Material.FIRE);
			else mats.add(i.getType());
		}
		
		Material[] build = mats.toArray(new Material[0]);
		return new MultiBlock(build, this.trigger);
	}
	
	public Iterator<Map.Entry<ItemStack[], ItemStack>> recipeIterator() {
		return this.recipes.entrySet().iterator();
	}
	
}
