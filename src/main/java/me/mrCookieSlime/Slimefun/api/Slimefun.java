package me.mrCookieSlime.Slimefun.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.core.services.LocalizationService;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.GPS.GPSNetwork;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ItemState;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.VanillaItem;

/**
 * Provides a few convenience methods.
 *
 * @since 4.0
 */
public final class Slimefun {

	private Slimefun() {}
	
	@Deprecated
	public static void registerGuideHandler(GuideHandler handler) {
		List<GuideHandler> handlers = SlimefunPlugin.getUtilities().guideHandlers.getOrDefault(handler.getTier(), new ArrayList<>());
		handlers.add(handler);
		SlimefunPlugin.getUtilities().guideHandlers.put(handler.getTier(), handlers);
	}

	/**
	 * Returns the GPSNetwork instance.
	 *
	 * @return the GPSNetwork instance.
	 */
	public static GPSNetwork getGPSNetwork() {
		return SlimefunPlugin.instance.getGPS();
	}
	
	public static Logger getLogger() {
		return SlimefunPlugin.instance.getLogger();
	}

	/**
	 * Returns the value associated to this key for the SlimefunItem corresponding to this id.
	 *
	 * @param  id   the id of the SlimefunItem, not null
	 * @param  key  the key of the value to get, not null
	 *
	 * @return the value associated to the key for the SlimefunItem corresponding to the id,
	 *         or null if it doesn't exist.
	 */
	public static Object getItemValue(String id, String key) {
		return getItemConfig().getValue(id + '.' + key);
	}

	/**
	 * Sets a default value associated to this key for the SlimefunItem corresponding to this id.
	 *
	 * @param  id     the id of the SlimefunItem, not null
	 * @param  key    the key of the value to set, not null
	 * @param  value  the value to set, can be null
	 */
	public static void setItemVariable(String id, String key, Object value) {
		getItemConfig().setDefaultValue(id + '.' + key, value);
	}

	/**
	 * Returns the Config instance of Items.yml file.
	 * <p>
	 * It calls {@code SlimefunStartup#getItemCfg()}.
	 *
	 * @return the Items.yml Config instance.
	 */
	public static Config getItemConfig() {
		return SlimefunPlugin.getItemCfg();
	}

	/**
	 * Registers this Research and automatically binds these ItemStacks to it.
	 * <p>
	 * This convenience method spares from doing the code below:
	 * <pre>
	 *     {@code
	 *		Research r = new Research(7, "Glowstone Armor", 3);
	 *		r.addItems(SlimefunItem.getByItem(SlimefunItems.GLOWSTONE_HELMET),
	 *		           SlimefunItem.getByItem(SlimefunItems.GLOWSTONE_CHESTPLATE),
	 *		           SlimefunItem.getByItem(SlimefunItems.GLOWSTONE_LEGGINGS),
	 *		           SlimefunItem.getByItem(SlimefunItems.GLOWSTONE_BOOTS));
	 *		r.register();
	 *     }*
	 * </pre>

	 * @param  research  the research to register, not null
	 * @param  items     the items to bind, not null
	 */
	public static void registerResearch(Research research, ItemStack... items) {
		for (ItemStack item : items) {
			research.addItems(SlimefunItem.getByItem(item));
		}
		
		research.register();
	}

	/**
	 * Checks if this player can use this item.
	 *
	 * @param  p        the player to check, not null
	 * @param  item     the item to check, not null
	 * @param  message  whether a message should be sent to the player or not
	 *
	 * @return <code>true</code> if the item is a SlimefunItem, enabled, researched and if the player has the permission to use it,
	 *         <code>false</code> otherwise.
	 */
	public static boolean hasUnlocked(Player p, ItemStack item, boolean message) {
		SlimefunItem sfItem = SlimefunItem.getByItem(item);
		ItemState state = SlimefunItem.getState(item);

		if (sfItem == null) {
			if (state != ItemState.ENABLED) {
				if (message && state != ItemState.VANILLA) {
					SlimefunPlugin.getLocal().sendMessage(p, "messages.disabled-item", true);
				}
				
				return false;
			}
			else return true;
		}
		else if (isEnabled(p, item, message) && hasPermission(p, sfItem, message)) {
			if (sfItem.getResearch() == null) return true;
			else if (PlayerProfile.get(p).hasUnlocked(sfItem.getResearch())) return true;
			else {
				if (message && !(sfItem instanceof VanillaItem)) {
					SlimefunPlugin.getLocal().sendMessage(p, "messages.not-researched", true);
				}
				
				return false;
			}
		}
		else return false;
	}

