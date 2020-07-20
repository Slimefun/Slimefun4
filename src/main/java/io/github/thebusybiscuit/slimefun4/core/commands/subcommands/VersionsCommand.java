package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import java.util.Collection;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

class VersionsCommand extends SubCommand {

    VersionsCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd);
    }

    @Override
    public String getName() {
        return "versions";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender.hasPermission("slimefun.command.versions") || sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColors.color("&a" + Bukkit.getName() + " &2" + ReflectionUtils.getVersion()));
            sender.sendMessage("");
            sender.sendMessage(ChatColors.color("&aCS-CoreLib &2v" + SlimefunPlugin.getCSCoreLibVersion()));
            sender.sendMessage(ChatColors.color("&aSlimefun &2v" + SlimefunPlugin.getVersion())
                + (SlimefunPlugin.getMetricsService().getVersion() != null
                ? " (Metrics build: #" + SlimefunPlugin.getMetricsService().getVersion() + ')'
                : "")
            );

            if (SlimefunPlugin.getRegistry().isBackwardsCompatible()) {
                sender.sendMessage(ChatColor.YELLOW + "Backwards compatiblity enabled!");
            }

            sender.sendMessage("");

            Collection<Plugin> addons = SlimefunPlugin.getInstalledAddons();
            sender.sendMessage(ChatColors.color("&7Installed Addons &8(" + addons.size() + ")"));

            for (Plugin plugin : addons) {
                if (Bukkit.getPluginManager().isPluginEnabled(plugin)) {
                    sender.sendMessage(ChatColors.color(" &a" + plugin.getName() + " &2v" + plugin.getDescription().getVersion()));
                }
                else {
                    sender.sendMessage(ChatColors.color(" &c" + plugin.getName() + " &4v" + plugin.getDescription().getVersion()));
                }
            }
        }
        else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

}
