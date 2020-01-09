package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

public abstract class AContainer extends SlimefunItem implements InventoryBlock {
	
	public static Map<Block, MachineRecipe> processing = new HashMap<>();
	public static Map<Block, Integer> progress = new HashMap<>();
	
	protected final List<MachineRecipe> recipes = new ArrayList<>();
	
	private static final int[] BORDER = {0, 1, 2, 3, 4, 5, 6, 7, 8, 13, 31, 36, 37, 38, 39, 40, 41, 42, 43, 44};
	private static final int[] BORDER_IN = {9, 10, 11, 12, 18, 21, 27, 28, 29, 30};
	private static final int[] BORDER_OUT = {14, 15, 16, 17, 23, 26, 32, 33, 34, 35};

	public AContainer(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
		
		createPreset(this, getInventoryTitle(), this::constructMenu);
		
		registerBlockHandler(id, (p, b, tool, reason) -> {
			BlockMenu inv = BlockStorage.getInventory(b);
			if (inv != null) {
				for (int slot : getInputSlots()) {
					if (inv.getItemInSlot(slot) != null) {
						b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
						inv.replaceExistingItem(slot, null);
					}
				}
				
				for (int slot : getOutputSlots()) {
					if (inv.getItemInSlot(slot) != null) {
						b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
						inv.replaceExistingItem(slot, null);
					}
				}
			}
			
			progress.remove(b);
			processing.remove(b);
			return true;
		});
		
		this.registerDefaultRecipes();
	}

	public AContainer(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		this(category, item, id, recipeType, recipe);
		this.recipeOutput = recipeOutput;
	}

	public AContainer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		this(category, item, item.getItemID(), recipeType, recipe);
	}
	
	protected void constructMenu(BlockMenuPreset preset) {
		for (int i : BORDER) {
			preset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
		}
		
		for (int i : BORDER_IN) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());
		}
		
		for (int i : BORDER_OUT) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());
		}
		
		preset.addItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());
		
		for (int i : getOutputSlots()) {
			preset.addMenuClickHandler(i, new AdvancedMenuClickHandler() {
				
				@Override
				public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
					return false;
				}

				@Override
				public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
					return cursor == null || cursor.getType() == null || cursor.getType() == Material.AIR;
				}
			});
		}
	}
	
	public abstract String getInventoryTitle();
	public abstract ItemStack getProgressBar();
	public abstract int getEnergyConsumption();
	public abstract int getSpeed();
	public abstract String getMachineIdentifier();
	
	public void registerDefaultRecipes() {
		// Override this method to register your machine recipes
	}
	
	public List<ItemStack> getDisplayRecipes() {
		List<ItemStack> displayRecipes = new ArrayList<>(recipes.size() * 2);
		
		for (MachineRecipe recipe: recipes) {
			if (recipe.getInput().length != 1) continue;
			
			displayRecipes.add(recipe.getInput()[0]);
			displayRecipes.add(recipe.getOutput()[0]);
		}
		
		return displayRecipes;
	}
	
	@Override
	public int[] getInputSlots() {
		return new int[] {19, 20};
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {24, 25};
	}
	
	public MachineRecipe getProcessing(Block b) {
		return processing.get(b);
	}
	
	public boolean isProcessing(Block b) {
		return getProcessing(b) != null;
	}
	
	public void registerRecipe(MachineRecipe recipe) {
		recipe.setTicks(recipe.getTicks() / getSpeed());
		this.recipes.add(recipe);
	}
	
	public void registerRecipe(int seconds, ItemStack[] input, ItemStack[] output) {
		this.registerRecipe(new MachineRecipe(seconds, input, output));
	}
	
	@Override
	public void preRegister() {
		addItemHandler(new BlockTicker() {
			
			@Override
			public void tick(Block b, SlimefunItem sf, Config data) {
				AContainer.this.tick(b);
			}

			@Override
			public boolean isSynchronized() {
				return false;
			}
		});
	}

	protected void tick(Block b) {
		BlockMenu inv = BlockStorage.getInventory(b);
		
		if (isProcessing(b)) {
			int timeleft = progress.get(b);
			if (timeleft > 0) {
				MachineHelper.updateProgressbar(inv, 22, timeleft, processing.get(b).getTicks(), getProgressBar());
				
				if (ChargableBlock.isChargable(b)) {
					if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
					ChargableBlock.addCharge(b, -getEnergyConsumption());
					progress.put(b, timeleft - 1);
				}
				else progress.put(b, timeleft - 1);
			}
			else {
				inv.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
				
				for (ItemStack output : processing.get(b).getOutput()) {
					inv.pushItem(output.clone(), getOutputSlots());
				}
				
				progress.remove(b);
				processing.remove(b);
			}
		}
		else {
			MachineRecipe r = null;
			Map<Integer, Integer> found = new HashMap<>();
			
			for (MachineRecipe recipe : recipes) {
				for (ItemStack input : recipe.getInput()) {
					for (int slot : getInputSlots()) {
						if (SlimefunManager.isItemSimilar(inv.getItemInSlot(slot), input, true)) {
							found.put(slot, input.getAmount());
							break;
						}
					}
				}
				if (found.size() == recipe.getInput().length) {
					r = recipe;
					break;
				}
				else found.clear();
			}
			
			if (r != null) {
				if (!fits(b, r.getOutput())) return;
				
				for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
					inv.consumeItem(entry.getKey(), entry.getValue());
				}
				
				processing.put(b, r);
				progress.put(b, r.getTicks());
			}
		}
	}

}
