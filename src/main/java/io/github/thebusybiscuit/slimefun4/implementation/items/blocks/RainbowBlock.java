package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.tasks.RainbowTicker;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class RainbowBlock extends SimpleSlimefunItem<RainbowTicker> {

	private final RainbowTicker ticker;
	
	public RainbowBlock(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, RainbowTicker ticker) {
		super(category, item, recipeType, recipe, recipeOutput);
		
		this.ticker = ticker;
	}

	@Override
	public RainbowTicker getItemHandler() {
		return ticker;
	}

}
