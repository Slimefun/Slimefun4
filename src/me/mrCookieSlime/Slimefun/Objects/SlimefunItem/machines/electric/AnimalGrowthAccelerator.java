package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.holograms.AnimalGrowthAcceleratorHologram;

public class AnimalGrowthAccelerator extends SlimefunItem implements InventoryBlock {
	
	private static final int[] border = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};

	public AnimalGrowthAccelerator(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
		createPreset(this, "&bGrowth Accelerator", this::constructMenu);
		
		registerBlockHandler(name, (p, b, tool, reason) -> {
			AnimalGrowthAcceleratorHologram.remove(b);
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
	
	private void constructMenu(BlockMenuPreset preset) {
		for (int i : border) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
		}
	}
	
	public int getEnergyConsumption() {
		return 14;
	}
	
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
				try {
					AnimalGrowthAccelerator.this.tick(b);
				} catch (Exception x) {
					Slimefun.getLogger().log(Level.SEVERE, "An Error occured while ticking an Animal Growth Accelerator for Slimefun " + Slimefun.getVersion(), x);
				}
			}

			@Override
			public boolean isSynchronized() {
				return true;
			}
		});
	}
	
	protected void tick(Block b) {
		for (Entity n : AnimalGrowthAcceleratorHologram.getArmorStand(b, true).getNearbyEntities(3D, 3D, 3D)) {
			if (n instanceof Ageable && !((Ageable) n).isAdult()) {
				for (int slot: getInputSlots()) {
					if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), SlimefunItems.ORGANIC_FOOD, false)) {
						if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
						ChargableBlock.addCharge(b, -getEnergyConsumption());
						BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
						((Ageable) n).setAge(((Ageable) n).getAge() + 2000);
						if (((Ageable) n).getAge() > 0) ((Ageable) n).setAge(0);
						n.getWorld().spawnParticle(Particle.VILLAGER_HAPPY,((LivingEntity) n).getEyeLocation(), 8, 0.2F, 0.2F, 0.2F);
						return;
					}
				}
			}
		}
	}

}
