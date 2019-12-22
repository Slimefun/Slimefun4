package io.github.thebusybiscuit.slimefun4.core.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
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
import me.mrCookieSlime.CSCoreLibPlugin.general.World.TitleBuilder;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.TitleBuilder.TitleType;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

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
					double x = Integer.parseInt(args[1]) + 0.5D;
					double y = Integer.parseInt(args[2]) + 0.4D;
					double z = Integer.parseInt(args[3]) + 0.5D;
					
					if (BlockStorage.getLocationInfo(((Player) sender).getWorld().getBlockAt(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])).getLocation(), "floor") != null) {
						SlimefunPlugin.getUtilities().elevatorUsers.add(((Player) sender).getUniqueId());
						float yaw = ((Player) sender).getEyeLocation().getYaw() + 180;
						if (yaw > 180) yaw = -180 + (yaw - 180);
						((Player) sender).teleport(new Location(((Player) sender).getWorld(), x, y, z, yaw, ((Player) sender).getEyeLocation().getPitch()));
						
						try {
							TitleBuilder title = (TitleBuilder) new TitleBuilder(20, 60, 20).addText("&r" + ChatColor.translateAlternateColorCodes('&', BlockStorage.getLocationInfo(((Player) sender).getWorld().getBlockAt(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])).getLocation(), "floor")));
							TitleBuilder subtitle = (TitleBuilder) new TitleBuilder(20, 60, 20).addText(" ");
							
							title.send(TitleType.TITLE, ((Player) sender));
							subtitle.send(TitleType.SUBTITLE, ((Player) sender));
						} catch (Exception e) {
							Slimefun.getLogger().log(Level.SEVERE, "An Error occured while a Player used an Elevator in Slimefun " + Slimefun.getVersion(), e);
						}
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
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSlimefun &2v" + Slimefun.getVersion()));
		sender.sendMessage("");
		
		for (SubCommand cmd : commands) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/sf " + cmd.getName() + " &b") + cmd.getDescription());
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
