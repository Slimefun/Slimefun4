package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import org.bukkit.command.CommandSender;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class ReloadCommand extends SubCommand {

    ReloadCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "reload", false);
    }

    @Override
    protected String getDescription() {
        return "commands.reload.description";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender.hasPermission("slimefun.command.reload")) {
            SlimefunPlugin.getLocalization().sendMessage(sender, "guide.work-in-progress", true);
            boolean reloaded = SlimefunPlugin.getConfigManager().reload();
            boolean isRestartRequired = SlimefunPlugin.getConfigManager().isRestartRequired();

            if (reloaded) {
                if (isRestartRequired) {
                    SlimefunPlugin.getLocalization().sendMessage(sender, "commands.reload.restart-required", true);
                } else {
                    SlimefunPlugin.getLocalization().sendMessage(sender, "commands.reload.success", true);
                }
            } else {
                SlimefunPlugin.getLocalization().sendMessage(sender, "commands.reload.errors", true);
            }
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

}
