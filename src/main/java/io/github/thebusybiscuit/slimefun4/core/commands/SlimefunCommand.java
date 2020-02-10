package io.github.thebusybiscuit.slimefun4.core.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.ReportCommand;
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
import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.CheatCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.GiveCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.GuideCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.HelpCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.OpenGuideCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.ResearchCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.SearchCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.StatsCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.TeleporterCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.TimingsCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.subcommands.VersionsCommand;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.gps.ElevatorPlate;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class SlimefunCommand implements CommandExecutor, Listener {

	private final List<SubCommand> commands = new LinkedList<>();

	public SlimefunCommand(SlimefunPlugin plugin) {
		commands.add(new HelpCommand(plugin, this));
		commands.add(new VersionsCommand(plugin, this));
		commands.add(new CheatCommand(plugin, this));
		commands.add(new GuideCommand(plugin, this));
		commands.add(new GiveCommand(plugin, this));
		commands.add(new ResearchCommand(plugin, this));
		commands.add(new StatsCommand(plugin, this));
		commands.add(new TimingsCommand(plugin, this));
		commands.add(new TeleporterCommand(plugin, this));
		commands.add(new OpenGuideCommand(plugin, this));
		commands.add(new SearchCommand(plugin, this));
		commands.add(new ReportCommand(plugin, this));

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
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
