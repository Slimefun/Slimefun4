package me.mrCookieSlime.Slimefun.Objects;

import java.util.Calendar;

import org.bukkit.inventory.ItemStack;

public class SeasonCategory extends Category {
	
	int month = -1;

	public SeasonCategory(int month, int tier, ItemStack item) {
		super(item, tier);
		this.month = month - 1;
	}
	
	public int getMonth() {
		return this.month;
	}
	
	public boolean isUnlocked() {
		if (month == -1) return true;
		Calendar calendar = Calendar.getInstance();
		return month == calendar.get(Calendar.MONTH);
	}
}
