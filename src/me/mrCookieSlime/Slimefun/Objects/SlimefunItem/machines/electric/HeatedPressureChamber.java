package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.api.item_transport.RecipeSorter;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

public abstract class HeatedPressureChamber extends AContainer {

	public HeatedPressureChamber(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
		
		new BlockMenuPreset(name, getInventoryTitle()) {
			
			@Override
			public void init() {
				constructMenu(this);
			}

			@Override
			public boolean canOpen(Block b, Player p) {
				return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES);
			}
			
			@Override
			public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
				return new int[0];
			}

			@Override
			public int[] getSlotsAccessedByItemTransport(BlockMenu menu, ItemTransportFlow flow, ItemStack item) {
				if (flow == ItemTransportFlow.WITHDRAW) return getOutputSlots();
				
				List<Integer> slots = new ArrayList<>();
				
				for (int slot: getInputSlots()) {
					if (SlimefunManager.isItemSimiliar(menu.getItemInSlot(slot), item, true)) {
						slots.add(slot);
					}
				}
				
				if (slots.isEmpty()) {
					return getInputSlots();
				}
				else {
					Collections.sort(slots, new RecipeSorter(menu));
					
					int[] array = new int[slots.size()];
					
					for (int i = 0; i < slots.size(); i++) {
						array[i] = slots.get(i);
					}
					
					return array;
				}
			}
		};
		
		this.registerDefaultRecipes();
	}
	
	@Override
	public void registerDefaultRecipes() {
		registerRecipe(45, new ItemStack[] {SlimefunItems.BUCKET_OF_OIL}, new ItemStack[] {new CustomItem(SlimefunItems.PLASTIC_SHEET, 8)});
		registerRecipe(30, new ItemStack[] {SlimefunItems.GOLD_24K, SlimefunItems.URANIUM}, new ItemStack[] {SlimefunItems.BLISTERING_INGOT});
		registerRecipe(30, new ItemStack[] {SlimefunItems.BLISTERING_INGOT, SlimefunItems.CARBONADO}, new ItemStack[] {SlimefunItems.BLISTERING_INGOT_2});
		registerRecipe(60, new ItemStack[] {SlimefunItems.BLISTERING_INGOT_2, new ItemStack(Material.NETHER_STAR)}, new ItemStack[] {SlimefunItems.BLISTERING_INGOT_3});
		registerRecipe(90, new ItemStack[] {SlimefunItems.PLUTONIUM, SlimefunItems.URANIUM}, new ItemStack[] {SlimefunItems.BOOSTED_URANIUM});
		registerRecipe(60, new ItemStack[] {SlimefunItems.NETHER_ICE, SlimefunItems.PLUTONIUM}, new ItemStack[]{new CustomItem(SlimefunItems.ENRICHED_NETHER_ICE, 4)});
		registerRecipe(45, new ItemStack[] {SlimefunItems.ENRICHED_NETHER_ICE}, new ItemStack[]{new CustomItem(SlimefunItems.NETHER_ICE_COOLANT_CELL, 8)});
	}

	@Override
	public String getInventoryTitle() {
		return "&cHeated Pressure Chamber";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}

	@Override
	public int[] getInputSlots() {
		return new int[] {19, 20};
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {24, 25};
	}
	
	@Override
	public void preRegister() {
		addItemHandler(new BlockTicker() {
			
			@Override
			public void tick(Block b, SlimefunItem sf, Config data) {
				HeatedPressureChamber.this.tick(b);
			}

			@Override
			public boolean isSynchronized() {
				return false;
			}
		});
	}

	@Override
	protected void tick(Block b) {
		if (isProcessing(b)) {
			int timeleft = progress.get(b);
			
			if (timeleft > 0) {
				MachineHelper.updateProgressbar(BlockStorage.getInventory(b), 22, timeleft, processing.get(b).getTicks(), getProgressBar());
				
				if (ChargableBlock.isChargable(b)) {
					if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
					ChargableBlock.addCharge(b, -getEnergyConsumption());
					progress.put(b, timeleft - 1);
				}
				else progress.put(b, timeleft - 1);
			}
			else {
				BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
				pushItems(b, processing.get(b).getOutput());
				
				progress.remove(b);
				processing.remove(b);
			}
		}
		else {
			BlockMenu menu = BlockStorage.getInventory(b.getLocation());
			Map<Integer, Integer> found = new HashMap<>();
			MachineRecipe recipe = findRecipe(menu, found);
			
			if (recipe != null) {
				if (!fits(b, recipe.getOutput())) return;
				
				for (Map.Entry<Integer, Integer> entry: found.entrySet()) {
					menu.replaceExistingItem(entry.getKey(), InvUtils.decreaseItem(menu.getItemInSlot(entry.getKey()), entry.getValue()));
				}
				
				processing.put(b, recipe);
				progress.put(b, recipe.getTicks());
			}
		}
	}
	
	private MachineRecipe findRecipe(BlockMenu menu, Map<Integer, Integer> found) {
		for (MachineRecipe recipe: recipes) {
			for (ItemStack input: recipe.getInput()) {
				for (int slot: getInputSlots()) {
					if (SlimefunManager.isItemSimiliar(menu.getItemInSlot(slot), input, true)) {
						found.put(slot, input.getAmount());
						break;
					}
				}
			}
			if (found.size() == recipe.getInput().length) {
				return recipe;
			}
			else found.clear();
		}
		
		return null;
	}
	
	@Override
	public String getMachineIdentifier() {
		return "HEATED_PRESSURE_CHAMBER";
	}

}
