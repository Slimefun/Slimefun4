package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChargingBench extends AContainer {

	public ChargingBench(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
		
		new BlockMenuPreset(name, getInventoryTitle()) {
			
			@Override
			public void init() {
				constructMenu(this);
			}

			@Override
			public void newInstance(BlockMenu menu, Block b) {
			}

			@Override
			public boolean canOpen(Block b, Player p) {
				return p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true);
			}

			@Override
			public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
				if (flow.equals(ItemTransportFlow.INSERT)) return getInputSlots();
				else return getOutputSlots();
			}
		};
	}

	@Override
	public String getInventoryTitle() {
		return "&3Charging Bench";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.GOLDEN_PICKAXE);
	}
	
	@Override
	public int getEnergyConsumption() {
		return 10;
	}

	@Override
	public void registerDefaultRecipes() {}
	
	protected void tick(Block b) {
		if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
		
		for (int slot: getInputSlots()) {
			ItemStack stack = BlockStorage.getInventory(b).getItemInSlot(slot);
			if (ItemEnergy.getMaxEnergy(stack) > 0) {
				if (ItemEnergy.getStoredEnergy(stack) < ItemEnergy.getMaxEnergy(stack)) {

					ChargableBlock.addCharge(b, -getEnergyConsumption());
					float rest = ItemEnergy.addStoredEnergy(stack, getEnergyConsumption() / 2);
					if (rest > 0F) {
						if (fits(b, new ItemStack[] {stack})) {
							pushItems(b, new ItemStack[] {stack});
							BlockStorage.getInventory(b).replaceExistingItem(slot, null);
						}
						else {
							BlockStorage.getInventory(b).replaceExistingItem(slot, stack);
						}
					}
					else {
						BlockStorage.getInventory(b).replaceExistingItem(slot, stack);
					}
				}
				else if (fits(b, new ItemStack[] {stack})) {
					pushItems(b, new ItemStack[] {stack});
					BlockStorage.getInventory(b).replaceExistingItem(slot, null);
				}
				else {
					BlockStorage.getInventory(b).replaceExistingItem(slot, stack);
				}
				return;
			}
		}
	}

	@Override
	public int getSpeed() {
		return 1;
	}

	@Override
	public String getMachineIdentifier() {
		return "CHARGING_BENCH";
	}

}
