package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

class TeleporterCommand extends SubCommand {

    @ParametersAreNonnullByDefault
    TeleporterCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "teleporter", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (sender.hasPermission("slimefun.command.teleporter")) {
                if (args.length == 1) {
                    Slimefun.getGPSNetwork().getTeleportationManager().openTeleporterGUI(player, player.getUniqueId(), player.getLocation().getBlock().getRelative(BlockFace.DOWN), 999999999);
                } else if (args.length == 2) {

                    @SuppressWarnings("deprecation")
                    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);

                    if (targetPlayer.getName() != null) {
                        Slimefun.getGPSNetwork().getTeleportationManager().openTeleporterGUI(player, targetPlayer.getUniqueId(), player.getLocation().getBlock().getRelative(BlockFace.DOWN), 999999999);
                    } else {
                        Slimefun.getLocalization().sendMessage(sender, "messages.unknown-player", msg -> msg.replace("%player%", args[1]));
                    }
                } else {
                    Slimefun.getLocalization().sendMessage(sender, "messages.usage", msg -> msg.replace("%usage%", "/sf teleporter [Player]"));
                }
            } else {
                Slimefun.getLocalization().sendMessage(sender, "messages.no-permission");
            }
        } else {
            Slimefun.getLocalization().sendMessage(sender, "messages.only-players");
        }
    }

}
