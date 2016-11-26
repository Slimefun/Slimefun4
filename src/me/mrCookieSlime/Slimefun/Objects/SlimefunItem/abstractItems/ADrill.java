package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.GEO.OreGenSystem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public abstract class ADrill extends AContainer {
	
	private static final int[] border = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 13, 31, 36, 37, 38, 39, 40, 41, 42, 43, 44, 9, 10, 11, 12, 18, 21, 27, 28, 29, 30, 19, 20 };
	private static final int[] border_out = { 14, 15, 16, 17, 23, 26, 32, 33, 34, 35 };
	
	public abstract OreGenResource getOreGenResource();
	public abstract ItemStack[] getOutputItems();
	public abstract int getProcessingTime();
	public abstract int getSpeed();

	public ADrill(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
		
		new BlockMenuPreset(name, getInventoryTitle()) {
			
			@Override
			public void init() {
				this.constructMenu(this);
			}

			@SuppressWarnings("deprecation")
			private void constructMenu(BlockMenuPreset preset) {
				for (int i: border) {
					preset.addItem(i, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 7), " "),
					new MenuClickHandler() {

						@Override
						public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
							return false;
						}
								
					});
				}
				for (int i: border_out) {
					preset.addItem(i, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 1), " "),
					new MenuClickHandler() {

						@Override
						public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
							return false;
						}
								
					});
				}
				
				preset.addItem(22, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 15), " "),
				new MenuClickHandler() {

					@Override
					public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
						return false;
					}
									
				});
				
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
			public void newInstance(BlockMenu menu, Block b) {
			}

			@Override
			public boolean canOpen(Block b, Player p) {
				if (!(p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true))) {
					return false;
				}
				
				if (!OreGenSystem.wasResourceGenerated(getOreGenResource(), b.getChunk())) {
					Messages.local.sendTranslation(p, "gps.geo.scan-required", true);
					return false;
				}
				return true;
			}

			@Override
			public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
				if (flow.equals(ItemTransportFlow.INSERT)) return getInputSlots();
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
	
	@SuppressWarnings("deprecation")
	protected void tick(Block b) {
		if (isProcessing(b)) {
			int timeleft = progress.get(b);
			if (timeleft > 0) {
				ItemStack item = getProgressBar().clone();
		        item.setDurability(MachineHelper.getDurability(item, timeleft, processing.get(b).getTicks()));
				ItemMeta im = item.getItemMeta();
				im.setDisplayName(" ");
				List<String> lore = new ArrayList<String>();
				lore.add(MachineHelper.getProgress(timeleft, processing.get(b).getTicks()));
				lore.add("");
				lore.add(MachineHelper.getTimeLeft(timeleft / 2));
				im.setLore(lore);
				item.setItemMeta(im);
				
				BlockStorage.getInventory(b).replaceExistingItem(22, item);
				
				if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
				ChargableBlock.addCharge(b, -getEnergyConsumption());
				
				progress.put(b, timeleft - 1);
			}
			else {
				BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 15), " "));
				pushItems(b, processing.get(b).getOutput());
				
				progress.remove(b);
				processing.remove(b);
			}
		}
		else if (OreGenSystem.getSupplies(getOreGenResource(), b.getChunk(), false) > 0) {
			MachineRecipe r = new MachineRecipe(getProcessingTime() / getSpeed(), new ItemStack[0], this.getOutputItems());
			if (!fits(b, r.getOutput())) return;
			processing.put(b, r);
			progress.put(b, r.getTicks());
			OreGenSystem.setSupplies(getOreGenResource(), b.getChunk(), OreGenSystem.getSupplies(getOreGenResource(), b.getChunk(), false) - 1);
		}
	}

}
