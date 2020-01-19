package io.github.thebusybiscuit.slimefun4.core.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public abstract class SubCommand {

	protected final SlimefunPlugin plugin;
	protected final SlimefunCommand cmd;
	
	protected SubCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
		this.plugin = plugin;
		this.cmd = cmd;
	}
	
	public abstract String getName();
	
	public abstract void onExecute(CommandSender sender, String[] args);

	protected String getDescriptionPath() {
		return "commands." + getName();
	}
	
	public String getDescription(CommandSender sender) {
		if (sender instanceof Player) {
			return SlimefunPlugin.getLocal().getMessage((Player) sender, getDescriptionPath());
		}
		else {
			return SlimefunPlugin.getLocal().getMessage(getDescriptionPath());
		}
	}

}
