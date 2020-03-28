package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

class CheatCommand extends SubCommand {

    CheatCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd);
    }

    @Override
    public String getName() {
        return "cheat";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("slimefun.cheat.items")) {
                SlimefunGuide.openCheatMenu((Player) sender);
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
