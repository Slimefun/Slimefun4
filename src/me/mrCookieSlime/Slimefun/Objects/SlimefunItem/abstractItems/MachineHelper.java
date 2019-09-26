package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

@Deprecated
public final class MachineHelper {
	
	private MachineHelper() {}
	
	public static String getTimeLeft(int seconds) {
		return me.mrCookieSlime.Slimefun.utils.MachineHelper.getTimeLeft(seconds);
	}

	public static String getProgress(int time, int total) {
		return me.mrCookieSlime.Slimefun.utils.MachineHelper.getProgress(time, total);
	}

	public static String getCoolant(int time, int total) {
		return me.mrCookieSlime.Slimefun.utils.MachineHelper.getCoolant(time, total);
	}

	public static float getPercentage(int time, int total) {
		return me.mrCookieSlime.Slimefun.utils.MachineHelper.getPercentage(time, total);
	}

	public static short getDurability(ItemStack item, int timeleft, int max) {
		return me.mrCookieSlime.Slimefun.utils.MachineHelper.getDurability(item, timeleft, max);
	}
	
	public static void updateProgressbar(BlockMenu menu, int slot, int timeleft, int time, ItemStack indicator) {
		me.mrCookieSlime.Slimefun.utils.MachineHelper.updateProgressbar(menu, slot, timeleft, time, indicator);
	}

}
