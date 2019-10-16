package me.mrCookieSlime.Slimefun.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public final class MachineHelper {
	
	private MachineHelper() {}
	
	public static String getTimeLeft(int seconds) {
		String timeleft = "";
        final int minutes = (int) (seconds / 60L);
        if (minutes > 0) {
            timeleft = String.valueOf(timeleft) + minutes + "m ";
        }
        seconds -= minutes * 60;
        timeleft = String.valueOf(timeleft) + seconds + "s";
        return ChatColor.translateAlternateColorCodes('&', "&7" + timeleft + " left");
	}

	public static String getProgress(int time, int total) {
		StringBuilder progress = new StringBuilder();
		float percentage = Math.round(((((total - time) * 100.0F) / total) * 100.0F) / 100.0F);
		
		if (percentage < 16.0F) progress.append("&4");
		else if (percentage < 32.0F) progress.append("&c");
		else if (percentage < 48.0F) progress.append("&6");
		else if (percentage < 64.0F) progress.append("&e");
		else if (percentage < 80.0F) progress.append("&2");
		else progress = progress.append("&a");
		
		int rest = 20;
		for (int i = (int) percentage; i >= 5; i = i - 5) {
			progress.append(":");
			rest--;
		}
		
		progress.append("&7");
		
		for (int i = 0; i < rest; i++) {
			progress.append(":");
		}
		
		progress.append(" - " + percentage + "%");
		return ChatColor.translateAlternateColorCodes('&', progress.toString());
	}

	public static String getCoolant(int time, int total) {
		int passed = ((total - time) % 25);
		StringBuilder progress = new StringBuilder();
		float percentage = Math.round(((((25 - passed) * 100.0F) / 25) * 100.0F) / 100.0F);
		
		if (percentage < 33.0F) progress.append("&9");
		else if (percentage < 66.0F) progress.append("&1");
		else progress = progress.append("&b");
		
		int rest = 20;
		for (int i = (int) percentage; i >= 5; i = i - 5) {
			progress.append(":");
			rest--;
		}
		
		progress.append("&7");
		
		for (int i = 0; i < rest; i++) {
			progress.append(":");
		}
		
		progress.append(" - " + percentage + "%");
		return ChatColor.translateAlternateColorCodes('&', progress.toString());
	}

	public static float getPercentage(int time, int total) {
		int passed = ((total - time) % 25);
		return Math.round(((((25 - passed) * 100.0F) / 25) * 100.0F) / 100.0F);
	}

	public static short getDurability(ItemStack item, int timeleft, int max) {
		return (short) ((item.getType().getMaxDurability() / max) * timeleft);
	}
	
	public static void updateProgressbar(BlockMenu menu, int slot, int timeleft, int time, ItemStack indicator) {
		ItemStack item = indicator.clone();
		ItemMeta im = item.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		((Damageable) im).setDamage(getDurability(item, timeleft, time));
		im.setDisplayName(" ");
		List<String> lore = new ArrayList<>();
		lore.add(getProgress(timeleft, time));
		lore.add("");
		lore.add(getTimeLeft(timeleft / 2));
		im.setLore(lore);
		item.setItemMeta(im);
		
		menu.replaceExistingItem(slot, item);
	}

}
