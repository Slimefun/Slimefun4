package me.mrCookieSlime.Slimefun.Commands;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.scheduler.BukkitRunnable;

public class SlimefunTabCompleter implements TabCompleter {

	private List<String> originalSlimefunList;
	private boolean isUnprocessed = true;
	private final String listUpdateMessage = "&7Slimefun item list is being updated in the background! Please wait..";

	public SlimefunTabCompleter() {
		originalSlimefunList = Slimefun.listIDs();
		originalSlimefunList.add("COLORED_ENDER_CHEST_BIG");
		originalSlimefunList.add("COLORED_ENDER_CHEST_SMALL");
		if (hasColoredEnderChestItems()) removeColoredEnderChestItems();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			return createReturnList(SlimefunCommand.tabs, args[0]);
		}
		else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("give")) {
				if (isUnprocessed) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', listUpdateMessage));
					return null;
				}
				//TODO Make the code not check through all the items(probably requires the knowledge of Colored Enderchest
				//safety check for items in case of dynamic plugin reloading
				if (hasColoredEnderChestItems()) {
					removeColoredEnderChestItems();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', listUpdateMessage));
					return null;
				}
				return createReturnList(originalSlimefunList, args[2]);
			}
			else if (args[0].equalsIgnoreCase("research")) {
				List<String> researches = new ArrayList<String>();
				for(Research res: Research.list()) {
					researches.add(res.getName().toUpperCase().replace(" ", "_"));
				}
				researches.add("all");
				return createReturnList(researches, args[2]);
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}

	private boolean hasColoredEnderChestItems() {
		return (isUnprocessed = originalSlimefunList.contains("COLORED_ENDER_CHEST_BIG_0_0_0") ||
				originalSlimefunList.contains("COLORED_ENDER_CHEST_SMALL_0_0_0"));
	}

	private void removeColoredEnderChestItems() {
		new BukkitRunnable() {
			@Override
			public void run() {
				originalSlimefunList.removeIf(string -> string.startsWith("COLORED_ENDER_CHEST_BIG_") ||
				string.startsWith("COLORED_ENDER_CHEST_SMALL_"));
				isUnprocessed = false;
			}
		}.runTaskAsynchronously(SlimefunStartup.instance);
	}

	/***
	 * Returns a sublist from a given list containing items that start with the given string if string is not empty
	 * @param list The list to process
	 * @param string The typed string
	 * @return Sublist if string is not empty
	 */

	private List<String> createReturnList(List<String> list, String string) {
		if (string.equals("")) return list;

		List<String> returnList = new ArrayList<>();
		for (String item : list) {
			if (item.toLowerCase().startsWith(string.toLowerCase())) {
				returnList.add(item);
			}
		}
		return returnList;
	}
}
