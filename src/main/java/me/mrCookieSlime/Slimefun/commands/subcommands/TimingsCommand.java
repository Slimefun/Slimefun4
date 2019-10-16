package me.mrCookieSlime.Slimefun.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.commands.SlimefunCommand;
import me.mrCookieSlime.Slimefun.commands.SubCommand;

public class TimingsCommand extends SubCommand {

	public TimingsCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
		super(plugin, cmd);
	}

	@Override
	public String getName() {
		return "timings";
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (sender.hasPermission("slimefun.command.timings")|| sender instanceof ConsoleCommandSender) {
			SlimefunPlugin.getTicker().info(sender);
		}
		else {
			SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
		}
	}

}
