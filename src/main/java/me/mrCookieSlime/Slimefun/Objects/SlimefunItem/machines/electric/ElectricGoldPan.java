package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.collections.RandomizedSet;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

public abstract class ElectricGoldPan extends AContainer implements RecipeDisplayItem {

	private final RandomizedSet<ItemStack> randomizer = new RandomizedSet<>();
	private final RandomizedSet<ItemStack> randomizerNether = new RandomizedSet<>();
	
	private final List<ItemStack> displayRecipes = Arrays.asList(
			new ItemStack(Material.GRAVEL), new ItemStack(Material.FLINT), 
			new ItemStack(Material.GRAVEL), SlimefunItems.SIFTED_ORE, 
			new ItemStack(Material.GRAVEL), new ItemStack(Material.CLAY_BALL), 
			new ItemStack(Material.GRAVEL), new ItemStack(Material.IRON_NUGGET), 
			
			new ItemStack(Material.SOUL_SAND), new ItemStack(Material.QUARTZ), 
			new ItemStack(Material.SOUL_SAND), new ItemStack(Material.GOLD_NUGGET), 
			new ItemStack(Material.SOUL_SAND), new ItemStack(Material.NETHER_WART), 
			new ItemStack(Material.SOUL_SAND), new ItemStack(Material.BLAZE_POWDER), 
			new ItemStack(Material.SOUL_SAND), new ItemStack(Material.GLOWSTONE_DUST), 
			new ItemStack(Material.SOUL_SAND), new ItemStack(Material.GHAST_TEAR)
	);
	
	public ElectricGoldPan(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}
	
	@Override
	public void postRegister() {
		super.postRegister();
		
		String goldPan = "GOLD_PAN";
		String netherGoldPan = "NETHER_GOLD_PAN";
		
		add(false, SlimefunItems.SIFTED_ORE, (int) Slimefun.getItemValue(goldPan, "chance.SIFTED_ORE"));
		add(false, new ItemStack(Material.CLAY_BALL), (int) Slimefun.getItemValue(goldPan, "chance.CLAY"));
		add(false, new ItemStack(Material.FLINT), (int) Slimefun.getItemValue(goldPan, "chance.FLINT"));
		add(false, new ItemStack(Material.IRON_NUGGET), (int) Slimefun.getItemValue(goldPan, "chance.IRON_NUGGET"));
		
		add(true, new ItemStack(Material.QUARTZ), (int) Slimefun.getItemValue(netherGoldPan, "chance.QUARTZ"));
		add(true, new ItemStack(Material.GOLD_NUGGET), (int) Slimefun.getItemValue(netherGoldPan, "chance.GOLD_NUGGET"));
		add(true, new ItemStack(Material.NETHER_WART), (int) Slimefun.getItemValue(netherGoldPan, "chance.NETHER_WART"));
		add(true, new ItemStack(Material.BLAZE_POWDER), (int) Slimefun.getItemValue(netherGoldPan, "chance.BLAZE_POWDER"));
		add(true, new ItemStack(Material.GLOWSTONE_DUST), (int) Slimefun.getItemValue(netherGoldPan, "chance.GLOWSTONE_DUST"));
		add(true, new ItemStack(Material.GHAST_TEAR), (int) Slimefun.getItemValue(netherGoldPan, "chance.GHAST_TEAR"));
	}
	
	private void add(boolean nether, ItemStack item, int chance) {
		if (nether) {
			randomizerNether.add(item, chance);
		}
		else {
			randomizer.add(item, chance);
		}
	}

	@Override
	public String getInventoryTitle() {
		return "&6Electric Gold Pan";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.DIAMOND_SHOVEL);
	}
	
	public abstract int getSpeed();

	@Override
	protected void tick(Block b) {
		BlockMenu menu = BlockStorage.getInventory(b);
		if (isProcessing(b)) {
			int timeleft = progress.get(b);
			if (timeleft > 0 && getSpeed() < 10) {
				MachineHelper.updateProgressbar(menu, 22, timeleft, processing.get(b).getTicks(), getProgressBar());
				
				if (ChargableBlock.isChargable(b)) {
					if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
					ChargableBlock.addCharge(b, -getEnergyConsumption());
					progress.put(b, timeleft - 1);
				}
				else progress.put(b, timeleft - 1);
			}
			else if (ChargableBlock.isChargable(b)) {
				if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
				ChargableBlock.addCharge(b, -getEnergyConsumption());

				menu.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
				menu.pushItem(processing.get(b).getOutput()[0].clone(), getOutputSlots());
				
				progress.remove(b);
				processing.remove(b);
			}
		}
		else {
			for (int slot : getInputSlots()) {
				if (SlimefunManager.isItemSimilar(menu.getItemInSlot(slot), new ItemStack(Material.GRAVEL), true)) {
					ItemStack output = randomizer.getRandom();
					
					MachineRecipe r = new MachineRecipe(3 / getSpeed(), new ItemStack[0], new ItemStack[] {output});
					if (!menu.fits(output, getOutputSlots())) return;

	                menu.consumeItem(slot);
					processing.put(b, r);
					progress.put(b, r.getTicks());
					break;
				}
				else if (SlimefunManager.isItemSimilar(menu.getItemInSlot(slot), new ItemStack(Material.SOUL_SAND), true)) {
					ItemStack output = randomizerNether.getRandom();
					
					MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[] {output});
					if (!menu.fits(output, getOutputSlots())) return;

	                menu.consumeItem(slot);
					processing.put(b, r);
					progress.put(b, r.getTicks());
					break;
				}
			}
		}
	}
	
	@Override
	public String getMachineIdentifier() {
		return "ELECTRIC_GOLD_PAN";
	}
	
	@Override
	public List<ItemStack> getDisplayRecipes() {
		return displayRecipes;
	}

}
