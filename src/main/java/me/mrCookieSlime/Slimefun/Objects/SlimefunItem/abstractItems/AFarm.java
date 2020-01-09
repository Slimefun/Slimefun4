package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.core.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public abstract class AFarm extends SlimefunItem {

	private static final int[] border = {0, 1, 2, 3, 4, 5, 6, 7, 8, 36, 37, 38, 39, 40, 41, 42, 43, 44};
	private static final int[] border_out = {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};

	public AFarm(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);

		new BlockMenuPreset(id, getInventoryTitle()) {

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
				if (flow == ItemTransportFlow.WITHDRAW) return getOutputSlots();
				return new int[0];
			}
		};

		registerBlockHandler(id, (p, b, tool, reason) -> {
			BlockMenu inv = BlockStorage.getInventory(b);
			if (inv != null) {
				for (int slot : getOutputSlots()) {
					if (inv.getItemInSlot(slot) != null) {
						b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
						inv.replaceExistingItem(slot, null);
					}
				}
			}
			return true;
		});
	}

	private void constructMenu(BlockMenuPreset preset) {
		for (int i : border) {
			preset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
		}
		
		for (int i : border_out) {
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
	public abstract int getEnergyConsumption();
	public abstract boolean canHarvest(Block b);
	public abstract ItemStack harvest(Block b);
	public abstract int getRadius();

	public int[] getOutputSlots() {
		return new int[] {19, 20, 21, 22, 23, 24, 25};
	}
	
	protected void tick(Block b) {
		if (ChargableBlock.isChargable(b)) {
			if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
			int radius = getRadius();
			
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					Block block = new Location(b.getWorld(), b.getX() + (double) x, b.getY() + 2.0, b.getZ() + (double) z).getBlock();
					if (canHarvest(block)) {
						BlockMenu menu = BlockStorage.getInventory(b.getLocation());
						ItemStack item = harvest(block);
						
						if (!menu.fits(item, getOutputSlots())) {
							return;
						}
						
						menu.pushItem(item, getOutputSlots());
						ChargableBlock.addCharge(b, -getEnergyConsumption());
						
						return;
					}
				}
			}
		}
	}

	@Override
	public void preRegister() {
		addItemHandler(new BlockTicker() {

			@Override
			public void tick(Block b, SlimefunItem sf, Config data) {
				AFarm.this.tick(b);
			}
			
			@Override
			public boolean isSynchronized() {
				return true;
			}
		});
	}
}
