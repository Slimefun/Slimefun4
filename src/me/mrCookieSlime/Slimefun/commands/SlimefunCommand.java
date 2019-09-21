package me.mrCookieSlime.Slimefun.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.CommandHelp;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Player.Players;
import me.mrCookieSlime.CSCoreLibPlugin.general.Reflection.ReflectionUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.TitleBuilder;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.TitleBuilder.TitleType;
import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.GPS.GPSNetwork;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunGuideLayout;

public class SlimefunCommand implements CommandExecutor, Listener {

	private final SlimefunPlugin plugin;

	private final List<String> arguments = new ArrayList<>();
	private final List<String> descriptions = new ArrayList<>();
	private final List<String> tabs = new ArrayList<>();

	public SlimefunCommand(SlimefunPlugin plugin) {
		this.plugin = plugin;
		
		arguments.add("/sf help");
		tabs.add("help");
		descriptions.add(SlimefunPlugin.getLocal().getMessage("commands.help"));
		
		arguments.add("/sf versions");
		tabs.add("versions");
		descriptions.add(SlimefunPlugin.getLocal().getMessage("commands.versions"));
		
		arguments.add("/sf cheat");
		tabs.add("cheat");
		descriptions.add(SlimefunPlugin.getLocal().getMessage("commands.cheat"));
		
		arguments.add("/sf give");
		tabs.add("give");
		descriptions.add(SlimefunPlugin.getLocal().getMessage("commands.give"));
		
		arguments.add("/sf research");
		tabs.add("research");
		descriptions.add(SlimefunPlugin.getLocal().getMessage("commands.research.desc"));
		
		arguments.add("/sf guide");
		tabs.add("guide");
		descriptions.add(SlimefunPlugin.getLocal().getMessage("commands.guide"));
		
		arguments.add("/sf stats");
		tabs.add("stats");
		descriptions.add(SlimefunPlugin.getLocal().getMessage("commands.stats"));
		
		arguments.add("/sf timings");
		tabs.add("timings");
		descriptions.add(SlimefunPlugin.getLocal().getMessage("commands.timings"));
		
		arguments.add("/sf teleporter");
		tabs.add("teleporter");
		descriptions.add(SlimefunPlugin.getLocal().getMessage("commands.teleporter"));
		
		arguments.add("/sf open_guide");
		tabs.add("open_guide");
		descriptions.add(SlimefunPlugin.getLocal().getMessage("commands.open_guide"));
		
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
					else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
				}
				else SlimefunPlugin.getLocal().sendMessage(sender, "messages.only-players", true);
			}
			else if (args[0].equalsIgnoreCase("guide")) {
				if (sender instanceof Player) {
					if (sender.hasPermission("slimefun.command.guide")) ((Player) sender).getInventory().addItem(SlimefunGuide.getItem(SlimefunPlugin.getCfg().getBoolean("guide.default-view-book") ? SlimefunGuideLayout.BOOK : SlimefunGuideLayout.CHEST));
					else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
				}
				else SlimefunPlugin.getLocal().sendMessage(sender, "messages.only-players", true);
			}
			else if (args[0].equalsIgnoreCase("open_guide")) {
				if (sender instanceof Player) { 
					if (sender.hasPermission("slimefun.command.open_guide")) SlimefunGuide.openGuide((Player) sender, SlimefunPlugin.getCfg().getBoolean("guide.default-view-book"));
					else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
				}
				else SlimefunPlugin.getLocal().sendMessage(sender, "messages.only-players", true);
			}
			else if (args[0].equalsIgnoreCase("debug_fish")) {
				if (sender instanceof Player && sender.isOp()) {
					((Player) sender).getInventory().addItem(SlimefunItems.DEBUG_FISH);
				}
				else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
			}
			else if (args[0].equalsIgnoreCase("stats")) {
				if (args.length > 1) {
					if (sender.hasPermission("slimefun.stats.others") || sender instanceof ConsoleCommandSender) {
						if (Players.isOnline(args[1])) {
							Research.sendStats(sender, Bukkit.getPlayer(args[1]));
						}
						else {
							SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-online", true, msg -> msg.replace("%player%", args[1]));
						}
					}
					else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
				}
				else if (sender instanceof Player) {
					Research.sendStats((Player) sender, (Player) sender);
				}
				else {
					SlimefunPlugin.getLocal().sendMessage(sender, "messages.only-players", true);
				}
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
			}
			else if (args[0].equalsIgnoreCase("timings")) {
				if (sender.hasPermission("slimefun.command.timings")|| sender instanceof ConsoleCommandSender) {
					SlimefunPlugin.getTicker().info(sender);
				}
				else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
			}
			else if (args[0].equalsIgnoreCase("versions")) {
				if (sender.hasPermission("slimefun.command.versions")|| sender instanceof ConsoleCommandSender) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + Bukkit.getName() + " &2" + ReflectionUtils.getVersion()));
					sender.sendMessage("");
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCS-CoreLib &2v" + CSCoreLib.getLib().getDescription().getVersion()));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSlimefun &2v" + plugin.getDescription().getVersion()));
					sender.sendMessage("");
					
					List<String> addons = new ArrayList<>();
					
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
					SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
				}
			}
			else if (args[0].equalsIgnoreCase("give")) {
			    if (sender.hasPermission("slimefun.cheat.items") || !(sender instanceof Player)) {
			        if (args.length == 3) {
						if (Players.isOnline(args[1])) {
							if (Slimefun.listIDs().contains(args[2].toUpperCase())) {
								Player p = Bukkit.getPlayer(args[1]);
								SlimefunPlugin.getLocal().sendMessage(p, "messages.given-item", true, msg -> msg.replace("%item%", SlimefunItem.getByID(args[2].toUpperCase()).getItem().getItemMeta().getDisplayName()).replace("%amount%", "1"));
								p.getInventory().addItem(SlimefunItem.getByID(args[2].toUpperCase()).getItem());
								SlimefunPlugin.getLocal().sendMessage(sender, "messages.give-item", true, msg -> msg.replace("%player%", args[1]).replace("%item%", SlimefunItem.getByID(args[2].toUpperCase()).getItem().getItemMeta().getDisplayName()).replace("%amount%", "1"));
							}
							else SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-item", true, msg -> msg.replace("%item%", args[2]));
						}
						else SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-online", true, msg -> msg.replace("%player%", args[1]));
					}
			        else if (args.length == 4){
			            if (Players.isOnline(args[1])) {
                            if (Slimefun.listIDs().contains(args[2].toUpperCase())) {
                                 try {
                                     int amount = Integer.parseInt(args[3]);
                                     
                                     if (amount > 0) {
                                         SlimefunPlugin.getLocal().sendMessage(Bukkit.getPlayer(args[1]), "messages.given-item", true, msg -> msg.replace("%item%", SlimefunItem.getByID(args[2].toUpperCase()).getItem().getItemMeta().getDisplayName()).replace("%amount%", String.valueOf(amount)));
                                         Bukkit.getPlayer(args[1]).getInventory().addItem(new CustomItem(SlimefunItem.getByID(args[2].toUpperCase()).getItem(), amount));
                                         SlimefunPlugin.getLocal().sendMessage(sender, "messages.give-item", true, msg -> msg.replace("%player%", args[1]).replace("%item%", SlimefunItem.getByID(args[2].toUpperCase()).getItem().getItemMeta().getDisplayName()).replace("%amount%", String.valueOf(amount)));
                                     }
                                     else SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-amount", true, msg -> msg.replace("%amount%", String.valueOf(amount)));
                                } catch (NumberFormatException e){
                                    SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-amount", true, msg -> msg.replace("%amount%", args[3]));
                                }
                            }
                            else SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-item", true, msg -> msg.replace("%item%", args[2]));
                        }
                        else SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-online", true, msg -> msg.replace("%player%", args[1]));
			        }
			        else SlimefunPlugin.getLocal().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf give <Player> <Slimefun Item> [Amount]"));
				}
				else  SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
			}
			else if (args[0].equalsIgnoreCase("teleporter")) {
				if (args.length == 2) {
					if (sender.hasPermission("slimefun.command.teleporter") && sender instanceof Player) {
						OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
						if (player.getName() != null) {
							GPSNetwork.openTeleporterGUI((Player) sender, player.getUniqueId(), ((Player) sender).getLocation().getBlock().getRelative(BlockFace.DOWN), 999999999);
						}
						else sender.sendMessage("&4Unknown Player: &c" + args[1]);
					}
					else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
				}
				else SlimefunPlugin.getLocal().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf teleporter <Player>"));
			}
			else if (args[0].equalsIgnoreCase("research")) {
				if (args.length == 3) {
					if (sender.hasPermission("slimefun.cheat.researches") || !(sender instanceof Player)) {
						if (Players.isOnline(args[1])) {
							Player p = Bukkit.getPlayer(args[1]);
							PlayerProfile profile = PlayerProfile.fromUUID(p.getUniqueId());
							
							if (args[2].equalsIgnoreCase("all")) {
								for (Research res : Research.list()) {
									if (!profile.hasUnlocked(res)) SlimefunPlugin.getLocal().sendMessage(sender, "messages.give-research", true, msg -> msg.replace("%player%", p.getName()).replace("%research%", res.getName()));
									res.unlock(p, true);
								}
							}
							else if (args[2].equalsIgnoreCase("reset")) {
								for (Research res : Research.list()) {
									profile.setResearched(res, false);
								}
								SlimefunPlugin.getLocal().sendMessage(p, "commands.research.reset", true, msg -> msg.replace("%player%", args[1]));
							}
							else {
								Research research = null;
								for (Research res : Research.list()) {
									if (res.getName().toUpperCase().replace(' ', '_').equalsIgnoreCase(args[2])) {
										research = res;
										break;
									}
								}
								
								if (research != null) {
									research.unlock(p, true);
									final Research r = research;
									SlimefunPlugin.getLocal().sendMessage(sender, "messages.give-research", true, msg -> msg.replace("%player%", p.getName()).replace("%research%", r.getName()));
								}
								else {
									SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-research", true, msg -> msg.replace("%research%", args[2]));
								}
							}
						}
						else {
							SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-online", true, msg -> msg.replace("%player%", args[1]));
						}
					}
					else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
				}
				else {
					SlimefunPlugin.getLocal().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf research <Player> <all/reset/Research>"));
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

	public List<String> getTabArguments() {
		return tabs;
	}

}
