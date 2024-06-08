package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.core.services.SavingService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SaveCommand extends SubCommand {
    @ParametersAreNonnullByDefault
    SaveCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "save", false);
    }

    @Override
    public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        if (!sender.hasPermission("slimefun.command.save")) {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
            return;
        }

        boolean savedPlayers = Slimefun.getSavingService().saveAllPlayers(false);
        Slimefun.getLocalization().sendMessage(sender, "commands.save.players." + savedPlayers, true);
        boolean savedBlocks = Slimefun.getSavingService().saveAllBlocks(false);
        Slimefun.getLocalization().sendMessage(sender, "commands.save.blocks." + savedBlocks, true);
    }
}
