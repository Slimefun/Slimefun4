package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.players.PlayerList;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class StatsCommand extends SubCommand {

    StatsCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "stats", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            if (sender.hasPermission("slimefun.stats.others") || sender instanceof ConsoleCommandSender) {
                Optional<Player> player = PlayerList.findByName(args[1]);

                if (player.isPresent()) {
                    PlayerProfile.get(player.get(), profile -> profile.sendStats(sender));
                } else {
                    SlimefunPlugin.getLocalization().sendMessage(sender, "messages.not-online", true, msg -> msg.replace("%player%", args[1]));
                }
            } else {
                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
            }
        } else if (sender instanceof Player) {
            PlayerProfile.get((Player) sender, profile -> profile.sendStats(sender));
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.only-players", true);
        }
    }

}
