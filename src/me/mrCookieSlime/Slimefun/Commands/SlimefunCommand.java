package me.mrCookieSlime.Slimefun.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Variable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.CommandHelp;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Player.Players;
import me.mrCookieSlime.CSCoreLibPlugin.general.Reflection.ReflectionUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.TitleBuilder;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.TitleBuilder.TitleType;
import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.GPS.Elevator;
import me.mrCookieSlime.Slimefun.GPS.GPSNetwork;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Misc.BookDesign;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class SlimefunCommand implements CommandExecutor, Listener {

	public SlimefunStartup plugin;

	public static List<String> arguments = new ArrayList<>();
	public static List<String> descriptions = new ArrayList<>();
	public static List<String> tabs = new ArrayList<>();

	public SlimefunCommand(SlimefunStartup plugin) {
		this.plugin = plugin;
		
		arguments.add("/sf help");
		tabs.add("help");
		descriptions.add(Messages.local.getTranslation("commands.help").get(0));
		
		arguments.add("/sf versions");
		tabs.add("versions");
		descriptions.add(Messages.local.getTranslation("commands.versions").get(0));
		
		arguments.add("/sf cheat");
		tabs.add("cheat");
		descriptions.add(Messages.local.getTranslation("commands.cheat").get(0));
		
		arguments.add("/sf give");
		tabs.add("give");
		descriptions.add(Messages.local.getTranslation("commands.give").get(0));
		
		arguments.add("/sf research");
		tabs.add("research");
		descriptions.add(Messages.local.getTranslation("commands.research.desc").get(0));
		
		arguments.add("/sf guide");
		tabs.add("guide");
		descriptions.add(Messages.local.getTranslation("commands.guide").get(0));
		
		arguments.add("/sf stats");
		tabs.add("stats");
		descriptions.add(Messages.local.getTranslation("commands.stats").get(0));
		
		arguments.add("/sf timings");
		tabs.add("timings");
		descriptions.add(Messages.local.getTranslation("commands.timings").get(0));
		
		arguments.add("/sf teleporter");
		tabs.add("teleporter");
		descriptions.add(Messages.local.getTranslation("commands.teleporter").get(0));
		
		arguments.add("/sf open_guide");
		tabs.add("open_guide");
		descriptions.add(Messages.local.getTranslation("commands.open_guide").get(0));
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sendHelp(sender);
		}
		else if (args.length > 0) {
			if (args[0].equalsIgnoreCase("cheat")) {
				if (sender instanceof Player) {
					if (sender.hasPermission("slimefun.cheat.items")) SlimefunGuide.openCheatMenu((Player) sender);
					else Messages.local.sendTranslation(sender, "messages.no-permission", true);
				}
				else Messages.local.sendTranslation(sender, "messages.only-players", true);
			}
			else if (args[0].equalsIgnoreCase("guide")) {
				if (sender instanceof Player) {
					if (sender.hasPermission("slimefun.command.guide")) ((Player) sender).getInventory().addItem(SlimefunGuide.getItem(SlimefunStartup.getCfg().getBoolean("guide.default-view-book") ? BookDesign.BOOK : BookDesign.CHEST));
					else Messages.local.sendTranslation(sender, "messages.no-permission", true);
				}
				else Messages.local.sendTranslation(sender, "messages.only-players", true);
			}
			else if (args[0].equalsIgnoreCase("open_guide")) {
				if (sender instanceof Player) { 
					if (sender.hasPermission("slimefun.command.open_guide")) SlimefunGuide.openGuide((Player) sender, SlimefunStartup.getCfg().getBoolean("guide.default-view-book"));
					else Messages.local.sendTranslation(sender, "messages.no-permission", true);
				}
				else Messages.local.sendTranslation(sender, "messages.only-players", true);
			}
			else if (args[0].equalsIgnoreCase("debug_fish")) {
				if (sender instanceof Player && sender.isOp()) {
					((Player) sender).getInventory().addItem(SlimefunItems.DEBUG_FISH);
				}
				else Messages.local.sendTranslation(sender, "messages.no-permission", true);
			}
			else if (args[0].equalsIgnoreCase("stats")) {
				if (args.length > 1) {
					if (sender.hasPermission("slimefun.stats.others") || sender instanceof ConsoleCommandSender) {
						if (Players.isOnline(args[1])) Research.sendStats(sender, Bukkit.getPlayer(args[1]));
						else Messages.local.sendTranslation(sender, "messages.not-online", true, new Variable("%player%", args[1]));
					}
					else Messages.local.sendTranslation(sender, "messages.no-permission", true);
				}
				else if (sender instanceof Player) Research.sendStats((Player) sender, (Player) sender);
				else Messages.local.sendTranslation(sender, "messages.only-players", true);
			}
			else if (args[0].equalsIgnoreCase("elevator")) {
				if (sender instanceof Player && args.length == 4) {
					double x = Integer.parseInt(args[1]) + 0.5D;
					double y = Integer.parseInt(args[2]) + 0.4D;
					double z = Integer.parseInt(args[3]) + 0.5D;
					
					if (BlockStorage.getLocationInfo(((Player) sender).getWorld().getBlockAt(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])).getLocation(), "floor") != null) {
						Elevator.ignored.add(((Player) sender).getUniqueId());
						float yaw = ((Player) sender).getEyeLocation().getYaw() + 180;
						if (yaw > 180) yaw = -180 + (yaw - 180);
						((Player) sender).teleport(new Location(((Player) sender).getWorld(), x, y, z, yaw, ((Player) sender).getEyeLocation().getPitch()));
						try {
							TitleBuilder title = (TitleBuilder) new TitleBuilder(20, 60, 20).addText("&r" + ChatColor.translateAlternateColorCodes('&', BlockStorage.getLocationInfo(((Player) sender).getWorld().getBlockAt(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])).getLocation(), "floor")));
							TitleBuilder subtitle = (TitleBuilder) new TitleBuilder(20, 60, 20).addText(" ");
							
							title.send(TitleType.TITLE, ((Player) sender));
							subtitle.send(TitleType.SUBTITLE, ((Player) sender));
						} catch (Exception x1) {
							x1.printStackTrace();
						}
					}
				}
			}
			else if (args[0].equalsIgnoreCase("timings")) {
				if (sender.hasPermission("slimefun.command.timings")|| sender instanceof ConsoleCommandSender) {
					SlimefunStartup.ticker.info(sender);
				}
				else Messages.local.sendTranslation(sender, "messages.no-permission", true);
			}
			else if (args[0].equalsIgnoreCase("versions")) {
				if (sender.hasPermission("slimefun.command.versions")|| sender instanceof ConsoleCommandSender) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + Bukkit.getName() + " &2" + ReflectionUtils.getVersion()));
					sender.sendMessage("");
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCS-CoreLib &2v" + CSCoreLib.getLib().getDescription().getVersion()));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSlimefun &2v" + plugin.getDescription().getVersion()));
					sender.sendMessage("");
					
					List<String> addons = new ArrayList<String>();
					
					for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
						if (plugin.getDescription().getDepend().contains("Slimefun") || plugin.getDescription().getSoftDepend().contains("Slimefun")) {
							if (Bukkit.getPluginManager().isPluginEnabled(plugin)) {
								addons.add(ChatColor.translateAlternateColorCodes('&', " &a" + plugin.getName() + " &2v" + plugin.getDescription().getVersion()));
							}
							else {
								addons.add(ChatColor.translateAlternateColorCodes('&', " &c" + plugin.getName() + " &4v" + plugin.getDescription().getVersion()));
							}
						}
					}
					
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Installed Addons &8(" + addons.size() + ")"));
					
					for (String addon : addons) {
						sender.sendMessage(addon);
					}
				}
				else {
					Messages.local.sendTranslation(sender, "messages.no-permission", true);
				}
			}
			else if (args[0].equalsIgnoreCase("give")) {
			    if (sender.hasPermission("slimefun.cheat.items") || !(sender instanceof Player)) {
			        if (args.length == 3) {
						if (Players.isOnline(args[1])) {
							if (Slimefun.listIDs().contains(args[2].toUpperCase())) {
								Messages.local.sendTranslation(Bukkit.getPlayer(args[1]), "messages.given-item", true, new Variable("%item%", SlimefunItem.getByID(args[2].toUpperCase()).getItem().getItemMeta().getDisplayName()), new Variable("%amount%", "1"));
								Bukkit.getPlayer(args[1]).getInventory().addItem(SlimefunItem.getByID(args[2].toUpperCase()).getItem());
								Messages.local.sendTranslation(sender, "messages.give-item", true, new Variable("%player%", args[1]), new Variable("%item%", SlimefunItem.getByID(args[2].toUpperCase()).getItem().getItemMeta().getDisplayName()), new Variable("%amount%", "1"));
							}
							else Messages.local.sendTranslation(sender, "messages.not-valid-item", true, new Variable("%item%", args[2]));
						}
						else Messages.local.sendTranslation(sender, "messages.not-online", true, new Variable("%player%", args[1]));
					}
			        else if (args.length == 4){
			            if (Players.isOnline(args[1])) {
                            if (Slimefun.listIDs().contains(args[2].toUpperCase())) {
                                 try {
                                     int amount = Integer.parseInt(args[3]);
                                     
                                     if (amount > 0) {
                                         Messages.local.sendTranslation(Bukkit.getPlayer(args[1]), "messages.given-item", true, new Variable("%item%", SlimefunItem.getByID(args[2].toUpperCase()).getItem().getItemMeta().getDisplayName()), new Variable("%amount%", String.valueOf(amount)));
                                         Bukkit.getPlayer(args[1]).getInventory().addItem(new CustomItem(SlimefunItem.getByID(args[2].toUpperCase()).getItem(), amount));
                                         Messages.local.sendTranslation(sender, "messages.give-item", true, new Variable("%player%", args[1]), new Variable("%item%", SlimefunItem.getByID(args[2].toUpperCase()).getItem().getItemMeta().getDisplayName()), new Variable("%amount%", String.valueOf(amount)));
                                     }
                                     else Messages.local.sendTranslation(sender, "messages.not-valid-amount", true, new Variable("%amount%", String.valueOf(amount)));
                                } catch (NumberFormatException e){
                                    Messages.local.sendTranslation(sender, "messages.not-valid-amount", true, new Variable("%amount%", args[3]));
                                }
                            }
                            else Messages.local.sendTranslation(sender, "messages.not-valid-item", true, new Variable("%item%", args[2]));
                        }
                        else Messages.local.sendTranslation(sender, "messages.not-online", true, new Variable("%player%", args[1]));
			        }
			        else Messages.local.sendTranslation(sender, "messages.usage", true, new Variable("%usage%", "/sf give <Player> <Slimefun Item> [Amount]"));
				}
				else  Messages.local.sendTranslation(sender, "messages.no-permission", true);
			}
			else if (args[0].equalsIgnoreCase("teleporter")) {
				if (args.length == 2) {
					if (sender.hasPermission("slimefun.command.teleporter") && sender instanceof Player) {
						OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
						if (player.getName() != null) {
							try {
								GPSNetwork.openTeleporterGUI((Player) sender, player.getUniqueId(), ((Player) sender).getLocation().getBlock().getRelative(BlockFace.DOWN), 999999999);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						else sender.sendMessage("&4Unknown Player: &c" + args[1]);
					}
					else Messages.local.sendTranslation(sender, "messages.no-permission", true);
				}
				else Messages.local.sendTranslation(sender, "messages.usage", true, new Variable("%usage%", "/sf teleporter <Player>"));
			}
			else if (args[0].equalsIgnoreCase("research")) {
				if (args.length == 3) {
					if (sender.hasPermission("slimefun.cheat.researches") || !(sender instanceof Player)) {
						if (Players.isOnline(args[1])) {
							Player p = Bukkit.getPlayer(args[1]);
							if (args[2].equalsIgnoreCase("all")) {
								for (Research res : Research.list()) {
									if (!res.hasUnlocked(p)) Messages.local.sendTranslation(sender, "messages.give-research", true, new Variable("%player%", p.getName()), new Variable("%research%", res.getName()));
									res.unlock(p, true);
								}
							}
							else if (args[2].equalsIgnoreCase("reset")) {
								for (Research res : Research.list()) {
									res.lock(p);
								}
								Messages.local.sendTranslation(p, "commands.research.reset", true, new Variable("%player%", args[1]));
							}
							else {
								Research research = null;
								for (Research res : Research.list()) {
									if (res.getName().toUpperCase().replace(" ", "_").equalsIgnoreCase(args[2])) {
										research = res;
										break;
									}
								}
								
								if (research != null) {
									research.unlock(p, true);
									Messages.local.sendTranslation(sender, "messages.give-research", true, new Variable("%player%", p.getName()), new Variable("%research%", research.getName()));
								}
								else {
									Messages.local.sendTranslation(sender, "messages.not-valid-research", true, new Variable("%research%", args[2]));
								}
							}
						}
						else {
							Messages.local.sendTranslation(sender, "messages.not-online", true, new Variable("%player%", args[1]));
						}
					}
					else Messages.local.sendTranslation(sender, "messages.no-permission", true);
				}
				else {
					Messages.local.sendTranslation(sender, "messages.usage", true, new Variable("%usage%", "/sf research <Player> <all/reset/Research>"));
				}
			}
			else {
				sendHelp(sender);
			}
		}
		return true;
	}

	private void sendHelp(CommandSender sender) {
		sender.sendMessage("");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSlimefun &2v" + plugin.getDescription().getVersion()));
		sender.sendMessage("");
		for (int i = 0; i < arguments.size(); i++) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3" + arguments.get(i) + " &b") + descriptions.get(i));
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().equalsIgnoreCase("/help slimefun")) {
			CommandHelp.sendCommandHelp(e.getPlayer(), plugin, arguments, descriptions);
			e.setCancelled(true);
		}
	}

}
