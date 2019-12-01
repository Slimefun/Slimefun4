package me.mrCookieSlime.Slimefun.utils;

import org.bukkit.ChatColor;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;

public final class Christmas {

	private Christmas() {}
	
	public static String color(String text) {
		return ChatColor.RED + ChatColors.alternating(text, ChatColor.GREEN, ChatColor.RED);
	}
	
}
