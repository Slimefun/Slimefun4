package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.core.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.GEO.OreGenSystem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

@Deprecated
public abstract class ADrill extends AContainer {
	
	private static final int[] border = {0, 1, 2, 3, 4, 5, 6, 7, 8, 13, 31, 36, 37, 38, 39, 40, 41, 42, 43, 44, 9, 10, 11, 12, 18, 21, 27, 28, 29, 30, 19, 20};
	private static final int[] border_out = {14, 15, 16, 17, 23, 26, 32, 33, 34, 35};
	
	public abstract OreGenResource getOreGenResource();
	public abstract ItemStack[] getOutputItems();
	public abstract int getProcessingTime();
	public abstract int getSpeed();

	public ADrill(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
		
		new BlockMenuPreset(id, getInventoryTitle()) {
			
			@Override
			public void init() {
				this.constructMenu(this);
			}

			private void constructMenu(BlockMenuPreset preset) {
				for (int i : border) {
					preset.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "),
						ChestMenuUtils.getEmptyClickHandler()
					);
				}
				for (int i : border_out) {
					preset.addItem(i, new CustomItem(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "),
						ChestMenuUtils.getEmptyClickHandler()
					);
				}
				
				preset.addItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "),
					ChestMenuUtils.getEmptyClickHandler()
				);
				
				for (int i: getOutputSlots()) {
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
			public boolean canOpen(Block b, Player p) {
				if (!(p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES))) {
					return false;
				}
				
				if (!OreGenSystem.wasResourceGenerated(getOreGenResource(), b.getChunk())) {
					SlimefunPlugin.getLocal().sendMessage(p, "gps.geo.scan-required", true);
					return false;
				}
				return true;
			}

			@Override
			public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
				if (flow == ItemTransportFlow.INSERT) return getInputSlots();
				else return getOutputSlots();
			}
		};
	}
	
	@Override
	public int[] getInputSlots() { 
		return new int[0]; 
	}

	@Override
	public void registerDefaultRecipes() {}

	@Override
	protected void tick(Block b) {
		BlockMenu menu = BlockStorage.getInventory(b);
		if (isProcessing(b)) {
			int timeleft = progress.get(b);
			if (timeleft > 0) {
				MachineHelper.updateProgressbar(menu, 22, timeleft, processing.get(b).getTicks(), getProgressBar());
				
				if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
				ChargableBlock.addCharge(b, -getEnergyConsumption());
				
				progress.put(b, timeleft - 1);
			}
			else {
				menu.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
				menu.pushItem(processing.get(b).getOutput()[0], getOutputSlots());
				
				progress.remove(b);
				processing.remove(b);
			}
		}
		else if (OreGenSystem.getSupplies(getOreGenResource(), b.getChunk(), false) > 0) {
			MachineRecipe r = new MachineRecipe(getProcessingTime() / getSpeed(), new ItemStack[0], this.getOutputItems());
			if (!menu.fits(r.getOutput()[0], getOutputSlots())) return;
			processing.put(b, r);
			progress.put(b, r.getTicks());
			OreGenSystem.setSupplies(getOreGenResource(), b.getChunk(), OreGenSystem.getSupplies(getOreGenResource(), b.getChunk(), false) - 1);
		}
	}

}
