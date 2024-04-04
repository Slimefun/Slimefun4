package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.services.AutoSavingService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SaveCommand extends SubCommand {
    private final AutoSavingService service;

    @ParametersAreNonnullByDefault
    SaveCommand(Slimefun plugin, SlimefunCommand cmd, AutoSavingService service) {
        super(plugin, cmd, "save", false);
        this.service = service;
    }

    @Override
    public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        if (!(sender instanceof Player)) {
            Slimefun.getLocalization().sendMessage(sender, "messages.only-players", true);
            return;
        }

        if (sender.hasPermission("slimefun.command.save")) {
            boolean savedPlayers = this.service.saveAllPlayers(false);
            Slimefun.getLocalization().sendMessage(sender, "commands.save.players." + savedPlayers, true);
            boolean savedBlocks = this.service.saveAllBlocks(false);
            Slimefun.getLocalization().sendMessage(sender, "commands.save.blocks." + savedBlocks, true);
        } else {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }
}
