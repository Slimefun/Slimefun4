package io.github.thebusybiscuit.slimefun4.core.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.Commands;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

/**
 * This {@link CommandExecutor} holds the functionality of our {@code /slimefun} command.
 * 
 * @author TheBusyBiscuit
 *
 */
public class SlimefunCommand implements CommandExecutor, Listener {

    private final SlimefunPlugin plugin;
    private final List<SubCommand> commands = new LinkedList<>();
    private final Map<SubCommand, Integer> commandUsage = new HashMap<>();

    public SlimefunCommand(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        plugin.getCommand("slimefun").setExecutor(this);
        plugin.getCommand("slimefun").setTabCompleter(new SlimefunTabCompleter(this));

        this.plugin = plugin;
        Commands.addCommands(this, commands);
    }

    public SlimefunPlugin getPlugin() {
        return plugin;
    }

    /**
     * Returns a heatmap of how often certain commands are used.
     * 
     * @return A {@link Map} holding the amount of times each command was run
     */
    public Map<SubCommand, Integer> getCommandUsage() {
        return commandUsage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            for (SubCommand command : commands) {
                if (args[0].equalsIgnoreCase(command.getName())) {
                    command.recordUsage(commandUsage);
                    command.onExecute(sender, args);
                    return true;
                }
            }
        }

        sendHelp(sender);

        return true;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&aSlimefun &2v" + SlimefunPlugin.getVersion()));
        sender.sendMessage("");

        for (SubCommand cmd : commands) {
            if (!cmd.isHidden()) {
                sender.sendMessage(ChatColors.color("&3/sf " + cmd.getName() + " &b") + cmd.getDescription(sender));
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/help slimefun")) {
            sendHelp(e.getPlayer());
            e.setCancelled(true);
        }
    }

    public List<String> getTabArguments() {
        return commands.stream().map(SubCommand::getName).collect(Collectors.toList());
    }

}
