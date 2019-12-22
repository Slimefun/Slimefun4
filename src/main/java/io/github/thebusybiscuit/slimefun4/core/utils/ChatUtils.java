package io.github.thebusybiscuit.slimefun4.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;

public final class ChatUtils {
	
	private ChatUtils() {}
	
	public static void sendURL(CommandSender sender, String url) {
		sender.sendMessage("");
		sender.sendMessage(ChatColors.color("&eClick here:"));
		sender.sendMessage(ChatColors.color("&7&o" + url));
		sender.sendMessage("");
	}
	
	public static String christmas(String text) {
		return ChatColors.alternating(text, ChatColor.GREEN, ChatColor.RED);
	}

}
