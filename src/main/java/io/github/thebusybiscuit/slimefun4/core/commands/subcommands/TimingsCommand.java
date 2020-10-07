package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class TimingsCommand extends SubCommand {

    TimingsCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "timings", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender.hasPermission("slimefun.command.timings") || sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Please wait a second... The results are coming in!");
            SlimefunPlugin.getProfiler().requestSummary(sender);
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

}
