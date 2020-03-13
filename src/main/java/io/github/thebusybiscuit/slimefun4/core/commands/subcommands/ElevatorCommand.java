package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.items.gps.ElevatorPlate;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

class ElevatorCommand extends SubCommand {

    private final ElevatorPlate elevatorPlate;

    public ElevatorCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd);

        elevatorPlate = ((ElevatorPlate) SlimefunItem.getByID("ELEVATOR_PLATE"));
    }

    @Override
    public String getName() {
        return "elevator";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player && args.length == 4) {
            Player p = (Player) sender;

            int x = Integer.parseInt(args[1]);
            int y = Integer.parseInt(args[2]);
            int z = Integer.parseInt(args[3]);

            if (BlockStorage.getLocationInfo(p.getWorld().getBlockAt(x, y, z).getLocation(), "floor") != null) {
                elevatorPlate.getUsers().add(p.getUniqueId());
                float yaw = p.getEyeLocation().getYaw() + 180;
                if (yaw > 180) yaw = -180 + (yaw - 180);

                p.teleport(new Location(p.getWorld(), x + 0.5, y + 0.4, z + 0.5, yaw, p.getEyeLocation().getPitch()));

                String floor = BlockStorage.getLocationInfo(p.getWorld().getBlockAt(x, y, z).getLocation(), "floor");
                p.sendTitle(ChatColor.RESET + ChatColors.color(floor), " ", 20, 60, 20);
            }
        }
    }

}
