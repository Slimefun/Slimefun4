package me.mrCookieSlime.Slimefun.GPS;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatInput;
import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import io.github.thebusybiscuit.slimefun4.core.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.gps.GPSTransmitter;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class GPSNetwork {
	
	private static final String DIRECTORY = "data-storage/Slimefun/waypoints/";
	
	private final int[] border = {0, 1, 3, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
	private final int[] inventory = {19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
	
	private final int[] teleporterBorder = {0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
	private final int[] teleporterInventory = {19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
	
	private final Map<UUID, Set<Location>> transmitters = new HashMap<>();
	
	private final ItemStack deathpointIcon = SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFlMzg1NWY5NTJjZDRhMDNjMTQ4YTk0NmUzZjgxMmE1OTU1YWQzNWNiY2I1MjYyN2VhNGFjZDQ3ZDMwODEifX19");
	private final ItemStack netherIcon = SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDgzNTcxZmY1ODlmMWE1OWJiMDJiODA4MDBmYzczNjExNmUyN2MzZGNmOWVmZWJlZGU4Y2YxZmRkZSJ9fX0=");
	private final ItemStack endIcon = SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzZjYWM1OWIyYWFlNDg5YWEwNjg3YjVkODAyYjI1NTVlYjE0YTQwYmQ2MmIyMWViMTE2ZmE1NjljZGI3NTYifX19");
	private final ItemStack worldIcon = SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzljODg4MWU0MjkxNWE5ZDI5YmI2MWExNmZiMjZkMDU5OTEzMjA0ZDI2NWRmNWI0MzliM2Q3OTJhY2Q1NiJ9fX0=");
	
	public void updateTransmitter(Location l, UUID uuid, NetworkStatus status) {
		Set<Location> set = transmitters.getOrDefault(uuid, new HashSet<>());

		if (status == NetworkStatus.ONLINE) {
			if (set.add(l)) {
				transmitters.put(uuid, set);
			}
		}
		else {
			set.remove(l);
			transmitters.put(uuid, set);
		}
	}
	
	public int getNetworkComplexity(UUID uuid) {
		if (!transmitters.containsKey(uuid)) {
			return 0;
		}
		
		int level = 0;
		for (Location l : transmitters.get(uuid)) {
			SlimefunItem item = BlockStorage.check(l);
			
			if (item instanceof GPSTransmitter) {
				level += ((GPSTransmitter) item).getMultiplier(l.getBlockY());
			}
		}
		
		return level;
	}
	
	public int countTransmitters(UUID uuid) {
		if (!transmitters.containsKey(uuid)) return 0;
		else return transmitters.get(uuid).size();
	}
	
	public void openTransmitterControlPanel(Player p) {
		ChestMenu menu = new ChestMenu("&9Control Panel");
		
		for (int slot : border) {
			menu.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
		}
		
		menu.addItem(2, new CustomItem(SlimefunItems.GPS_TRANSMITTER, "&7Transmitter Overview &e(Selected)"));
		menu.addMenuClickHandler(2, ChestMenuUtils.getEmptyClickHandler());

		int complexity = getNetworkComplexity(p.getUniqueId());
		menu.addItem(4, new CustomItem(SlimefunItems.GPS_CONTROL_PANEL, "&7Network Info", "", "&8\u21E8 &7Status: " + (complexity > 0 ? "&2&lONLINE": "&4&lOFFLINE"), "&8\u21E8 &7Complexity: &r" + complexity));
		menu.addMenuClickHandler(4, ChestMenuUtils.getEmptyClickHandler());
		
		menu.addItem(6, new CustomItem(worldIcon, "&7Waypoint Overview &r(Select)"));
		menu.addMenuClickHandler(6, (pl, slot, item, action) -> {
			openWaypointControlPanel(pl);
			return false;
		});
		
		int index = 0;
		for (Location l : getTransmitters(p.getUniqueId())) {
			if (index >= inventory.length) break;

			SlimefunItem sfi = BlockStorage.check(l);
			if (sfi instanceof GPSTransmitter) {
				int slot = inventory[index];
				
				menu.addItem(slot, new CustomItem(SlimefunItems.GPS_TRANSMITTER, "&bGPS Transmitter", "&8\u21E8 &7World: &r" + l.getWorld().getName(), "&8\u21E8 &7X: &r" + l.getX(), "&8\u21E8 &7Y: &r" + l.getY(), "&8\u21E8 &7Z: &r" + l.getZ(), "", "&8\u21E8 &7Signal Strength: &r" + ((GPSTransmitter) sfi).getMultiplier(l.getBlockY()), "&8\u21E8 &7Ping: &r" + DoubleHandler.fixDouble(1000D / l.getY()) + "ms"));
				menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
				
				index++;
			}
		}
		
		menu.open(p);
	}
	
	public ItemStack getIcon(Map.Entry<String, Location> entry) {
		Location l = entry.getValue();
		
		if (entry.getKey().startsWith("&4Deathpoint")) {
			return deathpointIcon;
		}
		else if (l.getWorld().getEnvironment() == Environment.NETHER) {
			return netherIcon;
		}
		else if (l.getWorld().getEnvironment() == Environment.THE_END) {
			return endIcon;
		}
		else {
			return worldIcon;
		}
	}
	
	public void openWaypointControlPanel(Player p) {
		ChestMenu menu = new ChestMenu("&9Control Panel");
		
		for (int slot : border) {
			menu.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
		}
		
		menu.addItem(2, new CustomItem(SlimefunItems.GPS_TRANSMITTER, "&7Transmitter Overview &r(Select)"));
		menu.addMenuClickHandler(2, (pl, slot, item, action) -> {
			openTransmitterControlPanel(pl);
			return false;
		});

		int complexity = getNetworkComplexity(p.getUniqueId());
		menu.addItem(4, new CustomItem(SlimefunItems.GPS_CONTROL_PANEL, "&7Network Info", "", "&8\u21E8 &7Status: " + (complexity > 0 ? "&2&lONLINE": "&4&lOFFLINE"), "&8\u21E8 &7Complexity: &r" + complexity));
		menu.addMenuClickHandler(4, ChestMenuUtils.getEmptyClickHandler());
		
		menu.addItem(6, new CustomItem(worldIcon, "&7Waypoint Overview &e(Selected)"));
		menu.addMenuClickHandler(6, ChestMenuUtils.getEmptyClickHandler());
		
		int index = 0;
		for (Map.Entry<String, Location> entry : getWaypoints(p.getUniqueId()).entrySet()) {
			if (index >= inventory.length) break;
			int slot = inventory[index];
			
			Location l = entry.getValue();
			ItemStack globe = getIcon(entry);
			
			menu.addItem(slot, new CustomItem(globe, entry.getKey(), "&8\u21E8 &7World: &r" + l.getWorld().getName(), "&8\u21E8 &7X: &r" + l.getX(), "&8\u21E8 &7Y: &r" + l.getY(), "&8\u21E8 &7Z: &r" + l.getZ(), "", "&8\u21E8 &cClick to delete"));
			menu.addMenuClickHandler(slot, (pl, slotn, item, action) -> {
				String id = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', entry.getKey())).toUpperCase().replace(' ', '_');
				Config cfg = new Config(DIRECTORY + pl.getUniqueId().toString() + ".yml");
				cfg.setValue(id, null);
				cfg.save();
				pl.playSound(pl.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);

				openWaypointControlPanel(pl);
				return false;
			});
			
			index++;
		}
			
		menu.open(p);
	}

	public Map<String, Location> getWaypoints(UUID uuid) {
		Map<String, Location> map = new HashMap<>();
		Config cfg = new Config(DIRECTORY + uuid.toString() + ".yml");
		
		for (String key : cfg.getKeys()) {
			if (cfg.contains(key + ".world") && Bukkit.getWorld(cfg.getString(key + ".world")) != null) {
				map.put(cfg.getString(key + ".name"), cfg.getLocation(key));
			}
		}
		
		return map;
	}
	
	public void addWaypoint(Player p, final Location l) {
		if ((getWaypoints(p.getUniqueId()).size() + 2) > inventory.length) {
			SlimefunPlugin.getLocal().sendMessage(p, "gps.waypoint.max", true);
			return;
		}
		
		SlimefunPlugin.getLocal().sendMessage(p, "gps.waypoint.new", true);
		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 1F);
		
		ChatInput.waitForPlayer(SlimefunPlugin.instance, p, message ->
			addWaypoint(p, message, l)
		);
	}
	
	public void addWaypoint(Player p, String name, Location l) {
		if ((getWaypoints(p.getUniqueId()).size() + 2) > inventory.length) {
			SlimefunPlugin.getLocal().sendMessage(p, "gps.waypoint.max", true);
			return;
		}
		
		Config cfg = new Config(DIRECTORY + p.getUniqueId().toString() + ".yml");
		String id = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', name)).toUpperCase().replace(' ', '_');
		
		cfg.setValue(id, l);
		cfg.setValue(id + ".name", name);
		cfg.save();
		
		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 1F);
		SlimefunPlugin.getLocal().sendMessage(p, "gps.waypoint.added", true);
	}

	public Set<Location> getTransmitters(UUID uuid) {
		return transmitters.getOrDefault(uuid, new HashSet<>());
	}
	
	public void openTeleporterGUI(Player p, UUID uuid, Block b, final int complexity) {
		if (SlimefunPlugin.getUtilities().teleporterUsers.contains(p.getUniqueId())) {
			return;
		}
		
		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
		SlimefunPlugin.getUtilities().teleporterUsers.add(p.getUniqueId());
		
		ChestMenu menu = new ChestMenu("&3Teleporter");
		menu.addMenuCloseHandler(pl -> SlimefunPlugin.getUtilities().teleporterUsers.remove(pl.getUniqueId()));
		
		for (int slot : teleporterBorder) {
			menu.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
		}
		
		menu.addItem(4, new CustomItem(worldIcon, "&7Waypoint Overview &e(Select a Destination)"));
		menu.addMenuClickHandler(4, ChestMenuUtils.getEmptyClickHandler());
		
		Location source = new Location(b.getWorld(), b.getX() + 0.5D, b.getY() + 2D, b.getZ() + 0.5D);
		int index = 0;
		
		for (Map.Entry<String, Location> entry : Slimefun.getGPSNetwork().getWaypoints(uuid).entrySet()) {
			if (index >= teleporterInventory.length) break;
			int slot = teleporterInventory[index];
			
			Location l = entry.getValue();
			ItemStack globe = getIcon(entry);
			
			menu.addItem(slot, new CustomItem(globe, entry.getKey(), "&8\u21E8 &7World: &r" + l.getWorld().getName(), "&8\u21E8 &7X: &r" + l.getX(), "&8\u21E8 &7Y: &r" + l.getY(), "&8\u21E8 &7Z: &r" + l.getZ(), "&8\u21E8 &7Estimated Teleportation Time: &r" + (50 / TeleportationSequence.getSpeed(Slimefun.getGPSNetwork().getNetworkComplexity(uuid), source, l)) + "s", "", "&8\u21E8 &cClick to select"));
			menu.addMenuClickHandler(slot, (pl, slotn, item, action) -> {
					pl.closeInventory();
					TeleportationSequence.start(pl.getUniqueId(), complexity, source, l, false);
					return false;
			});
			
			index++;
		}
		
		menu.open(p);
	}

}
