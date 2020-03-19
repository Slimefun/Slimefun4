package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

class SearchCommand extends SubCommand {

    SearchCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd);
    }

    @Override
    public String getName() {
        return "search";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("slimefun.command.search")) {
                if (args.length > 1) {
                    String query = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    PlayerProfile.get((Player) sender, profile -> SlimefunGuide.openSearch(profile, query, true, true));
                }
                else {
                    SlimefunPlugin.getLocal().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf search <SearchTerm>"));
                }
            }
            else {
                SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
            }
        }
        else {
            SlimefunPlugin.getLocal().sendMessage(sender, "messages.only-players", true);
        }
    }

}
