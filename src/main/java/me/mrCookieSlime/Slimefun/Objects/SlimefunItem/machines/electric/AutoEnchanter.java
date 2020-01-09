package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.EmeraldEnchants.EmeraldEnchants;
import me.mrCookieSlime.EmeraldEnchants.ItemEnchantment;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

public class AutoEnchanter extends AContainer {

	public AutoEnchanter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}

	@Override
	public String getInventoryTitle() {
		return "&5Auto-Enchanter";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.GOLDEN_CHESTPLATE);
	}

	@Override
	public int getEnergyConsumption() {
		return 9;
	}

	@Override
	protected void tick(Block b) {
		BlockMenu menu = BlockStorage.getInventory(b.getLocation());
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
				pushItems(b, processing.get(b).getOutput());

				progress.remove(b);
				processing.remove(b);
			}
		}
		else {
			MachineRecipe recipe = null;
			
			for (int slot : getInputSlots()) {
				ItemStack target = menu.getItemInSlot(slot == getInputSlots()[0] ? getInputSlots()[1]: getInputSlots()[0]);
				// Check if enchantable
				SlimefunItem sfTarget = SlimefunItem.getByItem(target);
				if(sfTarget != null && !sfTarget.isEnchantable()) return;
				
				ItemStack item = menu.getItemInSlot(slot);
				
				// Enchant
				if (item != null && item.getType() == Material.ENCHANTED_BOOK && target != null) {
					Map<Enchantment, Integer> enchantments = new HashMap<>();
					Set<ItemEnchantment> emeraldEnchantments = new HashSet<>();
					int amount = 0;
					int specialAmount = 0;
					EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
					
					for (Map.Entry<Enchantment, Integer> e: meta.getStoredEnchants().entrySet()) {
						if (e.getKey().canEnchantItem(target)) {
							amount++;
							enchantments.put(e.getKey(), e.getValue());
						}
					}
					
					if (SlimefunPlugin.getHooks().isEmeraldEnchantsInstalled()) {
						for (ItemEnchantment enchantment : EmeraldEnchants.getInstance().getRegistry().getEnchantments(item)) {
							if (EmeraldEnchants.getInstance().getRegistry().isApplicable(target, enchantment.getEnchantment()) && EmeraldEnchants.getInstance().getRegistry().getEnchantmentLevel(target, enchantment.getEnchantment().getName()) < enchantment.getLevel()) {
								amount++;
								specialAmount++;
								emeraldEnchantments.add(enchantment);
							}
						}
						specialAmount += EmeraldEnchants.getInstance().getRegistry().getEnchantments(target).size();
					}
					
					if (amount > 0 && specialAmount <= SlimefunPlugin.getSettings().emeraldEnchantsLimit) {
						ItemStack newItem = target.clone();
						
						for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
							newItem.addUnsafeEnchantment(entry.getKey(), entry.getValue());
						}
						
						for (ItemEnchantment ench: emeraldEnchantments) {
							EmeraldEnchants.getInstance().getRegistry().applyEnchantment(newItem, ench.getEnchantment(), ench.getLevel());
						}
						
						recipe = new MachineRecipe(75 * amount, new ItemStack[] {target, item}, new ItemStack[] {newItem, new ItemStack(Material.BOOK)});
					}
					break;
				}
			}

			if (recipe != null) {
				if (!fits(b, recipe.getOutput())) return;
				
				for (int slot : getInputSlots()) {
	                menu.consumeItem(slot);
				}
				
				processing.put(b, recipe);
				progress.put(b, recipe.getTicks());
			}
		}
	}

	@Override
	public int getSpeed() {
		return 1;
	}

	@Override
	public String getMachineIdentifier() {
		return "AUTO_ENCHANTER";
	}

}
