package io.github.thebusybiscuit.slimefun4.core.guide;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import io.github.thebusybiscuit.slimefun4.core.services.github.Contributor;
import io.github.thebusybiscuit.slimefun4.core.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.core.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.core.utils.NumberUtils;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public final class GuideSettings {
	
	public static final NamespacedKey FIREWORKS_KEY = new NamespacedKey(SlimefunPlugin.instance, "research_fireworks");
	
	private static final int[] BACKGROUND_SLOTS = {1, 3, 5, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};

	private GuideSettings() {}
	
	public static void openSettings(Player p, final ItemStack guide) {
		ChestMenu menu = new ChestMenu("Settings / Info");

		menu.setEmptySlotsClickable(false);
		menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F));

		for (int slot : BACKGROUND_SLOTS) {
			menu.addItem(slot, ChestMenuUtils.getBackground());
			menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
		}
		
		addMenubar(p, menu, guide);
		addConfigurableOptions(p, menu, guide);

		menu.open(p);
	}

	private static void addMenubar(Player p, ChestMenu menu, ItemStack guide) {
		menu.addItem(0, new CustomItem(getItem(SlimefunGuideLayout.CHEST), "&e\u21E6 Back", "", "&7Go back to your Slimefun Guide"),
		(pl, slot, item, action) -> {
			SlimefunGuide.openGuide(pl, guide);
			return false;
		});

		menu.addItem(2, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTk1MmQyYjNmMzUxYTZiMDQ4N2NjNTlkYjMxYmY1ZjI2NDExMzNlNWJhMDAwNmIxODU3NmU5OTZhMDI5M2U1MiJ9fX0="),
				"&cCredits",
				"",
				"&7Contributors: &e" + SlimefunPlugin.getGitHubService().getContributors().size(),
				"",
				"&7Slimefun is an open-source project",
				"&7and maintained by a large community.",
				"&7Here you can find them all", 
				"", 
				"&7\u21E8 Click to see all of them"
		),
		(pl, slot, action, item) -> {
			openCredits(pl, 0);
			return false;
		});

		menu.addItem(4, new CustomItem(new ItemStack(Material.WRITABLE_BOOK),
				"&aSlimefun Version",
				"&7&oThis is very important when reporting bugs!",
				"",
				"&7Minecraft Version: &a" + Bukkit.getBukkitVersion(),
				"&7Slimefun Version: &a" + Slimefun.getVersion(),
				"&7CS-CoreLib Version: &a" + CSCoreLib.getLib().getDescription().getVersion(),
				"&7Installed Addons: &b" + Slimefun.getInstalledAddons().size()
		),  ChestMenuUtils.getEmptyClickHandler());

		menu.addItem(6, new CustomItem(new ItemStack(Material.COMPARATOR), 
				"&eSource Code", 
				"", 
				"&7Bytes of Code: &6" + NumberUtils.formatBigNumber(SlimefunPlugin.getGitHubService().getCodeSize()), 
				"&7Last Activity: &a" + NumberUtils.timeDelta(SlimefunPlugin.getGitHubService().getLastUpdate()) + " ago", 
				"&7Forks: &e" + SlimefunPlugin.getGitHubService().getForks(), 
				"&7Stars: &e" + SlimefunPlugin.getGitHubService().getStars(), 
				"", 
				"&7&oSlimefun 4 is a community project,", 
				"&7&othe source code is available on GitHub", 
				"&7&oand if you want to keep this Plugin alive,", 
				"&7&othen please consider contributing to it", 
				"", 
				"&7\u21E8 Click to go to GitHub"
		),
		(pl, slot, item, action) -> {
			pl.closeInventory();
			ChatUtils.sendURL(pl, "https://github.com/TheBusyBiscuit/Slimefun4");
			return false;
		});

		menu.addItem(8, new CustomItem(new ItemStack(Material.KNOWLEDGE_BOOK), 
				"&3Slimefun Wiki", 
				"", 
				"&7Do you need help with an Item or machine?", 
				"&7You cannot figure out what to do?", 
				"&7Check out our community-maintained Wiki", 
				"&7and become one of our Editors!", 
				"", 
				"&7\u21E8 Click to go to the official Slimefun Wiki"
		),
		(pl, slot, item, action) -> {
			pl.closeInventory();
			ChatUtils.sendURL(pl, "https://github.com/TheBusyBiscuit/Slimefun4/wiki");
			return false;
		});
	}

	private static void addConfigurableOptions(Player p, ChestMenu menu, ItemStack guide) {
		int i = 19;
		
		if (SlimefunManager.isItemSimilar(guide, getItem(SlimefunGuideLayout.CHEST), true)) {
			if (p.hasPermission("slimefun.cheat.items")) {
				menu.addItem(i, new CustomItem(new ItemStack(Material.CHEST), "&7Guide Layout: &eChest GUI", "", "&aChest GUI", "&7Book GUI", "&7Cheat Sheet", "", "&e Click &8\u21E8 &7Change Layout"));
				menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
					pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.BOOK));
					openSettings(pl, pl.getInventory().getItemInMainHand());
					return false;
				});
			}
			else {
				menu.addItem(i, new CustomItem(new ItemStack(Material.CHEST), "&7Guide Layout: &eChest GUI", "", "&aChest GUI", "&7Book GUI", "", "&e Click &8\u21E8 &7Change Layout"));
				menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
					pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.BOOK));
					openSettings(pl, pl.getInventory().getItemInMainHand());
					return false;
				});
			}
			
			i++;
		}
		else if (SlimefunManager.isItemSimilar(guide, getItem(SlimefunGuideLayout.BOOK), true)) {
			if (p.hasPermission("slimefun.cheat.items")) {
				menu.addItem(i, new CustomItem(new ItemStack(Material.BOOK), "&7Guide Layout: &eBook GUI", "", "&7Chest GUI", "&aBook GUI", "&7Cheat Sheet", "", "&e Click &8\u21E8 &7Change Layout"));
				menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
					pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.CHEAT_SHEET));
					openSettings(pl, pl.getInventory().getItemInMainHand());
					return false;
				});
			}
			else {
				menu.addItem(i, new CustomItem(new ItemStack(Material.BOOK), "&7Guide Layout: &eBook GUI", "", "&7Chest GUI", "&aBook GUI", "", "&e Click &8\u21E8 &7Change Layout"));
				menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
					pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.CHEST));
					openSettings(pl, pl.getInventory().getItemInMainHand());
					return false;
				});
			}
			
			i++;
		}
		else if (SlimefunManager.isItemSimilar(guide, getItem(SlimefunGuideLayout.CHEAT_SHEET), true)) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.COMMAND_BLOCK), "&7Guide Layout: &eCheat Sheet", "", "&7Chest GUI", "&7Book GUI", "&aCheat Sheet", "", "&e Click &8\u21E8 &7Change Layout"));
			menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
				pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.CHEST));
				openSettings(pl, pl.getInventory().getItemInMainHand());
				return false;
			});
			
			i++;
		}

		if (SlimefunPlugin.getSettings().researchFireworksEnabled) {
			if (!PersistentDataAPI.hasByte(p, FIREWORKS_KEY) || PersistentDataAPI.getByte(p, FIREWORKS_KEY) == (byte) 1) {
				menu.addItem(i, new CustomItem(new ItemStack(Material.FIREWORK_ROCKET), "&bFireworks: &aYes", "", "&7When researching items, you will", "&7be presented with a big firework.", "", "&7\u21E8 Click to toggle your fireworks"),
				(pl, slot, item, action) -> {
					PersistentDataAPI.setByte(pl, FIREWORKS_KEY, (byte) 0);
					openSettings(pl, guide);
					return false;
				});
			}
			else {
				menu.addItem(i, new CustomItem(new ItemStack(Material.FIREWORK_ROCKET), "&bFireworks: &4No", "", "&7When researching items, you will", "&7not be presented with a big firework.", "", "&7\u21E8 Click to toggle your fireworks"),
				(pl, slot, item, action) -> {
					PersistentDataAPI.setByte(pl, FIREWORKS_KEY, (byte) 1);
					openSettings(pl, guide);
					return false;
				});
			}
			
			i++;
		}

		menu.addItem(i, new CustomItem(new ItemStack(Material.REDSTONE), "&4Report a bug", "", "&7Open Issues: &a" + SlimefunPlugin.getGitHubService().getIssues(), "&7Pending Pull Requests: &a" + SlimefunPlugin.getGitHubService().getPullRequests(), "", "&7\u21E8 Click to go to the Slimefun4 Bug Tracker"),
		(pl, slot, item, action) -> {
			pl.closeInventory();
			ChatUtils.sendURL(pl, "https://github.com/TheBusyBiscuit/Slimefun4/issues");
			return false;
		});
	}

	private static void openCredits(Player p, int page) {
		ChestMenu menu = new ChestMenu("Credits");

		menu.setEmptySlotsClickable(false);
		menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F));

		for (int i = 0; i < 9; i++) {
			if (i != 1) {
				menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
			}
			else {
				menu.addItem(1, new CustomItem(ChestMenuUtils.getBackButton(), "&e\u21E6 Back", "", "&7Go back to the Settings Panel"));
				menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
					openSettings(pl, p.getInventory().getItemInMainHand());
					return false;
				});
			}
		}
		
		for (int i = 45; i < 54; i++) {
			if (i != 46 && i != 52) {
				menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
			}
		}
		
		List<Contributor> contributors = new ArrayList<>(SlimefunPlugin.getGitHubService().getContributors().values());
		contributors.sort(Comparator.comparingInt(Contributor::index));
		
		int pages = (contributors.size() - 1) / 36 + 1;
		
		for (int i = page * 36; i < contributors.size(); i++) {
			if (i >= (page + 1) * 36) {
				break;
			}
			
			Contributor contributor = contributors.get(i);
			ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
			
			try {
				skull = SkullItem.fromBase64(contributor.getTexture());
			} catch (Exception e) {
				Slimefun.getLogger().log(Level.SEVERE, "An Error occurred while inserting a Contributors head.", e);
			}

			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setDisplayName(ChatColor.GRAY + contributor.getName()
					+ (!contributor.getName().equals(contributor.getMinecraftName()) ? ChatColor.DARK_GRAY + " (MC: " + contributor.getMinecraftName() + ")" : "")
			);
			
			List<String> lore = new LinkedList<>();
			lore.add("");
			
			for (Map.Entry<String, Integer> entry : contributor.getContributions().entrySet()) {
				String info = entry.getKey();
				
				if (entry.getValue() > 0) {
					info += " &7(" + entry.getValue() + " Commit" + (entry.getValue() > 1 ? "s": "") + ")";
				}
				
				lore.add(ChatColors.color(info));
			}
			
			lore.add("");
			lore.add(ChatColors.color("&7\u21E8 Click to visit " + contributor.getName() + "'s profile"));
			meta.setLore(lore);
			skull.setItemMeta(meta);

			menu.addItem(i - page * 36 + 9, skull);
			menu.addMenuClickHandler(i - page * 36 + 9, (pl, slot, item, action) -> {
				pl.closeInventory();
				ChatUtils.sendURL(pl, contributor.getProfile());
				return false;
			});
		}

		menu.addItem(46, ChestMenuUtils.getPreviousButton(page + 1, pages));
		menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
			if (page > 0) openCredits(pl, page - 1);
			return false;
		});

		menu.addItem(52, ChestMenuUtils.getNextButton(page + 1, pages));
		menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
			if (page + 1 < pages) openCredits(pl, page + 1);
			return false;
		});

		menu.open(p);
	}

	private static ItemStack getItem(SlimefunGuideLayout layout) {
		return SlimefunGuide.getItem(layout);
	}
	
}
