package me.mrCookieSlime.Slimefun.commands.subcommands;

import org.bukkit.command.CommandSender;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.commands.SlimefunCommand;
import me.mrCookieSlime.Slimefun.commands.SubCommand;

public class HelpCommand extends SubCommand {

	public HelpCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
		super(plugin, cmd);
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		cmd.sendHelp(sender);
	}

}
