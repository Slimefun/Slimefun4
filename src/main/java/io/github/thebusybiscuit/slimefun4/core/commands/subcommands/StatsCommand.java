package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.players.PlayerList;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;

public class StatsCommand extends SubCommand {

	public StatsCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
		super(plugin, cmd);
	}

	@Override
	public String getName() {
		return "stats";
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (args.length > 1) {
			if (sender.hasPermission("slimefun.stats.others") || sender instanceof ConsoleCommandSender) {
				Optional<Player> player = PlayerList.findByName(args[1]);
				if (player.isPresent()) {
					PlayerProfile.get(player.get(), profile -> profile.sendStats(sender));
				}
				else {
					SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-online", true, msg -> msg.replace("%player%", args[1]));
				}
			}
			else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
		}
		else if (sender instanceof Player) {
			PlayerProfile.get((Player) sender, profile -> profile.sendStats(sender));
		}
		else {
			SlimefunPlugin.getLocal().sendMessage(sender, "messages.only-players", true);
		}
	}

}
