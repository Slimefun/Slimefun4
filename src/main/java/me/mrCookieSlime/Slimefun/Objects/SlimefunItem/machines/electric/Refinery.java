package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

public abstract class Refinery extends AContainer implements RecipeDisplayItem {

	public Refinery(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
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
	public String getMachineIdentifier() {
		return "REFINERY";
	}
	
	@Override
	public List<ItemStack> getDisplayRecipes() {
		return Arrays.asList(SlimefunItems.BUCKET_OF_OIL, SlimefunItems.BUCKET_OF_FUEL);
	}

	@Override
	protected void tick(Block b) {
		BlockMenu menu = BlockStorage.getInventory(b);
		
		if (isProcessing(b)) {
			int timeleft = progress.get(b);
			
			if (timeleft > 0) {
				MachineHelper.updateProgressbar(menu, 22, timeleft, processing.get(b).getTicks(), getProgressBar());
				
				if (ChargableBlock.isChargable(b)) {
					if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
					
					ChargableBlock.addCharge(b, -getEnergyConsumption());
					progress.put(b, timeleft - 1);
				}
				else progress.put(b, timeleft - 1);
			}
			else {
				menu.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
				menu.pushItem(processing.get(b).getOutput()[0], getOutputSlots());
				
				progress.remove(b);
				processing.remove(b);
			}
		}
		else {
			for (int slot : getInputSlots()) {
				if (SlimefunManager.isItemSimilar(menu.getItemInSlot(slot), SlimefunItems.BUCKET_OF_OIL, true)) {
					MachineRecipe r = new MachineRecipe(40, new ItemStack[0], new ItemStack[] {SlimefunItems.BUCKET_OF_FUEL});
					
					if (!menu.fits(SlimefunItems.BUCKET_OF_FUEL, getOutputSlots())) return;
					
					ItemUtils.consumeItem(menu.getItemInSlot(slot), false);
					processing.put(b, r);
					progress.put(b, r.getTicks());
					break;
				}
			}
		}
	}

}
