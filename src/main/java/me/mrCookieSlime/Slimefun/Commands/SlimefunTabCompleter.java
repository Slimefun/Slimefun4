package me.mrCookieSlime.Slimefun.Commands;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class SlimefunTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			return SlimefunCommand.tabs;
		}
		else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("give")) {
				return Slimefun.listIDs();
			}
			else if (args[0].equalsIgnoreCase("research")) {
				List<String> researches = new ArrayList<String>();
				for(Research res: Research.list()) {
					researches.add(res.getName().toUpperCase().replace(" ", "_"));
				}
				researches.add("all");
				return researches;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}

}
