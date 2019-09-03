package me.mrCookieSlime.Slimefun.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.EmeraldEnchants.EmeraldEnchants;
import me.mrCookieSlime.EmeraldEnchants.ItemEnchantment;
import me.mrCookieSlime.Slimefun.GPS.GPSNetwork;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem.State;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SoulboundItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.VanillaItem;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides a few convenience methods.
 *
 * @since 4.0
 */
public final class Slimefun {

	private Slimefun() {}
	
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
		return SlimefunPlugin.instance.gps;
	}
	
	public static Logger getLogger() {
		return SlimefunPlugin.instance.getLogger();
	}

	/**
	 * Returns the value associated to this key for the SlimefunItem corresponding to this id.
	 *
	 * @param  id   the id of the SlimefunItem
	 * @param  key  the key of the value to get
	 *
	 * @return the value associated to the key for the SlimefunItem corresponding to the id,
	 *         or null if it doesn't exist.
	 */
	@Nullable public static Object getItemValue(@Nonnull String id, @Nonnull String key) {
		return getItemConfig().getValue(id + "." + key);
	}

	/**
	 * Sets a default value associated to this key for the SlimefunItem corresponding to this id.
	 *
	 * @param  id     the id of the SlimefunItem
	 * @param  key    the key of the value to set
	 * @param  value  the value to set
	 */
	public static void setItemVariable(@Nonnull String id, @Nonnull String key, @Nullable Object value) {
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

	 * @param  research  the research to register
	 * @param  items     the items to bind
	 */
	public static void registerResearch(@Nonnull Research research, @Nonnull ItemStack... items) {
		for (ItemStack item: items) {
			research.addItems(SlimefunItem.getByItem(item));
		}
		research.register();
	}

	/**
	 * Checks if this player can use this item.
	 *
	 * @param  p        the player to check
	 * @param  item     the item to check
	 * @param  message  whether a message should be sent to the player or not
	 *
	 * @return <code>true</code> if the item is a SlimefunItem, enabled, researched and if the player has the permission to use it,
	 *         <code>false</code> otherwise.
	 */
	public static boolean hasUnlocked(@Nonnull Player p, @Nonnull ItemStack item, boolean message) {
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
			else if (PlayerProfile.fromUUID(p.getUniqueId()).hasUnlocked(sfItem.getResearch())) return true;
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
	 * @param  p        the player to check
	 * @param  sfItem   the item to check
	 * @param  message  whether a message should be sent to the player or not
	 *
	 * @return <code>true</code> if the item is enabled, researched and the player has the permission to use it,
	 *         <code>false</code> otherwise.
	 */
	public static boolean hasUnlocked(@Nonnull Player p, @Nonnull SlimefunItem sfItem, boolean message) {
		if (isEnabled(p, sfItem, message) && hasPermission(p, sfItem, message)) {
			if (sfItem.getResearch() == null) return true;
			else if (PlayerProfile.fromUUID(p.getUniqueId()).hasUnlocked(sfItem.getResearch())) return true;
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
	 * @param  p        the player to check
	 * @param  item     the item to check, null returns <code>true</code>
	 * @param  message  whether a message should be sent to the player or not
	 *
	 * @return <code>true</code> if the item is not null and if the player has the permission to use it,
	 *         <code>false</code> otherwise.
	 */
	public static boolean hasPermission(@Nonnull Player p, @Nullable SlimefunItem item, boolean message) {
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
	 * @param  p        the player to get the world he is in
	 * @param  item     the item to check
	 * @param  message  whether a message should be sent to the player or not
	 *
	 * @return <code>true</code> if the item is a SlimefunItem and is enabled in the world the player is in,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isEnabled(@Nonnull Player p, @Nonnull ItemStack item, boolean message) {
		String world = p.getWorld().getName();
		SlimefunItem sfItem = SlimefunItem.getByItem(item);
		if (sfItem == null) return !SlimefunItem.isDisabled(item);
		if (SlimefunPlugin.getWhitelist().contains(world + ".enabled")) {
			if (SlimefunPlugin.getWhitelist().getBoolean(world + ".enabled")) {
				if (!SlimefunPlugin.getWhitelist().contains(world + ".enabled-items." + sfItem.getID())) SlimefunPlugin.getWhitelist().setDefaultValue(world + ".enabled-items." + sfItem.getID(), true);
				if (SlimefunPlugin.getWhitelist().getBoolean(world + ".enabled-items." + sfItem.getID())) return true;
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
	 * @param  p        the player to get the world he is in
	 * @param  sfItem   the item to check
	 * @param  message  whether a message should be sent to the player or not
	 *
	 * @return <code>true</code> if the item is enabled in the world the player is in,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isEnabled(@Nonnull Player p, @Nonnull SlimefunItem sfItem, boolean message) {
		String world = p.getWorld().getName();
		if (SlimefunPlugin.getWhitelist().contains(world + ".enabled")) {
			if (SlimefunPlugin.getWhitelist().getBoolean(world + ".enabled")) {
				if (!SlimefunPlugin.getWhitelist().contains(world + ".enabled-items." + sfItem.getID())) SlimefunPlugin.getWhitelist().setDefaultValue(world + ".enabled-items." + sfItem.getID(), true);
				if (SlimefunPlugin.getWhitelist().getBoolean(world + ".enabled-items." + sfItem.getID())) return true;
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
		List<String> ids = new ArrayList<>();
		for (SlimefunItem item: SlimefunItem.list()) {
			ids.add(item.getID());
		}
		return ids;
	}

	/**
	 * Returns a list of all the ItemStacks representing the registered categories.
	 *
	 * @return the list of the display items of all the registered categories.
	 */
	public static List<ItemStack> listCategories() {
		List<ItemStack> items = new ArrayList<>();
		for (Category c: Category.list()) {
			items.add(c.getItem());
		}
		return items;
	}

	/**
	 * Returns a list of all the registered SlimefunItem's.
	 *
	 * @return the list of all the registered SlimefunItem's.
	 */
	public static List<SlimefunItem> listSlimefunItems() {
		return new ArrayList<>(SlimefunItem.list());
	}

	/**
	 * Binds this description to the SlimefunItem corresponding to this id.
	 *
	 * @param  id           the id of the SlimefunItem
	 * @param  description  the description
	 *
	 * @deprecated As of 4.1.10, renamed to {@link #addHint(String, String...)} for better name convenience.
	 */
	@Deprecated
	public static void addDescription(@Nonnull String id, @Nonnull String... description) {
		getItemConfig().setDefaultValue(id + ".description", Arrays.asList(description));
	}

	/**
	 * Binds this hint to the SlimefunItem corresponding to this id.
	 *
	 * @param  id    the id of the SlimefunItem
	 * @param  hint  the hint
	 *
	 * @since 4.1.10, rename of {@link #addDescription(String, String...)}.
	 */
	public static void addHint(@Nonnull String id, @Nonnull String... hint) {
		getItemConfig().setDefaultValue(id + ".hint", Arrays.asList(hint));
	}

	/**
	 * Binds this YouTube link to the SlimefunItem corresponding to this id.
	 *
	 * @param  id    the id of the SlimefunItem
	 * @param  link  the link of the YouTube video
	 */
	public static void addYoutubeVideo(@Nonnull String id, @Nonnull String link) {
		getItemConfig().setDefaultValue(id + ".youtube", link);
	}

	/**
	 * Binds this link as a Wiki page to the SlimefunItem corresponding to this id.
	 *
	 * @param  id    the id of the SlimefunItem
	 * @param  link  the link of the Wiki page
	 */
	public static void addWikiPage(@Nonnull String id, @Nonnull String link) {
		getItemConfig().setDefaultValue(id + ".wiki", link);
	}

	/**
	 * Convenience method to simplify binding an official Wiki page to the SlimefunItem corresponding to this id.
	 *
	 * @param  id    the id of the SlimefunItem
	 * @param  page  the ending of the link corresponding to the page
	 */
	public static void addOfficialWikiPage(@Nonnull String id, @Nonnull String page) {
		addWikiPage(id, "https://github.com/TheBusyBiscuit/Slimefun4/wiki/" + page);
	}

	public static List<GuideHandler> getGuideHandlers(int tier) {
		return SlimefunPlugin.getUtilities().guideHandlers.getOrDefault(tier, new ArrayList<>());
	}

	public static String getVersion() {
		return SlimefunPlugin.instance.getDescription().getVersion();
	}

	/**
	 * Convenience method to simply check if an itemstack is soulbound.
	 *
	 * @param  item  the item to check
	 *
	 * @return <code>true</code> if the item is a soulbound item,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isSoulbound(@Nonnull ItemStack item) {
		if (item == null || item.getType() == Material.AIR) return false;
		else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.BOUND_BACKPACK, false)) return true;
		else {

			ItemStack strippedItem = item.clone();
			for (Enchantment enchantment: strippedItem.getEnchantments().keySet()) {
				strippedItem.removeEnchantment(enchantment);
			}

			if (SlimefunPlugin.getHooks().isEmeraldEnchantsInstalled()) {
				for(ItemEnchantment enchantment: EmeraldEnchants.getInstance().getRegistry().getEnchantments(strippedItem)){
					EmeraldEnchants.getInstance().getRegistry().applyEnchantment(strippedItem, enchantment.getEnchantment(), 0);
				}
			}
			if (SlimefunItem.getByItem(strippedItem) instanceof SoulboundItem) return true;

			else {
				if (item.hasItemMeta()) {
					ItemMeta im = item.getItemMeta();
					if (im != null && im.hasLore()) {
						List<String> lore = im.getLore();
						return lore != null && im.getLore().contains(ChatColor.GRAY + "Soulbound");
					}
				}
			}
		}
		return false;
	}
}