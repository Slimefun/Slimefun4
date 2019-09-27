package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.geo;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.GEO.OreGenSystem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.holograms.SimpleHologram;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

public abstract class GEOMiner extends AContainer implements InventoryBlock, RecipeDisplayItem {
	
	private static final int[] BORDER = {0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 44, 45, 53};
	private static final int[] BORDER_OUT = {19, 20, 21, 22, 23, 24, 25, 28, 34, 37, 43, 46, 47, 48, 49, 50, 51, 52};
	private static final int[] OUTPUT_SLOTS = {29, 30, 31, 32, 33, 38, 39, 40, 41, 42};
	
	public GEOMiner(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
		
		registerBlockHandler(getID(), new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				// Spawn the hologram
				SimpleHologram.update(b, "&7Idling...");
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				SimpleHologram.remove(b);
				
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
			}
		});
	}
	
	@Override
	public String getInventoryTitle() {
		return "&6GEO-Miner";
	}
	
	@Override
	public String getMachineIdentifier() {
		return "GEO_MINER";
	}
	
	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.DIAMOND_PICKAXE);
	}
	
	@Override
	public int[] getInputSlots() {
		return new int[0];
	}
	
	@Override
	public int[] getOutputSlots() {
		return OUTPUT_SLOTS;
	}
	
	public int getProcessingTime() {
		return 18;
	}
	
	@Override
	public List<ItemStack> getDisplayRecipes() {
		List<ItemStack> displayRecipes = new LinkedList<>();
		for (OreGenResource resource: OreGenSystem.listResources()) {
			if (!resource.isLiquid()) {
				displayRecipes.add(new CustomItem(resource.getItem(), "&r" + resource.getName()));
			}
		}
		
		if (displayRecipes.size() % 2 != 0) displayRecipes.add(null);
		return displayRecipes;
	}
	
	@Override
	protected void constructMenu(BlockMenuPreset preset) {
		for (int i : BORDER) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}
		
		for (int i : BORDER_OUT) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}
		
		preset.addItem(4, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		
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

	@Override
	protected void tick(Block b) {
		if (isProcessing(b)) {
			int timeleft = progress.get(b);
			if (timeleft > 0) {
				MachineHelper.updateProgressbar(BlockStorage.getInventory(b), 4, timeleft, processing.get(b).getTicks(), getProgressBar());
				
				if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
				ChargableBlock.addCharge(b, -getEnergyConsumption());
				
				progress.put(b, timeleft - 1);
			}
			else {
				BlockStorage.getInventory(b).replaceExistingItem(4, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
				pushItems(b, processing.get(b).getOutput());
				
				progress.remove(b);
				processing.remove(b);
			}
		}
		else if (!BlockStorage.hasChunkInfo(b.getChunk())) {
			SimpleHologram.update(b, "&4GEO-Scan required!");
		}
		else {
			Chunk chunk = b.getChunk();
			
			for (OreGenResource resource: OreGenSystem.listResources()) {
				if (!resource.isLiquid()) {
					if (!OreGenSystem.wasResourceGenerated(resource, chunk)) {
						SimpleHologram.update(b, "&4GEO-Scan required!");
						return;
					}
					else {
						int supplies = OreGenSystem.getSupplies(resource, chunk, false);
						if (supplies > 0) {
							MachineRecipe r = new MachineRecipe(getProcessingTime() / getSpeed(), new ItemStack[0], new ItemStack[] {resource.getItem().clone()});
							if (!fits(b, r.getOutput())) return;
							
							processing.put(b, r);
							progress.put(b, r.getTicks());
							OreGenSystem.setSupplies(resource, b.getChunk(), supplies - 1);
							SimpleHologram.update(b, "&7Mining: &r" + resource.getName());
							return;
						}
					}
						
				}
			}
			
			SimpleHologram.update(b, "&7Finished");
		}
	}
	
}
