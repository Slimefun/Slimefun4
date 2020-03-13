package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;

class DebugFishCommand extends SubCommand {

    public DebugFishCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd);
    }

    @Override
    public String getName() {
        return "debug_fish";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player && sender.isOp()) {
            ((Player) sender).getInventory().addItem(SlimefunItems.DEBUG_FISH);
        }
        else {
            SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
        }
    }

}
