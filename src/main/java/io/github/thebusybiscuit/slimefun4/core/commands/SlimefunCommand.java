package io.github.thebusybiscuit.slimefun4.core.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.CommandSetup;
import io.github.thebusybiscuit.slimefun4.implementation.items.gps.ElevatorPlate;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class SlimefunCommand implements CommandExecutor, Listener {

    private final SlimefunPlugin plugin;
    private final List<SubCommand> commands = new LinkedList<>();
    private final Map<SubCommand, Integer> commandUsage = new HashMap<>();

    public SlimefunCommand(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        plugin.getCommand("slimefun").setExecutor(this);
        plugin.getCommand("slimefun").setTabCompleter(new SlimefunTabCompleter(this));

        this.plugin = plugin;
        CommandSetup.addCommands(this, commands);
    }

    public SlimefunPlugin getPlugin() {
        return plugin;
    }

    public Map<SubCommand, Integer> getCommandUsage() {
        return commandUsage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("debug_fish")) {
                if (sender instanceof Player && sender.isOp()) {
                    ((Player) sender).getInventory().addItem(SlimefunItems.DEBUG_FISH);
                }
                else {
                    SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
                }

                return true;
            }
            else if (args[0].equalsIgnoreCase("elevator")) {
                if (sender instanceof Player && args.length == 4) {
                    Player p = (Player) sender;

                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int z = Integer.parseInt(args[3]);

                    if (BlockStorage.getLocationInfo(p.getWorld().getBlockAt(x, y, z).getLocation(), "floor") != null) {
                        ((ElevatorPlate) SlimefunItem.getByID("ELEVATOR_PLATE")).getUsers().add(p.getUniqueId());
                        float yaw = p.getEyeLocation().getYaw() + 180;
                        if (yaw > 180) yaw = -180 + (yaw - 180);

                        p.teleport(new Location(p.getWorld(), x + 0.5, y + 0.4, z + 0.5, yaw, p.getEyeLocation().getPitch()));

                        String floor = BlockStorage.getLocationInfo(p.getWorld().getBlockAt(x, y, z).getLocation(), "floor");
                        p.sendTitle(ChatColor.RESET + ChatColors.color(floor), " ", 20, 60, 20);
                    }
                }
                return true;
            }
            else {
                for (SubCommand command : commands) {
                    if (args[0].equalsIgnoreCase(command.getName())) {
                        commandUsage.merge(command, 1, Integer::sum);
                        command.onExecute(sender, args);
                        return true;
                    }
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
            sender.sendMessage(ChatColors.color("&3/sf " + cmd.getName() + " &b") + cmd.getDescription(sender));
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
