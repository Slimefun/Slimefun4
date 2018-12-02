package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Refinery extends AContainer {

	public Refinery(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}

	@Override
	public String getInventoryTitle() {
		return "&cRefinery";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}

	@Override
	public void registerDefaultRecipes() {}
	
	@Override
	public String getMachineIdentifier() {
		return "REFINERY";
	}
	
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
			for (int slot: getInputSlots()) {
				if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), SlimefunItems.BUCKET_OF_OIL, true)) {
					MachineRecipe r = new MachineRecipe(40, new ItemStack[0], new ItemStack[] {SlimefunItems.BUCKET_OF_FUEL});
					if (!fits(b, r.getOutput())) return;
					BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
					processing.put(b, r);
					progress.put(b, r.getTicks());
					break;
				}
			}
		}
	}

}
