package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.*;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.MultiBlock;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SlimefunMachine extends SlimefunItem {
	
	private List<ItemStack[]> recipes;
	private List<ItemStack> shownRecipes;
	private Material trigger;
	//Adjacent blockfaces for iterative output chest checks
	private static final BlockFace[] outputFaces = {
		    BlockFace.UP,
		    BlockFace.NORTH,
		    BlockFace.EAST,
		    BlockFace.SOUTH,
		    BlockFace.WEST
		};

	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger) {
		super(category, item, id, RecipeType.MULTIBLOCK, recipe);
		this.recipes = new ArrayList<>();
		this.shownRecipes = new ArrayList<>();
		this.shownRecipes.addAll(Arrays.asList(machineRecipes));
		this.trigger = trigger;
	}
	
	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger, boolean ghost) {
		super(category, item, id, RecipeType.MULTIBLOCK, recipe, ghost);
		this.recipes = new ArrayList<>();
		this.shownRecipes = new ArrayList<>();
		this.shownRecipes.addAll(Arrays.asList(machineRecipes));
		this.trigger = trigger;
	}
	
	public SlimefunMachine(Category category, ItemStack item, String id, ItemStack[] recipe, ItemStack[] machineRecipes, Material trigger, String[] keys, Object[] values) {
		super(category, item, id, RecipeType.MULTIBLOCK, recipe, keys, values);
		this.recipes = new ArrayList<>();
		this.shownRecipes = new ArrayList<>();
		this.shownRecipes.addAll(Arrays.asList(machineRecipes));
		this.trigger = trigger;
	}
	
	public List<ItemStack[]> getRecipes() {
		return this.recipes;
	}
	
	public List<ItemStack> getDisplayRecipes() {
		return this.shownRecipes;
	}
	
	public static BlockFace[] getOutputFaces() {
		return outputFaces;
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
		List<Material> mats = new ArrayList<>();
		for (ItemStack i: this.getRecipe()) {
			if (i == null) mats.add(null);
			else if (i.getType() == Material.FLINT_AND_STEEL) mats.add(Material.FIRE);
			else mats.add(i.getType());
		}
		Material[] build = mats.toArray(new Material[mats.size()]);
		return new MultiBlock(build, this.trigger);
	}
	
	public Iterator<ItemStack[]> recipeIterator() {
		return this.recipes.iterator();
	}
	
	// Overloaded method for finding a potential output chest. Fallbacks to the old system of putting the adding back into the dispenser.
	// Optional last argument Inventory placeCheckerInv is for multiblock machines that create a dummy inventory to check if there's a space for the adding,
	// i.e. Enhanced crafting table
	public static Inventory findValidOutputInv(ItemStack adding, Block dispBlock, Inventory dispInv) {
		return findValidOutputInv(adding, dispBlock, dispInv, dispInv);
	}
	
	public static Inventory findValidOutputInv(ItemStack product, Block dispBlock, Inventory dispInv, Inventory placeCheckerInv) {
		Inventory outputInv = null;
		for (BlockFace face : outputFaces) {
			Block potentialOutput = dispBlock.getRelative(face);
			String id = BlockStorage.checkID(potentialOutput);
			if (id != null && id.equals("OUTPUT_CHEST")) {
				// Found the output chest! Now, let's check if we can fit the product in it.
				Inventory inv = ((Container) potentialOutput.getState()).getInventory();
				if (InvUtils.fits(inv, product)) {
					// It fits! Let's set the inventory to that now.
					outputInv = inv;
					break;
				}
			}
		}
		// This if-clause will trigger if no suitable output chest was found. It's functionally the same as the old fit check for the dispenser, only refactored.
		if (outputInv == null && InvUtils.fits(placeCheckerInv, product)) outputInv = dispInv;	
		
		return outputInv; 
		
		
	}
	
}
