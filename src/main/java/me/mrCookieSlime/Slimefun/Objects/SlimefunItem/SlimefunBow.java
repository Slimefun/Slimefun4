package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.handlers.BowShootHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public abstract class SlimefunBow extends SlimefunItem {

	public SlimefunBow(SlimefunItemStack item, ItemStack[] recipe) {
		super(Categories.WEAPONS, item, RecipeType.MAGIC_WORKBENCH, recipe);
	}
	
	@Override
	public void preRegister() {
		super.preRegister();
		addItemHandler(onShoot());
	}
	
	public abstract BowShootHandler onShoot();

}
