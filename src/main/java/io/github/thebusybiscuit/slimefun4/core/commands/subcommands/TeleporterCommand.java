package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class TeleporterCommand extends SubCommand {

    @ParametersAreNonnullByDefault
    TeleporterCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "teleporter", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("slimefun.command.teleporter")) {
                if (args.length == 1) {
                    Player p = (Player) sender;
                    SlimefunPlugin.getGPSNetwork().getTeleportationManager().openTeleporterGUI(p, p.getUniqueId(), p.getLocation().getBlock().getRelative(BlockFace.DOWN), 999999999);
                } else if (args.length == 2) {

                    @SuppressWarnings("deprecation")
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);

                    if (player.getName() != null) {
                        SlimefunPlugin.getGPSNetwork().getTeleportationManager().openTeleporterGUI((Player) sender, player.getUniqueId(), ((Player) sender).getLocation().getBlock().getRelative(BlockFace.DOWN), 999999999);
                    } else {
                        SlimefunPlugin.getLocalization().sendMessage(sender, "messages.unknown-player", msg -> msg.replace("%player%", args[1]));
                    }
                } else {
                    SlimefunPlugin.getLocalization().sendMessage(sender, "messages.usage", msg -> msg.replace("%usage%", "/sf teleporter [Player]"));
                }
            } else {
                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission");
            }
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.only-players");
        }
    }

}
