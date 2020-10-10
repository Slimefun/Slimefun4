package io.github.thebusybiscuit.slimefun4.core.guide;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.guide.BookSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.implementation.guide.ChestSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This is a static utility class that provides convenient access to the methods
 * of {@link SlimefunGuideImplementation} that abstracts away the actual implementation.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunGuideImplementation
 * @see ChestSlimefunGuide
 * @see BookSlimefunGuide
 *
 */
public final class SlimefunGuide {

    private SlimefunGuide() {}

    public static ItemStack getItem(@Nonnull SlimefunGuideLayout design) {
        return SlimefunPlugin.getRegistry().getGuideLayout(design).getItem();
    }

    public static void openCheatMenu(Player p) {
        openMainMenuAsync(p, SlimefunGuideLayout.CHEAT_SHEET, 1);
    }

    public static void openGuide(Player p, ItemStack guide) {
        if (SlimefunUtils.isItemSimilar(guide, getItem(SlimefunGuideLayout.CHEST), true)) {
            openGuide(p, SlimefunGuideLayout.CHEST);
        } else if (SlimefunUtils.isItemSimilar(guide, getItem(SlimefunGuideLayout.BOOK), true)) {
            openGuide(p, SlimefunGuideLayout.BOOK);
        } else if (SlimefunUtils.isItemSimilar(guide, getItem(SlimefunGuideLayout.CHEAT_SHEET), true)) {
            openGuide(p, SlimefunGuideLayout.CHEAT_SHEET);
        } else {
            // When using /sf cheat or /sf open_guide, ItemStack is null.
            openGuide(p, SlimefunGuideLayout.CHEST);
        }
    }

    public static void openGuide(Player p, SlimefunGuideLayout layout) {
        if (!SlimefunPlugin.getWorldSettingsService().isWorldEnabled(p.getWorld())) {
            return;
        }

        Optional<PlayerProfile> optional = PlayerProfile.find(p);

        if (optional.isPresent()) {
            PlayerProfile profile = optional.get();
            SlimefunGuideImplementation guide = SlimefunPlugin.getRegistry().getGuideLayout(layout);
            profile.getGuideHistory().openLastEntry(guide);
        } else {
            openMainMenuAsync(p, layout, 1);
        }
    }

    private static void openMainMenuAsync(Player player, SlimefunGuideLayout layout, int selectedPage) {
        if (!PlayerProfile.get(player, profile -> SlimefunPlugin.runSync(() -> openMainMenu(profile, layout, selectedPage)))) {
            SlimefunPlugin.getLocalization().sendMessage(player, "messages.opening-guide");
        }
    }

    public static void openMainMenu(PlayerProfile profile, SlimefunGuideLayout layout, int selectedPage) {
        SlimefunPlugin.getRegistry().getGuideLayout(layout).openMainMenu(profile, selectedPage);
    }

    public static void openCategory(PlayerProfile profile, Category category, SlimefunGuideLayout layout, int selectedPage) {
        if (category == null) {
            return;
        }

        SlimefunPlugin.getRegistry().getGuideLayout(layout).openCategory(profile, category, selectedPage);
    }

    public static void openSearch(PlayerProfile profile, String input, boolean survival, boolean addToHistory) {
        SlimefunGuideImplementation layout = SlimefunPlugin.getRegistry().getGuideLayout(SlimefunGuideLayout.CHEST);

        if (!survival) {
            layout = SlimefunPlugin.getRegistry().getGuideLayout(SlimefunGuideLayout.CHEAT_SHEET);
        }

        layout.openSearch(profile, input, addToHistory);
    }

    public static void displayItem(PlayerProfile profile, ItemStack item, boolean addToHistory) {
        SlimefunPlugin.getRegistry().getGuideLayout(SlimefunGuideLayout.CHEST).displayItem(profile, item, 0, addToHistory);
    }

    public static void displayItem(PlayerProfile profile, SlimefunItem item, boolean addToHistory) {
        SlimefunPlugin.getRegistry().getGuideLayout(SlimefunGuideLayout.CHEST).displayItem(profile, item, addToHistory);
    }

    public static boolean isGuideItem(ItemStack item) {
        return SlimefunUtils.isItemSimilar(item, getItem(SlimefunGuideLayout.CHEST), true) || SlimefunUtils.isItemSimilar(item, getItem(SlimefunGuideLayout.BOOK), true) || SlimefunUtils.isItemSimilar(item, getItem(SlimefunGuideLayout.CHEAT_SHEET), true);
    }

    public static SlimefunGuideLayout getDefaultLayout() {
        if (SlimefunPlugin.getCfg().getBoolean("guide.default-view-book")) {
            return SlimefunGuideLayout.BOOK;
        } else {
            return SlimefunGuideLayout.CHEST;
        }
    }
}
