package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class OpenGuideCommand extends SubCommand {

    OpenGuideCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "open_guide", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("slimefun.command.open_guide")) {
                boolean book = SlimefunPlugin.getCfg().getBoolean("guide.default-view-book");
                SlimefunGuide.openGuide((Player) sender, book ? SlimefunGuideLayout.BOOK : SlimefunGuideLayout.CHEST);
            } else {
                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
            }
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.only-players", true);
        }
    }

}
