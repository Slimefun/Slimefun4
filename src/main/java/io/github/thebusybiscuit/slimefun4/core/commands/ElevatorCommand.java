package io.github.thebusybiscuit.slimefun4.core.commands;

import javax.annotation.Nonnull;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.elevator.ElevatorPlate;

/**
 * This {@link Command} is responsible for handling the callback for an {@link ElevatorPlate}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see ElevatorPlate
 *
 */
public class ElevatorCommand implements CommandExecutor, Listener {

    private final ElevatorPlate elevatorPlate;

    public ElevatorCommand(@Nonnull SlimefunPlugin plugin, @Nonnull ElevatorPlate elevatorPlate) {
        this.elevatorPlate = elevatorPlate;

        plugin.getCommand("elevator").setExecutor(this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            return elevatorPlate.useElevator((Player) sender, args[0]);
        } else {
            return false;
        }
    }

    @EventHandler
    public void onChatEvent(PlayerQuitEvent e) {
        // Clearing these tokens will prevent memory leaks
        elevatorPlate.clearTokens(e.getPlayer());
    }

}
