package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.api.item_transport.RecipeSorter;

public abstract class ElectricSmeltery extends AContainer {
	
	private static final int[] border = {4, 5, 6, 7, 8, 13, 31, 40, 41, 42, 43, 44};
	private static final int[] border_in = {0, 1, 2, 3, 9, 12, 18, 21, 27, 30, 36, 37, 38, 39};
	private static final int[] border_out = {14, 15, 16, 17, 23, 26, 32, 33, 34, 35};

	public ElectricSmeltery(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
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
				
				for (int slot : getInputSlots()) {
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
		
		registerBlockHandler(name, (p, b, tool, reason) -> {
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

	@Override
	protected void constructMenu(BlockMenuPreset preset) {
		for (int i : border) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "),
				(p, slot, item, action) -> false
			);
		}
		for (int i : border_in) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "),
				(p, slot, item, action) -> false
			);
		}
		for (int i : border_out) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "),
				(p, slot, item, action) -> false
			);
		}
		
		preset.addItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "),
			(p, slot, item, action) -> false
		);
		
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
	public String getInventoryTitle() {
		return "&cElectric Smeltery";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}

	@Override
	public int[] getInputSlots() {
		return new int[] {10, 11, 19, 20, 28, 29};
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {24, 25};
	}
	
	@Override
	public String getMachineIdentifier() {
		return "ELECTRIC_SMELTERY";
	}

}
