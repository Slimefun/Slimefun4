package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class EnhancedFurnace extends SimpleSlimefunItem<BlockTicker> {
	
	private int speed;
	private int efficiency;
	private int fortune;
	
	public EnhancedFurnace(int speed, int efficiency, int fortune, ItemStack item, String id, ItemStack[] recipe) {
		super(Categories.MACHINES_1, item, id, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
		
		this.speed = speed - 1;
		this.efficiency = efficiency - 1;
		this.fortune = fortune - 1;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getFuelEfficiency() {
		return efficiency;
	}
	
	public int getOutput() {
		int fortune = this.fortune;
		fortune = new Random().nextInt(fortune + 2) - 1;
		if (fortune <= 0) fortune = 0;
		fortune++;
		return fortune;
	}

	@Override
	public BlockTicker getItemHandler() {
		return new BlockTicker() {
			
			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				if (!(b.getState() instanceof Furnace)) {
					// The Furnace has been destroyed, we can clear the block data
					BlockStorage.clearBlockInfo(b);
				}
				else if (((Furnace) b.getState()).getCookTime() > 0) {
					Furnace furnace = (Furnace) b.getState();

					int newCookTime = furnace.getCookTime() + getSpeed() * 10;

					if (newCookTime > 200) furnace.setCookTime((short) 188);
					else furnace.setCookTime((short) newCookTime);

					furnace.update(true, false);
				}
			}

			@Override
			public boolean isSynchronized() {
				return true;
			}
		};
	}
}