	/**
	 * Checks if this player can use this item.
	 *
	 * @param  p        the player to check, not null
	 * @param  sfItem   the item to check, not null
	 * @param  message  whether a message should be sent to the player or not
	 *
	 * @return <code>true</code> if the item is enabled, researched and the player has the permission to use it,
	 *         <code>false</code> otherwise.
	 */
	public static boolean hasUnlocked(Player p, SlimefunItem sfItem, boolean message) {
		if (isEnabled(p, sfItem, message) && hasPermission(p, sfItem, message)) {
			if (sfItem.getResearch() == null) return true;
			else if (PlayerProfile.get(p).hasUnlocked(sfItem.getResearch())) return true;
			else {
				if (message && !(sfItem instanceof VanillaItem)) SlimefunPlugin.getLocal().sendMessage(p, "messages.not-researched", true);
				return false;
			}
		}
		return false;
	}

	/**
	 * Checks if this player has the permission to use this item.
	 *
	 * @param  p        the player to check, not null
	 * @param  item     the item to check, null returns <code>true</code>
	 * @param  message  whether a message should be sent to the player or not
	 *
	 * @return <code>true</code> if the item is not null and if the player has the permission to use it,
	 *         <code>false</code> otherwise.
	 */
	public static boolean hasPermission(Player p, SlimefunItem item, boolean message) {
		if (item == null) return true;
		else if (item.getPermission().equalsIgnoreCase("")) return true;
		else if (p.hasPermission(item.getPermission())) return true;
		else {
			if (message) SlimefunPlugin.getLocal().sendMessage(p, "messages.no-permission", true);
			return false;
		}
	}

	/**
	 * Checks if this item is enabled in the world this player is in.
	 *
	 * @param  p        the player to get the world he is in, not null
	 * @param  item     the item to check, not null
	 * @param  message  whether a message should be sent to the player or not
	 *
	 * @return <code>true</code> if the item is a SlimefunItem and is enabled in the world the player is in,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isEnabled(Player p, ItemStack item, boolean message) {
		SlimefunItem sfItem = SlimefunItem.getByItem(item);
		if (sfItem == null) return !SlimefunItem.isDisabled(item);
		else return isEnabled(p, sfItem, message);
	}

	/**
	 * Checks if this item is enabled in the world this player is in.
	 *
	 * @param  p        the player to get the world he is in, not null
	 * @param  sfItem   the item to check, not null
	 * @param  message  whether a message should be sent to the player or not
	 *
	 * @return <code>true</code> if the item is enabled in the world the player is in,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isEnabled(Player p, SlimefunItem sfItem, boolean message) {
		if (sfItem.isDisabled()) {
			if (message) SlimefunPlugin.getLocal().sendMessage(p, "messages.disabled-in-world", true);
			return false;
		}
		
		String world = p.getWorld().getName();
		if (SlimefunPlugin.getWhitelist().contains(world + ".enabled")) {
			if (SlimefunPlugin.getWhitelist().getBoolean(world + ".enabled")) {
				if (!SlimefunPlugin.getWhitelist().contains(world + ".enabled-items." + sfItem.getID())) SlimefunPlugin.getWhitelist().setDefaultValue(world + ".enabled-items." + sfItem.getID(), true);
				if (SlimefunPlugin.getWhitelist().getBoolean(world + ".enabled-items." + sfItem.getID())) return true;
				else {
					if (message) SlimefunPlugin.getLocal().sendMessage(p, "messages.disabled-in-world", true);
					return false;
				}
			}
			else {
				if (message) SlimefunPlugin.getLocal().sendMessage(p, "messages.disabled-in-world", true);
				return false;
			}
		}
		else return true;
	}

	/**
	 * Lists all the IDs of the enabled items.
	 *
	 * @return the list of all the IDs of the enabled items.
	 */
	public static List<String> listIDs() {
		List<String> ids = new ArrayList<>();
		for (SlimefunItem item : SlimefunItem.list()) {
			ids.add(item.getID());
		}
		return ids;
	}

