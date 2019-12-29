package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.MultiBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SlimefunMachine extends SlimefunItem implements RecipeDisplayItem {

	protected final List<ItemStack[]> recipes;
	protected final List<ItemStack> shownRecipes;
	protected final MultiBlock multiblock;

	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, BlockFace trigger) {
		super(category, item, id, RecipeType.MULTIBLOCK, recipe);
		this.recipes = new ArrayList<>();
		this.shownRecipes = new ArrayList<>();
		this.shownRecipes.addAll(Arrays.asList(machineRecipes));
		this.multiblock = new MultiBlock(this, convertItemStacksToMaterial(recipe), trigger);
	}

	public SlimefunMachine(Category category, SlimefunItemStack item, ItemStack[] recipe, ItemStack[] machineRecipes, BlockFace trigger) {
		super(category, item, RecipeType.MULTIBLOCK, recipe);
		this.recipes = new ArrayList<>();
		this.shownRecipes = new ArrayList<>();
		this.shownRecipes.addAll(Arrays.asList(machineRecipes));
		this.multiblock = new MultiBlock(this, convertItemStacksToMaterial(recipe), trigger);
	}

	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, BlockFace trigger, boolean ghost) {
		super(category, item, id, RecipeType.MULTIBLOCK, recipe, ghost);
		this.recipes = new ArrayList<>();
		this.shownRecipes = new ArrayList<>();
		this.shownRecipes.addAll(Arrays.asList(machineRecipes));
		this.multiblock = new MultiBlock(this, convertItemStacksToMaterial(recipe), trigger);
	}

	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, BlockFace trigger, String[] keys, Object[] values) {
		super(category, item, id, RecipeType.MULTIBLOCK, recipe, keys, values);
		this.recipes = new ArrayList<>();
		this.shownRecipes = new ArrayList<>();
		this.shownRecipes.addAll(Arrays.asList(machineRecipes));
		this.multiblock = new MultiBlock(this, convertItemStacksToMaterial(recipe), trigger);
	}

	public List<ItemStack[]> getRecipes() {
		return recipes;
	}

	@Override
	public List<ItemStack> getDisplayRecipes() {
		return shownRecipes;
	}

	public void addRecipe(ItemStack[] input, ItemStack output) {
		if (output == null) throw new IllegalArgumentException("Recipes must have an Output!");
		
		recipes.add(input);
		recipes.add(new ItemStack[] {output});
	}

	@Override
	public void postRegister() {
		multiblock.register();
	}

	@Override
	public void install() {
		for (ItemStack recipeItem : shownRecipes) {
			SlimefunItem item = SlimefunItem.getByItem(recipeItem);
			
			if (item == null || !SlimefunItem.isDisabled(recipeItem)) {
				this.recipes.add(new ItemStack[] {recipeItem});
			}
		}
	}

	private static Material[] convertItemStacksToMaterial(ItemStack[] items) {
		List<Material> mats = new ArrayList<>();
		for (ItemStack item : items) {
			if (item == null) mats.add(null);
			else if (item.getType() == Material.FLINT_AND_STEEL) mats.add(Material.FIRE);
			else mats.add(item.getType());
		}

		return mats.toArray(new Material[0]);
	}
	
	@Override
	protected boolean areItemHandlersPrivate() {
		return true;
	}

	public Iterator<ItemStack[]> recipeIterator() {
		return this.recipes.iterator();
	}
	
	public MultiBlock getMultiBlock() {
		return multiblock;
	}
}
