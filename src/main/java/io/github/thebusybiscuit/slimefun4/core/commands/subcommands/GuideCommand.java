package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class GuideCommand extends SubCommand {

	public GuideCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
		super(plugin, cmd);
	}

	@Override
	public String getName() {
		return "guide";
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("slimefun.command.guide")) {
				((Player) sender).getInventory().addItem(SlimefunGuide.getItem(SlimefunPlugin.getCfg().getBoolean("guide.default-view-book") ? SlimefunGuideLayout.BOOK : SlimefunGuideLayout.CHEST));
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