	/**
	 * Returns a list of all the ItemStacks representing the registered categories.
	 *
	 * @return the list of the display items of all the registered categories.
	 * @see #currentCategories
	 */
	public static List<ItemStack> listCategories() {
		List<ItemStack> items = new ArrayList<>();
		for (Category c : Category.list()) {
			items.add(c.getItem());
		}
		return items;
	}

	/**
	 * Binds this description to the SlimefunItem corresponding to this id.
	 *
	 * @param  id           the id of the SlimefunItem, not null
	 * @param  description  the description, not null
	 *
	 * @deprecated As of 4.1.10, renamed to {@link #addHint(String, String...)} for better name convenience.
	 */
	@Deprecated
	public static void addDescription(String id, String... description) {
		getItemConfig().setDefaultValue(id + ".description", Arrays.asList(description));
	}

	/**
	 * Binds this hint to the SlimefunItem corresponding to this id.
	 *
	 * @param  id    the id of the SlimefunItem, not null
	 * @param  hint  the hint, not null
	 *
	 * @since 4.1.10, rename of {@link #addDescription(String, String...)}.
	 */
	public static void addHint(String id, String... hint) {
		getItemConfig().setDefaultValue(id + ".hint", Arrays.asList(hint));
	}

	/**
	 * Binds this YouTube link to the SlimefunItem corresponding to this id.
	 *
	 * @param  id    the id of the SlimefunItem, not null
	 * @param  link  the link of the YouTube video, not null
	 */
	public static void addYoutubeVideo(String id, String link) {
		getItemConfig().setDefaultValue(id + ".youtube", link);
	}

	/**
	 * Binds this link as a Wiki page to the SlimefunItem corresponding to this id.
	 *
	 * @param  id    the id of the SlimefunItem, not null
	 * @param  link  the link of the Wiki page, not null
	 */
	public static void addWikiPage(String id, String link) {
		getItemConfig().setDefaultValue(id + ".wiki", link);
	}

	/**
	 * Convenience method to simplify binding an official Wiki page to the SlimefunItem corresponding to this id.
	 *
	 * @param  id    the id of the SlimefunItem, not null
	 * @param  page  the ending of the link corresponding to the page, not null
	 */
	public static void addOfficialWikiPage(String id, String page) {
		addWikiPage(id, "https://github.com/TheBusyBiscuit/Slimefun4/wiki/" + page);
	}

	public static List<GuideHandler> getGuideHandlers(int tier) {
		return SlimefunPlugin.getUtilities().guideHandlers.getOrDefault(tier, new ArrayList<>());
	}

	public static String getVersion() {
		return SlimefunPlugin.instance.getDescription().getVersion();
	}
	
	public static LocalizationService getLocal() {
		return SlimefunPlugin.getLocal();
	}

	public static BukkitTask runSync(Runnable r) {
		return Bukkit.getScheduler().runTask(SlimefunPlugin.instance, r);
	}

	public static BukkitTask runSync(Runnable r, long delay) {
		return Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance, r, delay);
	}

	public static Set<Plugin> getInstalledAddons() {
		return Arrays.stream(SlimefunPlugin.instance.getServer().getPluginManager().getPlugins())
				.filter(Plugin::isEnabled)
				.filter(plugin -> plugin.getDescription().getDepend().contains("Slimefun") || plugin.getDescription().getSoftDepend().contains("Slimefun"))
				.collect(Collectors.toSet());
	}
}