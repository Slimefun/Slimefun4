package io.github.thebusybiscuit.slimefun4.core.utils;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public final class ChestMenuUtils {
	
	private ChestMenuUtils() {}
	
	private static final ItemStack UI_BACKGROUND = new SlimefunItemStack("_UI_BACKGROUND", Material.GRAY_STAINED_GLASS_PANE, " ");
	private static final ItemStack BACK_BUTTON = new SlimefunItemStack("_UI_BACK", Material.ENCHANTED_BOOK, "&7\u21E6 Back", meta -> meta.addItemFlags(ItemFlag.HIDE_ENCHANTS));
	private static final ItemStack MENU_BUTTON = new SlimefunItemStack("_UI_MENU", Material.COMPARATOR, "&eSettings / Info", "", "&7\u21E8 Click to see more");
	private static final ItemStack SEARCH_BUTTON = new SlimefunItemStack("_UI_SEARCH", Material.NAME_TAG, "&bSearch");
	private static final ItemStack WIKI_BUTTON = new SlimefunItemStack("_UI_WIKI", Material.KNOWLEDGE_BOOK, "&3Slimefun Wiki");
	
	private static final ItemStack PREV_BUTTON_ACTIVE = new SlimefunItemStack("_UI_PREVIOUS_ACTIVE", Material.LIME_STAINED_GLASS_PANE, "&r\u21E6 Previous Page");
	private static final ItemStack NEXT_BUTTON_ACTIVE = new SlimefunItemStack("_UI_NEXT_ACTIVE", Material.LIME_STAINED_GLASS_PANE, "&rNext Page \u21E8");
	private static final ItemStack PREV_BUTTON_INACTIVE = new SlimefunItemStack("_UI_PREVIOUS_INACTIVE", Material.BLACK_STAINED_GLASS_PANE, "&8\u21E6 Previous Page");
	private static final ItemStack NEXT_BUTTON_INACTIVE = new SlimefunItemStack("_UI_NEXT_INACTIVE", Material.BLACK_STAINED_GLASS_PANE, "&8Next Page \u21E8");
	
	private static final MenuClickHandler CLICK_HANDLER = (p, s, i, a) -> false;
	
	public static final ItemStack getBackground() {
		return UI_BACKGROUND;
	}
	
	public static final MenuClickHandler getEmptyClickHandler() {
		return CLICK_HANDLER;
	}

	public static ItemStack getBackButton() {
		return BACK_BUTTON;
	}

	public static ItemStack getMenuButton() {
		return MENU_BUTTON;
	}

	public static ItemStack getSearchButton() {
		return SEARCH_BUTTON;
	}

	public static ItemStack getWikiButton() {
		return WIKI_BUTTON;
	}

	public static ItemStack getPreviousButton(int page, int pages) {
		if (pages == 1 || page == 1) {
			return new CustomItem(PREV_BUTTON_INACTIVE, meta -> meta.setLore(Arrays.asList("", ChatColor.GRAY + "(" + page + " / " + pages + ")")));
		}
		
		return new CustomItem(PREV_BUTTON_ACTIVE, meta -> meta.setLore(Arrays.asList("", ChatColor.GRAY + "(" + page + " / " + pages + ")")));
	}

	public static ItemStack getNextButton(int page, int pages) {
		if (pages == 1 || page == pages) {
			return new CustomItem(NEXT_BUTTON_INACTIVE, meta -> meta.setLore(Arrays.asList("", ChatColor.GRAY + "(" + page + " / " + pages + ")")));
		}

		return new CustomItem(NEXT_BUTTON_ACTIVE, meta -> meta.setLore(Arrays.asList("", ChatColor.GRAY + "(" + page + " / " + pages + ")")));
	}
	
}
