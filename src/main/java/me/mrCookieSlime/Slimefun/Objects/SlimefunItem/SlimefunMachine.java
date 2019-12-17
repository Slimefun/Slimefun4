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

public class SlimefunMachine extends SlimefunItem implements RecipeDisplayItem {

	protected final List<ItemStack[]> recipes;
	protected final List<ItemStack> shownRecipes;
	protected final BlockFace trigger;

	@Deprecated
	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger) {
		this(category, item, id, recipe, machineRecipes, convertTriggerMaterial(recipe, trigger));
	}

	@Deprecated
	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger, boolean ghost) {
		this(category, item, id, recipe, machineRecipes, convertTriggerMaterial(recipe, trigger), ghost);
	}

	@Deprecated
	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger, String[] keys, Object[] values) {
		this(category, item, id, recipe, machineRecipes, convertTriggerMaterial(recipe, trigger), keys, values);
	}

	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, BlockFace trigger) {
		super(category, item, id, RecipeType.MULTIBLOCK, recipe);
		this.recipes = new ArrayList<>();
		this.shownRecipes = new ArrayList<>();
		this.shownRecipes.addAll(Arrays.asList(machineRecipes));
		this.trigger = trigger;
	}

	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, BlockFace trigger, boolean ghost) {
		super(category, item, id, RecipeType.MULTIBLOCK, recipe, ghost);
		this.recipes = new ArrayList<>();
		this.shownRecipes = new ArrayList<>();
		this.shownRecipes.addAll(Arrays.asList(machineRecipes));
		this.trigger = trigger;
	}

	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, BlockFace trigger, String[] keys, Object[] values) {
		super(category, item, id, RecipeType.MULTIBLOCK, recipe, keys, values);
		this.recipes = new ArrayList<>();
		this.shownRecipes = new ArrayList<>();
		this.shownRecipes.addAll(Arrays.asList(machineRecipes));
		this.trigger = trigger;
	}

	public List<ItemStack[]> getRecipes() {
		return recipes;
	}

	@Override
	public List<ItemStack> getDisplayRecipes() {
		return shownRecipes;
	}

	public void addRecipe(ItemStack[] input, ItemStack output) {
		recipes.add(input);
		recipes.add(new ItemStack[] {output});
	}

	@Override
	public void postRegister() {
		this.toMultiBlock().register();
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

		return mats.toArray(new Material[mats.size()]);
	}

	public MultiBlock toMultiBlock() {
		return new MultiBlock(convertItemStacksToMaterial(this.getRecipe()), this.trigger);
	}

	public Iterator<ItemStack[]> recipeIterator() {
		return this.recipes.iterator();
	}

	@Deprecated
	private static BlockFace convertTriggerMaterial(ItemStack[] recipe, Material trigger) {
		return MultiBlock.convertTriggerMaterialToBlockFace(convertItemStacksToMaterial(recipe), trigger);
	}
}
