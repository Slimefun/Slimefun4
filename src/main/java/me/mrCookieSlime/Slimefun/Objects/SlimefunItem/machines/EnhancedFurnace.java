package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class EnhancedFurnace extends SimpleSlimefunItem<BlockTicker> {
	
	private final int speed;
	private final int efficiency;
	private final int fortune;
	
	public EnhancedFurnace(int speed, int efficiency, int fortune, SlimefunItemStack item, ItemStack[] recipe) {
		super(Categories.MACHINES_1, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
		
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
		int bonus = this.fortune;
		bonus = ThreadLocalRandom.current().nextInt(bonus + 2) - 1;
		if (bonus <= 0) bonus = 0;
		bonus++;
		return bonus;
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
