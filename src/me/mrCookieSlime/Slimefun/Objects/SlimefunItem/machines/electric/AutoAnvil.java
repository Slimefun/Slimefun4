package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

public abstract class AutoAnvil extends AContainer {

	public AutoAnvil(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}

	@Override
	public String getInventoryTitle() {
		return "Auto-Anvil";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.IRON_PICKAXE);
	}
	
	@Override
	public int getSpeed() {
		return 1;
	}
	
	@Override
	public String getMachineIdentifier() {
		return "AUTO_ANVIL";
	}
	
	public abstract int getRepairFactor();
	
	@Override
	protected void tick(Block b) {
		if (isProcessing(b)) {
			int timeleft = progress.get(b);
			if (timeleft > 0) {
				MachineHelper.updateProgressbar(BlockStorage.getInventory(b), 22, timeleft, processing.get(b).getTicks(), getProgressBar());
				
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
			BlockMenu menu = BlockStorage.getInventory(b);
			MachineRecipe recipe = null;
			
			for (int slot: getInputSlots()) {
				ItemStack target = menu.getItemInSlot(slot == getInputSlots()[0] ? getInputSlots()[1]: getInputSlots()[0]);
				ItemStack item = menu.getItemInSlot(slot);
				
				if (item != null && item.getType().getMaxDurability() > 0 && ((Damageable) item.getItemMeta()).getDamage() > 0) {
					if (SlimefunManager.isItemSimiliar(target, SlimefunItems.DUCT_TAPE, true)) {
						ItemStack newItem = item.clone();
						short durability = (short) (((Damageable) newItem.getItemMeta()).getDamage() - (item.getType().getMaxDurability() / getRepairFactor()));
						if (durability < 0) durability = 0;
						ItemMeta meta = newItem.getItemMeta();
						((Damageable) meta).setDamage(durability);
						newItem.setItemMeta(meta);
						recipe = new MachineRecipe(30, new ItemStack[] {target, item}, new ItemStack[] {newItem});
					}
					break;
				}
			}
			
			if (recipe != null) {
				if (!fits(b, recipe.getOutput())) return;
				
				for (int slot: getInputSlots()) {
					menu.replaceExistingItem(slot, InvUtils.decreaseItem(menu.getItemInSlot(slot), 1));
				}
				processing.put(b, recipe);
				progress.put(b, recipe.getTicks());
			}
		}
	}

}
