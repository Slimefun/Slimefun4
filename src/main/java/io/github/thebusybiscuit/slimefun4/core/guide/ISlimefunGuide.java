package io.github.thebusybiscuit.slimefun4.core.guide;

import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.GuideHandler;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;

public interface ISlimefunGuide {
	
	SlimefunGuideLayout getLayout();
	ItemStack getItem();
	
	void openMainMenu(PlayerProfile profile, boolean survival, int page);
	void openCategory(PlayerProfile profile, Category category, boolean survival, int page);
	void openSearch(PlayerProfile profile, String input, boolean survival, boolean addToHistory);
	void displayItem(PlayerProfile profile, ItemStack item, boolean addToHistory);
	void displayItem(PlayerProfile profile, SlimefunItem item, boolean addToHistory);
	
	default String shorten(String string, String string2) {
		if (ChatColor.stripColor(string + string2).length() > 19) {
			return (string + ChatColor.stripColor(string2)).substring(0, 18) + "...";
		}
		else {
			return string + ChatColor.stripColor(string2);
		}
	}
	
	default Object getLastEntry(PlayerProfile profile, boolean remove) {
		LinkedList<Object> history = profile.getGuideHistory();

		if (remove && !history.isEmpty()) {
			history.removeLast();
		}

		return history.isEmpty() ? null: history.getLast();
	}

	default void handleHistory(PlayerProfile profile, Object last, boolean survival) {
		if (last == null) openMainMenu(profile, survival, 1);
		else if (last instanceof Category) openCategory(profile, (Category) last, survival, 1);
		else if (last instanceof SlimefunItem) displayItem(profile, (SlimefunItem) last, false);
		else if (last instanceof GuideHandler) ((GuideHandler) last).run(profile.getPlayer(), survival, getLayout() == SlimefunGuideLayout.BOOK);
		else if (last instanceof String) openSearch(profile, (String) last, survival, false);
		else displayItem(profile, (ItemStack) last, false);
	}
}
