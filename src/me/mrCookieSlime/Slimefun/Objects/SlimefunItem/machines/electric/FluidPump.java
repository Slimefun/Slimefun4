package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Block.Vein;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.utils.InventoryBlock;

public class FluidPump extends SlimefunItem implements InventoryBlock {
	
	public static Map<Block, MachineRecipe> processing = new HashMap<>();
	public static Map<Block, Integer> progress = new HashMap<>();
	
	private static final int[] border = {0, 1, 2, 3, 4, 5, 6, 7, 8, 13, 31, 36, 37, 38, 39, 40, 41, 42, 43, 44, 22};
	private static final int[] border_in = {9, 10, 11, 12, 18, 21, 27, 28, 29, 30};
	private static final int[] border_out = {14, 15, 16, 17, 23, 26, 32, 33, 34, 35};

	public FluidPump(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
		
		createPreset(getID(), "&9Fluid Pump", this::constructMenu);
	}
	
	private void constructMenu(BlockMenuPreset preset) {
		for (int i : border) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}
		
		for (int i : border_in) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}
		for (int i : border_out) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}
		
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
	public int[] getInputSlots() {
		return new int[] {19, 20};
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {24, 25};
	}
	
	protected void tick(Block b) {
		Block fluid = b.getRelative(BlockFace.DOWN);
		if (fluid.getType() == Material.LAVA) {
			for (int slot : getInputSlots()) {
				if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), new ItemStack(Material.BUCKET), true)) {
					if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
					
					ItemStack output = new ItemStack(Material.LAVA_BUCKET);
					
					if (!fits(b, output)) return;

					ChargableBlock.addCharge(b, -getEnergyConsumption());
					BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
					pushItems(b, output);
					
					List<Location> list = new ArrayList<>();
		        	list.add(fluid.getLocation());
		        	Vein.calculate(fluid.getLocation(), fluid.getLocation(), list, 64);
		        	list.get(list.size() - 1).getBlock().setType(Material.AIR);
					
					return;
				}
			}
		}
		else if (fluid.getType() == Material.WATER) {
			for (int slot : getInputSlots()) {
				if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), new ItemStack(Material.BUCKET), true)) {
					if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
					
					ItemStack output = new ItemStack(Material.WATER_BUCKET);
					
					if (!fits(b, output)) return;

					ChargableBlock.addCharge(b, -getEnergyConsumption());
					BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
					pushItems(b, output);
					
					fluid.setType(Material.AIR);
					
					return;
				}
			}
		}
	}
	
	private int getEnergyConsumption() {
		return 32;
	}

	@Override
	public void register(boolean slimefun) {
		addItemHandler(new BlockTicker() {
			
			@Override
			public void tick(Block b, SlimefunItem sf, Config data) {
				FluidPump.this.tick(b);
			}

			@Override
			public boolean isSynchronized() {
				return true;
			}
		});

		super.register(slimefun);
	}
	
	private Inventory inject(Block b) {
		int size = BlockStorage.getInventory(b).toInventory().getSize();
		Inventory inv = Bukkit.createInventory(null, size);
		for (int i = 0; i < size; i++) {
			inv.setItem(i, new CustomItem(Material.COMMAND_BLOCK, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US"));
		}
		for (int slot : getOutputSlots()) {
			inv.setItem(slot, BlockStorage.getInventory(b).getItemInSlot(slot));
		}
		return inv;
	}
	
	protected boolean fits(Block b, ItemStack... items) {
		return inject(b).addItem(items).isEmpty();
	}
	
	protected void pushItems(Block b, ItemStack... items) {
		Inventory inv = inject(b);
		inv.addItem(items);
		
		for (int slot: getOutputSlots()) {
			BlockStorage.getInventory(b).replaceExistingItem(slot, inv.getItem(slot));
		}
	}

}
