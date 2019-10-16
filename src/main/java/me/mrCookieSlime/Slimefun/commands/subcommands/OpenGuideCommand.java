package me.mrCookieSlime.Slimefun.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.commands.SlimefunCommand;
import me.mrCookieSlime.Slimefun.commands.SubCommand;

public class OpenGuideCommand extends SubCommand {

	public OpenGuideCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
		super(plugin, cmd);
	}

	@Override
	public String getName() {
		return "open_guide";
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (sender instanceof Player) { 
			if (sender.hasPermission("slimefun.command.open_guide")) {
				SlimefunGuide.openGuide((Player) sender, SlimefunPlugin.getCfg().getBoolean("guide.default-view-book"));
			}
			else {
				SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
			}
		}
		else {
			SlimefunPlugin.getLocal().sendMessage(sender, "messages.only-players", true);
		}
	}

}
