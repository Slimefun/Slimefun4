package me.mrCookieSlime.Slimefun.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.GPS.GPSNetwork;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.Messages;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Slimefun {
	
	public static Map<Integer, List<GuideHandler>> guide_handlers = new HashMap<Integer, List<GuideHandler>>();
	public static List<GuideHandler> guide_handlers2 = new ArrayList<GuideHandler>();
	
	private static GPSNetwork gps = new GPSNetwork();
	public static boolean emeraldenchants = false;
	public static List<Category> current_categories = new ArrayList<Category>();
	
	public static void registerGuideHandler(GuideHandler handler) {
		List<GuideHandler> handlers = new ArrayList<GuideHandler>();
		if (guide_handlers.containsKey(handler.getTier())) handlers = guide_handlers.get(handler.getTier());
		handlers.add(handler);
		guide_handlers.put(handler.getTier(), handlers);
		guide_handlers2.add(handler);
	}
	
	public static GPSNetwork getGPSNetwork() {
		return gps;
	}
	
	public static Object getItemValue(String id, String key) {
		return SlimefunStartup.getItemCfg().getValue(id + "." + key);
	}
	
	public static void setItemVariable(String id, String key, Object value) {
		SlimefunStartup.getItemCfg().setDefaultValue(id + "." + key, value);
	}
	
	public static Config getItemConfig() {
		return SlimefunStartup.getItemCfg();
	}

	public static void registerResearch(Research research, ItemStack... items) {
		for (ItemStack item: items) {
			research.addItems(SlimefunItem.getByItem(item));
		}
		research.register();
	}
	
	public static boolean hasUnlocked(Player p, ItemStack item, boolean message) {
		SlimefunItem sfItem = SlimefunItem.getByItem(item);
		if (sfItem == null) {
			if (SlimefunItem.isDisabled(item)) {
				if (message) Messages.local.sendTranslation(p, "messages.disabled-item", true);
				return false;
			}
			else return true;
		}
		else if (isEnabled(p, item, message) && hasPermission(p, sfItem, message)) {
			if (sfItem.getResearch() == null) return true;
			else if (sfItem.getResearch().hasUnlocked(p)) return true;
			else {
				if (message) Messages.local.sendTranslation(p, "messages.not-researched", true);
				return false;
			}
		}
		else return false;
	}
	
	public static boolean hasUnlocked(Player p, SlimefunItem sfItem, boolean message) {
		if (isEnabled(p, sfItem, message) && hasPermission(p, sfItem, message)) {
			if (sfItem.getResearch() == null) return true;
			else if (sfItem.getResearch().hasUnlocked(p)) return true;
			else {
				if (message) Messages.local.sendTranslation(p, "messages.not-researched", true);
				return false;
			}
		}
		return false;
	}
	
	public static boolean hasPermission(Player p, SlimefunItem item, boolean message) {
		if (item == null) return true;
		else if (SlimefunStartup.getItemCfg().getString(item.getName() + ".required-permission").equalsIgnoreCase("")) return true;
		else if (p.hasPermission(SlimefunStartup.getItemCfg().getString(item.getName() + ".required-permission"))) return true;
		else {
			if (message) Messages.local.sendTranslation(p, "messages.no-permission", true);
			return false;
		}
	}
	
	public static boolean isEnabled(Player p, ItemStack item, boolean message) {
		String world = p.getWorld().getName();
		SlimefunItem sfItem = SlimefunItem.getByItem(item);
		if (sfItem == null) return !SlimefunItem.isDisabled(item);
		if (SlimefunStartup.getWhitelist().contains(world + ".enabled")) {
			if (SlimefunStartup.getWhitelist().getBoolean(world + ".enabled")) {
				if (!SlimefunStartup.getWhitelist().contains(world + ".enabled-items." + sfItem.getName())) SlimefunStartup.getWhitelist().setDefaultValue(world + ".enabled-items." + sfItem.getName(), true);
				if (SlimefunStartup.getWhitelist().getBoolean(world + ".enabled-items." + sfItem.getName())) return true;
				else {
					if (message) Messages.local.sendTranslation(p, "messages.disabled-in-world", true);
					return false;
				}
			}
			else {
				if (message) Messages.local.sendTranslation(p, "messages.disabled-in-world", true);
				return false;
			}
		}
		else return true;
	}
	
	public static boolean isEnabled(Player p, SlimefunItem sfItem, boolean message) {
		String world = p.getWorld().getName();
		if (SlimefunStartup.getWhitelist().contains(world + ".enabled")) {
			if (SlimefunStartup.getWhitelist().getBoolean(world + ".enabled")) {
				if (!SlimefunStartup.getWhitelist().contains(world + ".enabled-items." + sfItem.getName())) SlimefunStartup.getWhitelist().setDefaultValue(world + ".enabled-items." + sfItem.getName(), true);
				if (SlimefunStartup.getWhitelist().getBoolean(world + ".enabled-items." + sfItem.getName())) return true;
				else {
					if (message) Messages.local.sendTranslation(p, "messages.disabled-in-world", true);
					return false;
				}
			}
			else {
				if (message) Messages.local.sendTranslation(p, "messages.disabled-in-world", true);
				return false;
			}
		}
		else return true;
	}

	public static List<String> listIDs() {
		List<String> ids = new ArrayList<String>();
		
		for (SlimefunItem item: SlimefunItem.list()) {
			ids.add(item.getName());
		}
		
		return ids;
	}

	public static List<ItemStack> listCategories() {
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (Category c: Category.list()) {
			items.add(c.getItem());
		}
		return items;
	}
	
	public static void addDescription(String id, String... description) {
		getItemConfig().setDefaultValue(id + ".description", Arrays.asList(description));
	}
	
	public static void addYoutubeVideo(String id, String link) {
		getItemConfig().setDefaultValue(id + ".youtube", link);
	}
	
	public static void addWikiPage(String id, String link) {
		getItemConfig().setDefaultValue(id + ".wiki", link);
	}
	
	public static void addOfficialWikiPage(String id, String page) {
		addWikiPage(id, "https://github.com/TheBusyBiscuit/Slimefun4/wiki/" + page);
	}
	
	public static boolean isEmeraldEnchantsInstalled() {
		return emeraldenchants;
	}

	public static List<GuideHandler> getGuideHandlers(int tier) {
		return guide_handlers.containsKey(tier) ? guide_handlers.get(tier): new ArrayList<GuideHandler>();
	}
}
