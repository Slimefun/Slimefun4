package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class TrashCan extends SlimefunItem implements InventoryBlock {
	
	private static final int[] BORDER = {0, 1, 2, 3, 5, 4, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
	private static final ItemStack BACKGROUND = new CustomItem(new ItemStack(Material.RED_STAINED_GLASS_PANE), " ");
	
	public TrashCan(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		createPreset(this, "&4Trash Can", this::constructMenu);
	}
	
	private void constructMenu(BlockMenuPreset preset) {
		for (int i : BORDER) {
			preset.addItem(i, BACKGROUND, ChestMenuUtils.getEmptyClickHandler());
		}
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
			public void tick(Block b, SlimefunItem item, Config data) {
				BlockMenu menu = BlockStorage.getInventory(b);
				
				for (int slot : getInputSlots()) {
					menu.replaceExistingItem(slot, null);
				}
			}
			
			@Override
			public boolean isSynchronized() {
				return false;
			}
		});
	}

}
