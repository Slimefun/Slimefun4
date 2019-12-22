package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class VersionsCommand extends SubCommand {

	public VersionsCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
		super(plugin, cmd);
	}

	@Override
	public String getName() {
		return "versions";
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (sender.hasPermission("slimefun.command.versions")|| sender instanceof ConsoleCommandSender) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + Bukkit.getName() + " &2" + ReflectionUtils.getVersion()));
			sender.sendMessage("");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCS-CoreLib &2v" + CSCoreLib.getLib().getDescription().getVersion()));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSlimefun &2v" + plugin.getDescription().getVersion()));
			sender.sendMessage("");
			
			List<String> addons = new ArrayList<>();
			
			for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
				if (plugin.getDescription().getDepend().contains("Slimefun") || plugin.getDescription().getSoftDepend().contains("Slimefun")) {
					if (Bukkit.getPluginManager().isPluginEnabled(plugin)) {
						addons.add(ChatColor.translateAlternateColorCodes('&', " &a" + plugin.getName() + " &2v" + plugin.getDescription().getVersion()));
					}
					else {
						addons.add(ChatColor.translateAlternateColorCodes('&', " &c" + plugin.getName() + " &4v" + plugin.getDescription().getVersion()));
					}
				}
			}
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Installed Addons &8(" + addons.size() + ")"));
			
			for (String addon : addons) {
				sender.sendMessage(addon);
			}
		}
		else {
			SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
		}
	}

}
