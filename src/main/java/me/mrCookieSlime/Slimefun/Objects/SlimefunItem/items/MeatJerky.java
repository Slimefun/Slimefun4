package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemConsumptionHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class MeatJerky extends SimpleSlimefunItem<ItemConsumptionHandler> {

	private int saturation;
	
	public MeatJerky(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe, new String[] {"Saturation"}, new Object[] {18});
	}
	
	@Override
	public void postRegister() {
		saturation = (int) Slimefun.getItemValue(getID(), "Saturation");
	}

	@Override
	public ItemConsumptionHandler getItemHandler() {
		return (e, p, item) -> {
			if (isItem(item)) {
				p.setSaturation(saturation);
				return true;
			}
			return false;
		};
	}

}
