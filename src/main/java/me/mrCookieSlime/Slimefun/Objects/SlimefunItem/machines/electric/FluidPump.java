package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.blocks.Vein;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class FluidPump extends SimpleSlimefunItem<BlockTicker> implements InventoryBlock {
	
	private static final int[] border = {0, 1, 2, 3, 4, 5, 6, 7, 8, 13, 31, 36, 37, 38, 39, 40, 41, 42, 43, 44, 22};
	private static final int[] border_in = {9, 10, 11, 12, 18, 21, 27, 28, 29, 30};
	private static final int[] border_out = {14, 15, 16, 17, 23, 26, 32, 33, 34, 35};

	protected int energyConsumption = 32;
	
	public FluidPump(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		
		createPreset(this, "&9Fluid Pump", this::constructMenu);
	}
	
	private void constructMenu(BlockMenuPreset preset) {
		for (int i : border) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());
		}
		
		for (int i : border_in) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());
		}
		for (int i : border_out) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());
		}
		
		for (int i : getOutputSlots()) {
			preset.addMenuClickHandler(i, new AdvancedMenuClickHandler() {
				
				@Override
				public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
					return false;
				}

				@Override
				public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
					return cursor == null || cursor.getType() == null || cursor.getType() == Material.AIR;
				}
			});
		}
	}

	@Override
	public int[] getInputSlots() {
		return new int[] {19, 20};
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {24, 25};
	}
	
	protected void tick(Block b) {
		Block fluid = b.getRelative(BlockFace.DOWN);
		ItemStack output = null;
		
		if (fluid.getType() == Material.LAVA) {
			output = new ItemStack(Material.LAVA_BUCKET);
		}
		else if (fluid.getType() == Material.WATER) {
			output = new ItemStack(Material.WATER_BUCKET);
		}
		
		if (output != null && ChargableBlock.getCharge(b) >= energyConsumption) {
			BlockMenu menu = BlockStorage.getInventory(b);
			
			for (int slot : getInputSlots()) {
				if (SlimefunManager.isItemSimilar(menu.getItemInSlot(slot), new ItemStack(Material.BUCKET), true)) {
					if (!menu.fits(output, getOutputSlots())) return;

					ChargableBlock.addCharge(b, -energyConsumption);
	                menu.consumeItem(slot);
					menu.pushItem(output, getOutputSlots());
					
					if (fluid.getType() == Material.WATER) {
						fluid.setType(Material.AIR);
					}
					else {
						List<Block> list = Vein.find(fluid, 50, block -> block.isLiquid() && block.getType() == fluid.getType());
			        	list.get(list.size() - 1).setType(Material.AIR);
					}
					
					return;
				}
			}
		}
	}

	@Override
	public BlockTicker getItemHandler() {
		return new BlockTicker() {
			
			@Override
			public void tick(Block b, SlimefunItem sf, Config data) {
				FluidPump.this.tick(b);
			}

			@Override
			public boolean isSynchronized() {
				return true;
			}
		};
	}

}
