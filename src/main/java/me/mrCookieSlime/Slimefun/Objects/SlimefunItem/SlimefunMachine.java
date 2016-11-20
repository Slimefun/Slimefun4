package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.MultiBlock;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SlimefunMachine extends SlimefunItem {
	
	List<ItemStack[]> recipes;
	List<ItemStack> shownRecipes;
	Material trigger;

	public SlimefunMachine(Category category, ItemStack item, String name, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger) {
		super(category, item, name, RecipeType.MULTIBLOCK, recipe);
		this.recipes = new ArrayList<ItemStack[]>();
		this.shownRecipes = new ArrayList<ItemStack>();
		for (ItemStack i: machineRecipes) {
			this.shownRecipes.add(i);
		}
		this.trigger = trigger;
	}
	
	public SlimefunMachine(Category category, ItemStack item, String name, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger, boolean ghost) {
		super(category, item, name, RecipeType.MULTIBLOCK, recipe, ghost);
		this.recipes = new ArrayList<ItemStack[]>();
		this.shownRecipes = new ArrayList<ItemStack>();
		for (ItemStack i: machineRecipes) {
			this.shownRecipes.add(i);
		}
		this.trigger = trigger;
	}
	
	public SlimefunMachine(Category category, ItemStack item, String name, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger, String[] keys, Object[] values) {
		super(category, item, name, RecipeType.MULTIBLOCK, recipe, keys, values);
		this.recipes = new ArrayList<ItemStack[]>();
		this.shownRecipes = new ArrayList<ItemStack>();
		for (ItemStack i: machineRecipes) {
			this.shownRecipes.add(i);
		}
		this.trigger = trigger;
	}
	
	public List<ItemStack[]> getRecipes() {
		return this.recipes;
	}
	
	public List<ItemStack> getDisplayRecipes() {
		return this.shownRecipes;
	}
	
	public void addRecipe(ItemStack[] input, ItemStack output) {
		this.recipes.add(input);
		this.recipes.add(new ItemStack[] {output});
	}
	
	@Override
	public void create() {
		this.toMultiBlock().register();
	}
	
	@Override
	public void install() {
		for (ItemStack i: this.getDisplayRecipes()) {
			SlimefunItem item = SlimefunItem.getByItem(i);
			if (item == null) this.recipes.add(new ItemStack[] {i});
			else if (!SlimefunItem.isDisabled(i)) this.recipes.add(new ItemStack[] {i});
		}
	}
	
	public MultiBlock toMultiBlock() {
		List<Material> mats = new ArrayList<Material>();
		for (ItemStack i: this.recipe) {
			if (i == null) mats.add(null);
			else if (i.getType() == Material.CAULDRON_ITEM) mats.add(Material.CAULDRON);
			else if (i.getType() == Material.FLINT_AND_STEEL) mats.add(Material.FIRE);
			else mats.add(i.getType());
		}
		Material[] build = mats.toArray(new Material[mats.size()]);
		return new MultiBlock(build, this.trigger);
	}
	
	public Iterator<ItemStack[]> recipeIterator() {
		return this.recipes.iterator();
	}

}
