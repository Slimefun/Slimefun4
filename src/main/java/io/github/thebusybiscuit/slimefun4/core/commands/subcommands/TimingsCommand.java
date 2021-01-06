package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.core.services.profiler.PerformanceInspector;
import io.github.thebusybiscuit.slimefun4.core.services.profiler.inspectors.ConsolePerformanceInspector;
import io.github.thebusybiscuit.slimefun4.core.services.profiler.inspectors.PlayerPerformanceInspector;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class TimingsCommand extends SubCommand {

    TimingsCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "timings", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender.hasPermission("slimefun.command.timings") || sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Please wait a second... The results are coming in!");
            PerformanceInspector inspector = inspectorOf(sender);
            SlimefunPlugin.getProfiler().requestSummary(inspector);
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

    @Nonnull
    private PerformanceInspector inspectorOf(@Nonnull CommandSender sender) {
        if (sender instanceof Player) {
            return new PlayerPerformanceInspector((Player) sender);
        } else {
            return new ConsolePerformanceInspector(sender);
        }
    }

}
