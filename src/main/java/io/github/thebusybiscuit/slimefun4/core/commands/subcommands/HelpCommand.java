package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import org.bukkit.command.CommandSender;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

class HelpCommand extends SubCommand {

    HelpCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd);
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        cmd.sendHelp(sender);
    }

}
