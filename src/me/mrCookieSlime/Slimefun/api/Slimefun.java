package me.mrCookieSlime.Slimefun.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.GPS.GPSNetwork;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem.State;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.VanillaItem;
import me.mrCookieSlime.Slimefun.Setup.Messages;

/**
 * Provides a few convenience methods.
 *
 * @since 4.0
 */
public class Slimefun {

	public static Map<Integer, List<GuideHandler>> guide_handlers = new HashMap<Integer, List<GuideHandler>>();
	public static List<GuideHandler> guide_handlers2 = new ArrayList<GuideHandler>();

	/**
	 * Instance of the GPSNetwork.
	 */
	private static GPSNetwork gps = new GPSNetwork();
	/**
	 * Whether EmeraldEnchants is enabled or not.
	 */
	public static boolean emeraldenchants = false;
    /**
     * Lists all the registered categories.
     */
	public static List<Category> current_categories = new ArrayList<Category>();

	public static void registerGuideHandler(GuideHandler handler) {
		List<GuideHandler> handlers = new ArrayList<GuideHandler>();
		if (guide_handlers.containsKey(handler.getTier())) handlers = guide_handlers.get(handler.getTier());
		handlers.add(handler);
		guide_handlers.put(handler.getTier(), handlers);
		guide_handlers2.add(handler);
	}

	/**
	 * Returns the GPSNetwork instance.
	 *
	 * @return the GPSNetwork instance.
	 */
	public static GPSNetwork getGPSNetwork() {
		return gps;
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
		return getItemConfig().getValue(id + "." + key);
	}

	/**
	 * Sets a default value associated to this key for the SlimefunItem corresponding to this id.
	 *
	 * @param  id     the id of the SlimefunItem, not null
	 * @param  key    the key of the value to set, not null
	 * @param  value  the value to set, can be null
	 */
	public static void setItemVariable(String id, String key, Object value) {
		getItemConfig().setDefaultValue(id + "." + key, value);
	}

	/**
	 * Returns the Config instance of Items.yml file.
	 * <p>
	 * It calls {@code SlimefunStartup#getItemCfg()}.
	 *
	 * @return the Items.yml Config instance.
	 */
	public static Config getItemConfig() {
		return SlimefunStartup.getItemCfg();
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
		for (ItemStack item: items) {
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
		State state = SlimefunItem.getState(item);
		if (sfItem == null) {
			if (state != State.ENABLED) {
				if (message && state != State.VANILLA) Messages.local.sendTranslation(p, "messages.disabled-item", true);
				return false;
			}
			else return true;
		}
		else if (isEnabled(p, item, message) && hasPermission(p, sfItem, message)) {
			if (sfItem.getResearch() == null) return true;
			else if (sfItem.getResearch().hasUnlocked(p)) return true;
			else {
				if (message && !(sfItem instanceof VanillaItem)) Messages.local.sendTranslation(p, "messages.not-researched", true);
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
			else if (sfItem.getResearch().hasUnlocked(p)) return true;
			else {
				if (message && !(sfItem instanceof VanillaItem)) Messages.local.sendTranslation(p, "messages.not-researched", true);
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
			if (message) Messages.local.sendTranslation(p, "messages.no-permission", true);
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
		String world = p.getWorld().getName();
		SlimefunItem sfItem = SlimefunItem.getByItem(item);
		if (sfItem == null) return !SlimefunItem.isDisabled(item);
		if (SlimefunStartup.getWhitelist().contains(world + ".enabled")) {
			if (SlimefunStartup.getWhitelist().getBoolean(world + ".enabled")) {
				if (!SlimefunStartup.getWhitelist().contains(world + ".enabled-items." + sfItem.getID())) SlimefunStartup.getWhitelist().setDefaultValue(world + ".enabled-items." + sfItem.getID(), true);
				if (SlimefunStartup.getWhitelist().getBoolean(world + ".enabled-items." + sfItem.getID())) return true;
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
		String world = p.getWorld().getName();
		if (SlimefunStartup.getWhitelist().contains(world + ".enabled")) {
			if (SlimefunStartup.getWhitelist().getBoolean(world + ".enabled")) {
				if (!SlimefunStartup.getWhitelist().contains(world + ".enabled-items." + sfItem.getID())) SlimefunStartup.getWhitelist().setDefaultValue(world + ".enabled-items." + sfItem.getID(), true);
				if (SlimefunStartup.getWhitelist().getBoolean(world + ".enabled-items." + sfItem.getID())) return true;
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

	/**
	 * Lists all the IDs of the enabled items.
	 *
	 * @return the list of all the IDs of the enabled items.
	 */
	public static List<String> listIDs() {
		List<String> ids = new ArrayList<String>();
		for (SlimefunItem item: SlimefunItem.list()) {
			ids.add(item.getID());
		}
		return ids;
	}

    /**
     * Returns a list of all the ItemStacks representing the registered categories.
     *
     * @return the list of the display items of all the registered categories.
     * @see #current_categories
     */
	public static List<ItemStack> listCategories() {
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (Category c: Category.list()) {
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

	/**
	 * Returns whether EmeraldEnchants is enabled or not.
	 * <p>
	 * It can be directly accessed by {@link #emeraldenchants}.
	 *
	 * @return <code>true</code> if EmeraldEnchants is enabled,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isEmeraldEnchantsInstalled() {
		return emeraldenchants;
	}

	public static List<GuideHandler> getGuideHandlers(int tier) {
		return guide_handlers.containsKey(tier) ? guide_handlers.get(tier): new ArrayList<GuideHandler>();
	}
}