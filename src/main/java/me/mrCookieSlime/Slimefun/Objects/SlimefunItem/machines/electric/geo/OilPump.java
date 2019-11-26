package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.geo;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.GEO.OreGenSystem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

public abstract class OilPump extends AContainer {

	public OilPump(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		
		new BlockMenuPreset(getID(), getInventoryTitle()) {
			
			@Override
			public void init() {
				constructMenu(this);
			}

			@Override
			public boolean canOpen(Block b, Player p) {
				if (!(p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES))) {
					return false;
				}
				
				if (!OreGenSystem.wasResourceGenerated(OreGenSystem.getResource("Oil"), b.getChunk())) {
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
	public String getMachineIdentifier() {
		return "OIL_PUMP";
	}

	@Override
	public String getInventoryTitle() {
		return "&4Oil Pump";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.DIAMOND_SHOVEL);
	}

	@Override
	protected void tick(Block b) {
		BlockMenu inv = BlockStorage.getInventory(b);
		
		if (isProcessing(b)) {
			int timeleft = progress.get(b);
			if (timeleft > 0) {
				MachineHelper.updateProgressbar(inv, 22, timeleft, processing.get(b).getTicks(), getProgressBar());
				
				if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
				ChargableBlock.addCharge(b, -getEnergyConsumption());
				
				progress.put(b, timeleft - 1);
			}
			else {
				inv.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
				inv.pushItem(SlimefunItems.BUCKET_OF_OIL, getOutputSlots());
				
				progress.remove(b);
				processing.remove(b);
			}
		}
		else {
			OreGenResource oil = OreGenSystem.getResource("Oil");
			int supplies = OreGenSystem.getSupplies(oil, b.getChunk(), false);
			
			if (supplies > 0) {
				for (int slot: getInputSlots()) {
					if (SlimefunManager.isItemSimiliar(inv.getItemInSlot(slot), new ItemStack(Material.BUCKET), true)) {
						MachineRecipe r = new MachineRecipe(26, new ItemStack[0], new ItemStack[] {SlimefunItems.BUCKET_OF_OIL});
						
						if (!inv.fits(SlimefunItems.BUCKET_OF_OIL, getOutputSlots())) {
							return;
						}
						
						inv.replaceExistingItem(slot, InvUtils.decreaseItem(inv.getItemInSlot(slot), 1));
						processing.put(b, r);
						progress.put(b, r.getTicks());
						OreGenSystem.setSupplies(oil, b.getChunk(), supplies - 1);
						break;
					}
				}
			}
		}
	}

}
