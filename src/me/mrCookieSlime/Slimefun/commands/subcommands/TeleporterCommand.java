package me.mrCookieSlime.Slimefun.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.GPS.GPSNetwork;
import me.mrCookieSlime.Slimefun.commands.SlimefunCommand;
import me.mrCookieSlime.Slimefun.commands.SubCommand;

public class TeleporterCommand extends SubCommand {

	public TeleporterCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
		super(plugin, cmd);
	}

	@Override
	public String getName() {
		return "teleporter";
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (args.length == 2) {
			if (sender.hasPermission("slimefun.command.teleporter") && sender instanceof Player) {
				OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
				if (player.getName() != null) {
					GPSNetwork.openTeleporterGUI((Player) sender, player.getUniqueId(), ((Player) sender).getLocation().getBlock().getRelative(BlockFace.DOWN), 999999999);
				}
				else sender.sendMessage("&4Unknown Player: &c" + args[1]);
			}
			else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
		}
		else SlimefunPlugin.getLocal().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf teleporter <Player>"));
	}

}
