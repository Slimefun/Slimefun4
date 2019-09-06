package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;

public class ChargingBench extends AContainer {

	public ChargingBench(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
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
	protected void tick(Block b) {
		if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
		
		for (int slot: getInputSlots()) {
			ItemStack stack = BlockStorage.getInventory(b).getItemInSlot(slot);
			if (ItemEnergy.getMaxEnergy(stack) > 0) {
				if (ItemEnergy.getStoredEnergy(stack) < ItemEnergy.getMaxEnergy(stack)) {

					ChargableBlock.addCharge(b, -getEnergyConsumption());
					float rest = ItemEnergy.addStoredEnergy(stack, getEnergyConsumption() / 2F);
					if (rest > 0F) {
						if (fits(b, stack)) {
							pushItems(b, stack);
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
				else if (fits(b, stack)) {
					pushItems(b, stack);
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
