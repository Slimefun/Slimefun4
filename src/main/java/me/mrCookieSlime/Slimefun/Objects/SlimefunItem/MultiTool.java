package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class MultiTool extends DamagableChargableItem {
	
	private static final String PREFIX = "mode.";
	
	private List<Integer> modes;

	public MultiTool(SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String... items) {
		super(Categories.TECH, item, recipeType, recipe, "Multi Tool", getKeys(items), getValues(items));
	}

	private static String[] getKeys(String... items) {
		String[] keys = new String[items.length * 2];
		
		for (int i = 0; i < items.length; i++) {
			keys[i * 2] = PREFIX + i + ".enabled";
			keys[i * 2 + 1] = PREFIX + i + ".item";
		}
		
		return keys;
	}
	
	private static Object[] getValues(String... items) {
		Object[] values = new Object[items.length * 2];
		
		for (int i = 0; i < items.length; i++) {
			values[i * 2] = true;
			values[i * 2 + 1] = items[i];
		}
		
		return values;
	}

	@Override
	public void postRegister() {
		List<Integer> list = new ArrayList<>();
		
		int i = 0;
		
		while (Slimefun.getItemValue(this.getID(), PREFIX + i + ".enabled") != null) {
			if ((boolean) Slimefun.getItemValue(this.getID(), PREFIX + i + ".enabled")) {
				list.add(i);
			}
			i++;
		}
		
		this.modes = list;
	}
	
	public List<Integer> getModes() {
		return this.modes;
	}

}
