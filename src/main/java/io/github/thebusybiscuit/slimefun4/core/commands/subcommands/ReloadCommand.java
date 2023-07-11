package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import org.bukkit.command.CommandSender;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import javax.annotation.Nonnull;

class ReloadCommand extends SubCommand {

    ReloadCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "reload", false);
    }

    @Override
    protected @Nonnull String getDescription() {
        return "commands.reload.description";
    }

    @Override
    public void onExecute(CommandSender sender, @Nonnull String[] args) {
        if (sender.hasPermission("slimefun.command.reload")) {
            Slimefun.getLocalization().sendMessage(sender, "guide.work-in-progress", true);
            boolean reloaded = Slimefun.getConfigManager().reload();
            boolean isRestartRequired = Slimefun.getConfigManager().    isRestartRequired();

            if (reloaded) {
                if (isRestartRequired) {
                    Slimefun.getLocalization().sendMessage(sender, "commands.reload.restart-required", true);
                } else {
                    Slimefun.getLocalization().sendMessage(sender, "commands.reload.success", true);
                }
            } else {
                Slimefun.getLocalization().sendMessage(sender, "commands.reload.errors", true);
            }
        } else {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

}