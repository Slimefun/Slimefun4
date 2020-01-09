package me.mrCookieSlime.Slimefun.utils;

import org.bukkit.ChatColor;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.core.utils.ChatUtils;

/**
 * 
 * @deprecated This class will be deleted after X-Mas 2019. It will be replaced by {@link ChatUtils#christmas(String)}
 *
 */
@Deprecated
public final class Christmas {

	private Christmas() {}
	
	public static String color(String text) {
		return ChatColor.RED + ChatColors.alternating(text, ChatColor.GREEN, ChatColor.RED);
	}
	
}
