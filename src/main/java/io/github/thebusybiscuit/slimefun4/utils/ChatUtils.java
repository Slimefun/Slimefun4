package io.github.thebusybiscuit.slimefun4.utils;

import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.chat.ChatInput;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public final class ChatUtils {

	private ChatUtils() {}

	public static void sendURL(CommandSender sender, String url) {
		sender.sendMessage("");
		SlimefunPlugin.getLocal().sendMessage(sender, "messages.link-prompt");
		sender.sendMessage(ChatColors.color("&7&o" + url));
		sender.sendMessage("");
	}

	public static String christmas(String text) {
		return ChatColors.alternating(text, ChatColor.GREEN, ChatColor.RED);
	}

	public static void awaitInput(Player p, Consumer<String> callback) {
		ChatInput.waitForPlayer(SlimefunPlugin.instance, p, callback);
	}

	public static String humanize(String string) {
		StringBuilder builder = new StringBuilder();

		String[] segments = string.toLowerCase().split("_");

		builder.append(Character.toUpperCase(segments[0].charAt(0)));
		builder.append(segments[0].substring(1));

		for (int i = 1; i < segments.length; i++) {
			String segment = segments[i];
			builder.append(" ");
			builder.append(Character.toUpperCase(segment.charAt(0)));
			builder.append(segment.substring(1));
		}

		return builder.toString();
	}

}
