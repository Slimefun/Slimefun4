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
	
	public EnhancedFurnace(int speed, int efficiency, int fortune, ItemStack item, String id, ItemStack[] recipe) {
		super(Categories.MACHINES_1, item, id, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
		
		this.speed = speed - 1;
		this.efficiency = efficiency - 1;
		this.fortune = fortune - 1;
		
		addItemHandler(new BlockTicker() {
			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				if (b.getState() instanceof Furnace)
					if (((Furnace) b.getState()).getCookTime() > 0) {
						Furnace furnace = (Furnace) b.getState();
						furnace.setCookTime((short) (furnace.getCookTime() + getSpeed() * 10));
						furnace.update(true, false);
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
