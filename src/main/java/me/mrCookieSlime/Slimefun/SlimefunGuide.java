package me.mrCookieSlime.Slimefun;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.guide.BookSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.ChestSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.ISlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public final class SlimefunGuide {

	private SlimefunGuide() {}
	
	static {
		Map<SlimefunGuideLayout, ISlimefunGuide> layouts = SlimefunPlugin.getUtilities().guideLayouts;
		ISlimefunGuide chestGuide = new ChestSlimefunGuide();
		layouts.put(SlimefunGuideLayout.CHEST, chestGuide);
		layouts.put(SlimefunGuideLayout.CHEAT_SHEET, chestGuide);
		layouts.put(SlimefunGuideLayout.BOOK, new BookSlimefunGuide());
	}
	
	@Deprecated
	public static ItemStack getItem() {
		return getItem(SlimefunGuideLayout.CHEST);
	}
	
	public static ItemStack getItem(SlimefunGuideLayout design) {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new LinkedList<>();
		lore.addAll(Arrays.asList("", ChatColors.color("&eRight Click &8\u21E8 &7Browse Items"), ChatColors.color("&eShift + Right Click &8\u21E8 &7Open Settings / Credits")));
		
		switch (design) {
		case BOOK:
			meta.setDisplayName(ChatColors.color("&aSlimefun Guide &7(Book GUI)"));
			break;
		case CHEAT_SHEET:
			meta.setDisplayName(ChatColors.color("&cSlimefun Guide &4(Cheat Sheet)"));
			lore.add(0, ChatColors.color("&4&lOnly openable by Admins"));
			lore.add(0, "");
			break;
		case CHEST:
			meta.setDisplayName(ChatColors.color("&aSlimefun Guide &7(Chest GUI)"));
			break;
		default:
			return null;
		}
		
		meta.setLore(lore);
		SlimefunPlugin.getItemTextureService().setTexture(meta, "SLIMEFUN_GUIDE");
		item.setItemMeta(meta);
		return item;
	}

	@Deprecated
	public static ItemStack getItem(boolean book) {
		return getItem(book ? SlimefunGuideLayout.BOOK: SlimefunGuideLayout.CHEST);
	}

	@Deprecated
	public static ItemStack getDeprecatedItem(boolean book) {
		return new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&eSlimefun Guide &7(Right Click)", (book ? "": "&2"), "&rThis is your basic Guide for Slimefun", "&rYou can see all Items added by this Plugin", "&ror its Addons including their Recipes", "&ra bit of information and more");
	}

	public static void openCheatMenu(Player p) {
		openMainMenuAsync(p, false, SlimefunGuideLayout.CHEAT_SHEET, 1);
	}

	@Deprecated
	public static void openGuide(Player p, boolean book) {
		openGuide(p, book ? SlimefunGuideLayout.BOOK: SlimefunGuideLayout.CHEST);
	}

	public static void openGuide(Player p, ItemStack guide) {
		if (SlimefunManager.isItemSimilar(guide, getItem(SlimefunGuideLayout.CHEST), true)) {
			openGuide(p, SlimefunGuideLayout.CHEST);
		}
		else if (SlimefunManager.isItemSimilar(guide, getItem(SlimefunGuideLayout.BOOK), true)) {
			openGuide(p, SlimefunGuideLayout.BOOK);
		}
		else if (SlimefunManager.isItemSimilar(guide, getItem(SlimefunGuideLayout.CHEAT_SHEET), true)) {
			openGuide(p, SlimefunGuideLayout.CHEAT_SHEET);
		}
	}

	public static void openGuide(Player p, SlimefunGuideLayout layout) {
		if (!SlimefunPlugin.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled")) return;
		if (!SlimefunPlugin.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled-items.SLIMEFUN_GUIDE")) return;

		ISlimefunGuide guide = SlimefunPlugin.getUtilities().guideLayouts.get(layout);
		Object last = null;
		
		Optional<PlayerProfile> profile = PlayerProfile.find(p);
		if (profile.isPresent()) {
			last = guide.getLastEntry(profile.get(), false);
			guide.handleHistory(profile.get(), last, true);
		}
		else {
			openMainMenuAsync(p, true, layout, 1);
		}
	}

	private static void openMainMenuAsync(final Player player, final boolean survival, final SlimefunGuideLayout layout, final int selected_page) {
		if (!PlayerProfile.get(player, profile -> Slimefun.runSync(() -> openMainMenu(profile, layout, survival, selected_page))))
			Slimefun.getLocal().sendMessage(player, "messages.opening-guide");
	}

	public static void openMainMenu(final PlayerProfile profile, SlimefunGuideLayout layout, final boolean survival, final int selected_page) {
		SlimefunPlugin.getUtilities().guideLayouts.get(layout).openMainMenu(profile, survival, selected_page);
	}

	public static void openCategory(final PlayerProfile profile, final Category category, SlimefunGuideLayout layout, final boolean survival, final int selected_page) {
		if (category == null) return;
		SlimefunPlugin.getUtilities().guideLayouts.get(layout).openCategory(profile, category, survival, selected_page);
	}

	public static void openSearch(final PlayerProfile profile, String input, boolean survival, boolean addToHistory) {
		SlimefunPlugin.getUtilities().guideLayouts.get(SlimefunGuideLayout.CHEST).openSearch(profile, input, survival, addToHistory);
	}

	public static void displayItem(PlayerProfile profile, final ItemStack item, boolean addToHistory) {
		SlimefunPlugin.getUtilities().guideLayouts.get(SlimefunGuideLayout.CHEST).displayItem(profile, item, addToHistory);
	}

	public static void displayItem(PlayerProfile profile, final SlimefunItem item, boolean addToHistory) {
		SlimefunPlugin.getUtilities().guideLayouts.get(SlimefunGuideLayout.CHEST).displayItem(profile, item, addToHistory);
	}
}
