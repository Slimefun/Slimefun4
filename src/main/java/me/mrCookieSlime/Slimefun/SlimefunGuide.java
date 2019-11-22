package me.mrCookieSlime.Slimefun;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

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

	private static final int[] SLOTS = new int[] {0, 2, 3, 5, 6, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};

	@Deprecated
	public static ItemStack getItem() {
		return getItem(SlimefunGuideLayout.CHEST);
	}
	
	public static ItemStack getItem(SlimefunGuideLayout design) {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new LinkedList<>();
		lore.addAll(Arrays.asList("&eRight Click &8\u21E8 &7Browse Items", "&eShift + Right Click &8\u21E8 &7Open Settings / Credits"));
		
		switch (design) {
		case BOOK:
			meta.setDisplayName("&aSlimefun Guide &7(Book GUI)");
			break;
		case CHEAT_SHEET:
			meta.setDisplayName("&cSlimefun Guide &4(Cheat Sheet)");
			lore.add(0, "&4&lOnly openable by Admins");
			lore.add(0, "");
			break;
		case CHEST:
			meta.setDisplayName("&aSlimefun Guide &7(Chest GUI)");
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

	public static void openSettings(Player p, final ItemStack guide) {
		final ChestMenu menu = new ChestMenu("Settings / Info");

		menu.setEmptySlotsClickable(false);
		menu.addMenuOpeningHandler(
				pl -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F)
		);

		for (int i: SLOTS) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i, (pl, slot, item, action) -> false);
		}


		if (SlimefunManager.isItemSimiliar(guide, getItem(SlimefunGuideLayout.CHEST), true)) {
			if (p.hasPermission("slimefun.cheat.items")) {
				menu.addItem(19, new CustomItem(new ItemStack(Material.CHEST), "&7Guide Layout: &eChest GUI", "", "&aChest GUI", "&7Book GUI", "&7Cheat Sheet", "", "&e Click &8\u21E8 &7Change Layout"));
				menu.addMenuClickHandler(19, (pl, slot, item, action) -> {
					pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.BOOK));
					openSettings(pl, pl.getInventory().getItemInMainHand());
					return false;
				});
			}
			else {
				menu.addItem(19, new CustomItem(new ItemStack(Material.CHEST), "&7Guide Layout: &eChest GUI", "", "&aChest GUI", "&7Book GUI", "", "&e Click &8\u21E8 &7Change Layout"));
				menu.addMenuClickHandler(19, (pl, slot, item, action) -> {
					pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.BOOK));
					openSettings(pl, pl.getInventory().getItemInMainHand());
					return false;
				});
			}
		}
		else if (SlimefunManager.isItemSimiliar(guide, getItem(SlimefunGuideLayout.BOOK), true)) {
			if (p.hasPermission("slimefun.cheat.items")) {
				menu.addItem(19, new CustomItem(new ItemStack(Material.CHEST), "&7Guide Layout: &eBook GUI", "", "&7Chest GUI", "&aBook GUI", "&7Cheat Sheet", "", "&e Click &8\u21E8 &7Change Layout"));
				menu.addMenuClickHandler(19, (pl, slot, item, action) -> {
					pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.CHEAT_SHEET));
					openSettings(pl, pl.getInventory().getItemInMainHand());
					return false;
				});
			}
			else {
				menu.addItem(19, new CustomItem(new ItemStack(Material.CHEST), "&7Guide Layout: &eBook GUI", "", "&7Chest GUI", "&aBook GUI", "", "&e Click &8\u21E8 &7Change Layout"));
				menu.addMenuClickHandler(19, (pl, slot, item, action) -> {
					pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.CHEST));
					openSettings(pl, pl.getInventory().getItemInMainHand());
					return false;
				});
			}
		}
		else if (SlimefunManager.isItemSimiliar(guide, getItem(SlimefunGuideLayout.CHEAT_SHEET), true)) {
			menu.addItem(19, new CustomItem(new ItemStack(Material.CHEST), "&7Guide Layout: &eCheat Sheet", "", "&7Chest GUI", "&7Book GUI", "&aCheat Sheet", "", "&e Click &8\u21E8 &7Change Layout"));
			menu.addMenuClickHandler(19, (pl, slot, item, action) -> {
				pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.CHEST));
				openSettings(pl, pl.getInventory().getItemInMainHand());
				return false;
			});
		}

		menu.addItem(1, new CustomItem(new ItemStack(Material.WRITABLE_BOOK), "&aCredits", "", "&7Version: &a" + Slimefun.getVersion(), "&7Installed Addons: &b" + Slimefun.getInstalledAddons().size(), "&7Contributors: &e" + SlimefunPlugin.getUtilities().contributors.size(), "", "&7\u21E8 Click to see the people behind this Plugin"));
		menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
			openCredits(pl, guide);
			return false;
		});

		try {
			menu.addItem(4, new CustomItem(new ItemStack(Material.COMPARATOR), "&eSource Code", "", "&7Bytes of Code: &6" + IntegerFormat.formatBigNumber(SlimefunPlugin.getUtilities().codeBytes), "&7Last Update: &a" + IntegerFormat.timeDelta(SlimefunPlugin.getUtilities().lastUpdate) + " ago", "&7Forks: &e" + SlimefunPlugin.getUtilities().forks, "&7Stars: &e" + SlimefunPlugin.getUtilities().stars, "", "&7&oSlimefun 4 is a community project,", "&7&othe source code is available on GitHub", "&7&oand if you want to keep this Plugin alive,", "&7&othen please consider contributing to it", "", "&7\u21E8 Click to go to GitHub"));
			menu.addMenuClickHandler(4, (pl, slot, item, action) -> {
				pl.closeInventory();
				pl.sendMessage("");
				pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&ohttps://github.com/TheBusyBiscuit/Slimefun4"));
				pl.sendMessage("");
				return false;
			});
		} catch (Exception x) {
			Slimefun.getLogger().log(Level.SEVERE, "An Error occured while creating the Info-Panel for Slimefun " + Slimefun.getVersion(), x);
		}

		menu.addItem(7, new CustomItem(new ItemStack(Material.REDSTONE), "&4Bug Tracker", "", "&7Open Issues: &a" + SlimefunPlugin.getUtilities().issues, "&7Pending Pull Requests: &a" + SlimefunPlugin.getUtilities().prs, "", "&7\u21E8 Click to go to the Slimefun Bug Tracker"));
		menu.addMenuClickHandler(7, (pl, slot, item, action) -> {
			pl.closeInventory();
			pl.sendMessage("");
			pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&ohttps://github.com/TheBusyBiscuit/Slimefun4/issues"));
			pl.sendMessage("");
			return false;
		});

		menu.open(p);
	}

	private static void openCredits(Player p, final ItemStack guide) {
		final ChestMenu menu = new ChestMenu("Credits");

		menu.setEmptySlotsClickable(false);
		menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F));

		for (int i = 0; i < 9; i++) {
			if (i != 4) {
				menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
				menu.addMenuClickHandler(i, (pl, slot, item, action) -> false);
			}
			else {
				menu.addItem(4, new CustomItem(new ItemStack(Material.EMERALD), "&7\u21E6 Back to Settings"));
				menu.addMenuClickHandler(4, (pl, slot, item, action) -> {
					openSettings(pl, guide);
					return false;
				});
			}
		}

		int index = 9;
		double total = 1.0 * SlimefunPlugin.getUtilities().contributors.stream().mapToInt(Contributor::getCommits).sum();

		for (final Contributor contributor: SlimefunPlugin.getUtilities().contributors) {
			ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

			try {
				String texture = contributor.getTexture();
				if (texture != null) skull = CustomSkull.getItem(texture);
			} catch (Exception x) {
				Slimefun.getLogger().log(Level.SEVERE, "An Error occured while fetching a Contributor Head for Slimefun " + Slimefun.getVersion(), x);
			}

			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setDisplayName(ChatColor.GOLD + contributor.getName());

			if (contributor.getCommits() > 0) {
				double percentage = DoubleHandler.fixDouble((contributor.getCommits() * 100.0) / total, 2);
				meta.setLore(Arrays.asList("", ChatColor.translateAlternateColorCodes('&', "&7Role: &r" + contributor.getJob()), ChatColor.translateAlternateColorCodes('&', "&7Contributions: &r" + contributor.getCommits() + " commits &7(&r" + percentage + "%&7)"), "", ChatColor.translateAlternateColorCodes('&', "&7\u21E8 Click to view my GitHub profile")));
			}
			else {
				meta.setLore(Arrays.asList("", ChatColor.translateAlternateColorCodes('&', "&7Role: &r" + contributor.getJob())));
			}

			skull.setItemMeta(meta);

			menu.addItem(index, skull);
			menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
				if (contributor.getCommits() > 0) {
					pl.closeInventory();
					pl.sendMessage("");
					pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&o" + contributor.getProfile()));
					pl.sendMessage("");
				}
				return false;
			});

			index++;
		}

		menu.open(p);
	}

	public static void openCheatMenu(Player p) {
		openMainMenuAsync(p, false, SlimefunGuideLayout.CHEAT_SHEET, 1);
	}

	@Deprecated
	public static void openGuide(Player p, boolean book) {
		openGuide(p, book ? SlimefunGuideLayout.BOOK: SlimefunGuideLayout.CHEST);
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
