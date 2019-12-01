package me.mrCookieSlime.Slimefun;

import java.util.*;
import java.util.logging.Level;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.guides.BookSlimefunGuide;
import me.mrCookieSlime.Slimefun.guides.ChestSlimefunGuide;
import me.mrCookieSlime.Slimefun.guides.ISlimefunGuide;
import me.mrCookieSlime.Slimefun.guides.SlimefunGuideLayout;
import me.mrCookieSlime.Slimefun.hooks.github.Contributor;
import me.mrCookieSlime.Slimefun.hooks.github.IntegerFormat;

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
		lore.addAll(Arrays.asList("" + ChatColors.color("&e右键 &8\u21E8 &7浏览物品"), ChatColors.color("&eShift + 右键 &8\u21E8 &7打开设置菜单")));

        switch (design) {
            case BOOK:
                meta.setDisplayName(ChatColors.color("&a粘液科技指南 &7(书与笔界面)"));
				break;
            case CHEAT_SHEET:
                meta.setDisplayName(ChatColors.color("&c粘液科技指南 &4(作弊模式)"));
				lore.add(0, ChatColors.color("&4&l仅限管理员使用"));
		    	lore.add(0, "");
				break;
            case CHEST:
                meta.setDisplayName(ChatColors.color("&a粘液科技指南 &7(箱子界面)"));
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
        return new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&e粘液科技指南 &7(右键打开)", (book ? "": "&2"), "&r这是粘液科技的基础指南", "&r指南内可以查看粘液科技的所有物品", "&r以及扩展的物品和更多信息");
    }

    public static void openCheatMenu(Player p) {
        openMainMenuAsync(p, false, SlimefunGuideLayout.CHEAT_SHEET, 1);
    }

    @Deprecated
    public static void openGuide(Player p, boolean book) {
        openGuide(p, book ? SlimefunGuideLayout.BOOK: SlimefunGuideLayout.CHEST);
    }

    public static void openGuide(Player p, ItemStack guide) {
        if (SlimefunManager.isItemSimiliar(guide, getItem(SlimefunGuideLayout.CHEST), true)) {
            openGuide(p, SlimefunGuideLayout.CHEST);
        }
        else if (SlimefunManager.isItemSimiliar(guide, getItem(SlimefunGuideLayout.BOOK), true)) {
            openGuide(p, SlimefunGuideLayout.BOOK);
        }
        else if (SlimefunManager.isItemSimiliar(guide, getItem(SlimefunGuideLayout.CHEAT_SHEET), true)) {
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
