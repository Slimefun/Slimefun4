package io.github.thebusybiscuit.slimefun4.core.commands;

import org.bukkit.command.CommandSender;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public abstract class SubCommand {

	protected final SlimefunPlugin plugin;
	protected final SlimefunCommand cmd;
	private final String description;
	
	protected SubCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
		this.plugin = plugin;
		this.cmd = cmd;
		
		this.description = SlimefunPlugin.getLocal().getMessage(getDescriptionPath());
	}
	
	public abstract String getName();
	
	public abstract void onExecute(CommandSender sender, String[] args);

	protected String getDescriptionPath() {
		return "commands." + getName();
	}
	
	public String getDescription() {
		return description;
	}

}
