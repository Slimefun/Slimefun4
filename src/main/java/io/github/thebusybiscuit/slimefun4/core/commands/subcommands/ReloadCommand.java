package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

class ReloadCommand extends SubCommand {
    public ReloadCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd);
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender.hasPermission("slimefun.command.reload") || sender instanceof ConsoleCommandSender) {
            SlimefunPlugin.getCfg().reload();
            SlimefunPlugin.getItemCfg().reload();
            SlimefunPlugin.getLocal().sendMessage(sender, "messages.reloaded", true);
        } else {
            SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
        }
    }
}
