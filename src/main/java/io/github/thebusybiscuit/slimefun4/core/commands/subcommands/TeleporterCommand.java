package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class TeleporterCommand extends SubCommand {

	public TeleporterCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
		super(plugin, cmd);
	}

	@Override
	public String getName() {
		return "teleporter";
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (args.length == 2) {
			if (sender.hasPermission("slimefun.command.teleporter") && sender instanceof Player) {
				OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
				if (player.getName() != null) {
					Slimefun.getGPSNetwork().openTeleporterGUI((Player) sender, player.getUniqueId(), ((Player) sender).getLocation().getBlock().getRelative(BlockFace.DOWN), 999999999);
				}
				else sender.sendMessage("&4Unknown Player: &c" + args[1]);
			}
			else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
		}
		else SlimefunPlugin.getLocal().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf teleporter <Player>"));
	}

}
