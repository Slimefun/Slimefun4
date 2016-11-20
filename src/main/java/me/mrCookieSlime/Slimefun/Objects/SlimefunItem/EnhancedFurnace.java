package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;

import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;

public class EnhancedFurnace extends SlimefunItem {
	
	int speed, efficiency, fortune;
	
	public EnhancedFurnace(int speed, int efficiency, int fortune, ItemStack item, String name, ItemStack[] recipe) {
		super(Categories.MACHINES_1, item, name, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
		
		this.speed = speed - 1;
		this.efficiency = efficiency - 1;
		this.fortune = fortune - 1;
		
		addItemHandler(new BlockTicker() {
			
			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				try {
					if (!(b.getState() instanceof Furnace)) {
						return;
					}
					if (((Furnace) b.getState()).getCookTime() > 0) {
						((Furnace) b.getState()).setCookTime((short) (((Furnace) b.getState()).getCookTime() + getSpeed() * 10));
					}
					b.getState().update();
				} catch(NullPointerException x) {
				}
			}

			@Override
			public void uniqueTick() {
			}

			@Override
			public boolean isSynchronized() {
				return true;
			}
		});
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getFuelEfficiency() {
		return speed;
	}
	
	public int getOutput() {
		int fortune = this.fortune;
		fortune = SlimefunStartup.randomize(fortune + 2) - 1;
		if (fortune <= 0) fortune = 0;
		fortune++;
		return fortune;
	}
}
