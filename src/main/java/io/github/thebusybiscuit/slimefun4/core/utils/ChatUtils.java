package io.github.thebusybiscuit.slimefun4.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;

public final class ChatUtils {
	
	private ChatUtils() {}
	
	public static void sendURL(Player p, String url) {
		p.sendMessage("");
		p.sendMessage(ChatColors.color("&eClick here:"));
		p.sendMessage(ChatColors.color("&7&o" + url));
		p.sendMessage("");
	}
	
	public static String christmas(String text) {
		return ChatColors.alternating(text, ChatColor.GREEN, ChatColor.RED);
	}

}
