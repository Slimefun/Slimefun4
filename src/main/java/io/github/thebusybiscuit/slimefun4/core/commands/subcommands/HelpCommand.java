package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.command.CommandSender;

class HelpCommand extends SubCommand {

    public HelpCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd);
    }

    @Override
    public String getName() {
        return "help";
    }

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		cmd.sendHelp(sender);
	}

}
