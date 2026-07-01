package io.github.thebusybiscuit.slimefun5.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun5.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun5.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

class CheatCommand extends SubCommand {

    @ParametersAreNonnullByDefault
    CheatCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "cheat", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (SlimefunGuide.canUseCheatSheet(player)) {
                SlimefunGuide.openCheatMenu(player);
            } else {
                Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
            }
        } else {
            Slimefun.getLocalization().sendMessage(sender, "messages.only-players", true);
        }
    }

}

