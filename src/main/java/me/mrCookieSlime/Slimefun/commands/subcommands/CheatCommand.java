package me.mrCookieSlime.Slimefun.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.commands.SlimefunCommand;
import me.mrCookieSlime.Slimefun.commands.SubCommand;

public class CheatCommand extends SubCommand {

	public CheatCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
		super(plugin, cmd);
	}

	@Override
	public String getName() {
		return "cheat";
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("slimefun.cheat.items")) {
				SlimefunGuide.openCheatMenu((Player) sender);
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
