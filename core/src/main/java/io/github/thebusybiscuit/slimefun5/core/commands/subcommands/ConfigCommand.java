package io.github.thebusybiscuit.slimefun5.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun5.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun5.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun5.core.guide.options.ConfigEditorMenu;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * {@code /sf config} - opens the in-game config editor for admins.
 *
 * @author Slimefun5
 *
 * @see ConfigEditorMenu
 */
class ConfigCommand extends SubCommand {

    @ParametersAreNonnullByDefault
    ConfigCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "config", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!ConfigEditorMenu.isEnabled()) {
                Slimefun.getLocalization().sendMessage(sender, "guide.config.disabled", true);
            } else if (ConfigEditorMenu.canUse(player)) {
                ConfigEditorMenu.open(player);
            } else {
                Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
            }
        } else {
            Slimefun.getLocalization().sendMessage(sender, "messages.only-players", true);
        }
    }
}
