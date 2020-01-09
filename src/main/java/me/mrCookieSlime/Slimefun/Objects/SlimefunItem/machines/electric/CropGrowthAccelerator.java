package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public abstract class CropGrowthAccelerator extends SlimefunItem implements InventoryBlock {
	
	private static final int[] border = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
	private static final Map<Material, Integer> crops = new EnumMap<>(Material.class);
	
	static {
		crops.put(Material.WHEAT, 7);
		crops.put(Material.POTATOES, 7);
		crops.put(Material.CARROTS, 7);
		crops.put(Material.NETHER_WART, 3);
		crops.put(Material.BEETROOTS, 3);
		crops.put(Material.COCOA, 8);
		crops.put(Material.SWEET_BERRY_BUSH, 3);
	}

	public CropGrowthAccelerator(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		createPreset(this, "&bGrowth Accelerator", this::constructMenu);
		
		registerBlockHandler(getID(), (p, b, tool, reason) -> {
			BlockMenu inv = BlockStorage.getInventory(b);
			if (inv != null) {
				for (int slot : getInputSlots()) {
					if (inv.getItemInSlot(slot) != null) {
						b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
						inv.replaceExistingItem(slot, null);
					}
				}
			}
			return true;
		});
	}
	
	protected void constructMenu(BlockMenuPreset preset) {
		for (int i : border) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());
		}
	}
	
	public abstract int getEnergyConsumption();
	public abstract int getRadius();
	public abstract int getSpeed();
	
	@Override
	public int[] getInputSlots() {
		return new int[] {10, 11, 12, 13, 14, 15, 16};
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
	
	@Override
	public void preRegister() {
		addItemHandler(new BlockTicker() {
			
			@Override
			public void tick(Block b, SlimefunItem sf, Config data) {
				CropGrowthAccelerator.this.tick(b);
			}

			@Override
			public boolean isSynchronized() {
				return true;
			}
		});
	}
	
	protected void tick(Block b) {
		BlockMenu inv = BlockStorage.getInventory(b);
		
		if (work(b, inv) > 0) {
			for (int slot : getInputSlots()) {
				if (SlimefunManager.isItemSimilar(inv.getItemInSlot(slot), SlimefunItems.FERTILIZER, false)) {
					inv.consumeItem(slot);
					break;
				}
			}
		}
	}

	private int work(Block b, BlockMenu inv) {
		int work = 0;
		
		for (int x = -getRadius(); x <= getRadius(); x++) {
			for (int z = -getRadius(); z <= getRadius(); z++) {
				Block block = b.getRelative(x, 0, z);
				
				if (crops.containsKey(block.getType()) && ((Ageable) block.getBlockData()).getAge() < crops.get(block.getType())) {
					for (int slot : getInputSlots()) {
						if (SlimefunManager.isItemSimilar(inv.getItemInSlot(slot), SlimefunItems.FERTILIZER, false)) {
							if (work > (getSpeed() - 1) || ChargableBlock.getCharge(b) < getEnergyConsumption()) return work;
							ChargableBlock.addCharge(b, -getEnergyConsumption());

							Ageable ageable = (Ageable) block.getBlockData();
							ageable.setAge(ageable.getAge() + 1);
							block.setBlockData(ageable);

							block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, block.getLocation().add(0.5D, 0.5D, 0.5D), 4, 0.1F, 0.1F, 0.1F);
							work++;
							return work;
						}
					}
				}
			}
		}
		
		return work;
	}

}
