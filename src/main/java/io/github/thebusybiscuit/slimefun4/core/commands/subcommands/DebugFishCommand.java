package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

class DebugFishCommand extends SubCommand {

    @ParametersAreNonnullByDefault
    DebugFishCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "debug_fish", true);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player player && sender.hasPermission("slimefun.debugging")) {
            player.getInventory().addItem(SlimefunItems.DEBUG_FISH.clone());
        } else {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

}
