package io.github.thebusybiscuit.slimefun4.core.guide;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import io.github.thebusybiscuit.slimefun4.api.events.PlayerLanguageChangeEvent;
import io.github.thebusybiscuit.slimefun4.core.services.github.Contributor;
import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

/**
 * This static utility class offers various methods that provide access to the
 * Settings menu of our {@link SlimefunGuide}.
 * 
 * This menu is used to allow a {@link Player} to change things such as the {@link Language}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunGuide
 *
 */
public final class SlimefunGuideSettings {

    public static final NamespacedKey FIREWORKS_KEY = new NamespacedKey(SlimefunPlugin.instance, "research_fireworks");
    private static final int[] BACKGROUND_SLOTS = { 1, 3, 5, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 48, 50, 52, 53 };

    private SlimefunGuideSettings() {}

    public static void openSettings(Player p, ItemStack guide) {
        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocal().getMessage(p, "guide.title.settings"));

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F));

        ChestMenuUtils.drawBackground(menu, BACKGROUND_SLOTS);

        addHeader(p, menu, guide);
        addConfigurableOptions(p, menu, guide);

        menu.open(p);
    }

    private static void addHeader(Player p, ChestMenu menu, ItemStack guide) {
        menu.addItem(0, new CustomItem(getItem(SlimefunGuideLayout.CHEST), "&e\u21E6 " + SlimefunPlugin.getLocal().getMessage(p, "guide.back.title"), "", "&7" + SlimefunPlugin.getLocal().getMessage(p, "guide.back.guide")), (pl, slot, item, action) -> {
            SlimefunGuide.openGuide(pl, guide);
            return false;
        });

        menu.addItem(2, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTk1MmQyYjNmMzUxYTZiMDQ4N2NjNTlkYjMxYmY1ZjI2NDExMzNlNWJhMDAwNmIxODU3NmU5OTZhMDI5M2U1MiJ9fX0="), "&c" + SlimefunPlugin.getLocal().getMessage(p, "guide.title.credits"), "", "&7Contributors: &e" + SlimefunPlugin.getGitHubService().getContributors().size(), "", "&7Slimefun is an open-source project", "&7and maintained by a large community.", "&7Here you can find them all", "", "&7\u21E8 &eClick to see our contributors"), (pl, slot, action, item) -> {
            openCredits(pl, 0);
            return false;
        });

        menu.addItem(4, new CustomItem(Material.WRITABLE_BOOK, "&aSlimefun Version", "&7&o" + SlimefunPlugin.getLocal().getMessage(p, "guide.tooltips.versions-notice"), "", "&rMinecraft Version: &a" + Bukkit.getBukkitVersion(), "&rSlimefun Version: &a" + SlimefunPlugin.getVersion(), "&rCS-CoreLib Version: &a" + CSCoreLib.getLib().getDescription().getVersion()), ChestMenuUtils.getEmptyClickHandler());

        menu.addItem(6, new CustomItem(Material.COMPARATOR, "&e" + SlimefunPlugin.getLocal().getMessage(p, "guide.title.source"), "", "&7Last Activity: &a" + NumberUtils.timeDelta(SlimefunPlugin.getGitHubService().getLastUpdate()) + " ago", "&7Forks: &e" + SlimefunPlugin.getGitHubService().getForks(), "&7Stars: &e" + SlimefunPlugin.getGitHubService().getStars(), "", "&7&oSlimefun 4 is a community project,", "&7&othe source code is available on GitHub", "&7&oand if you want to keep this Plugin alive,", "&7&othen please consider contributing to it", "", "&7\u21E8 &eClick to go to GitHub"));
        menu.addMenuClickHandler(6, (pl, slot, item, action) -> {
            pl.closeInventory();
            ChatUtils.sendURL(pl, "https://github.com/TheBusyBiscuit/Slimefun4");
            return false;
        });

        menu.addItem(8, new CustomItem(Material.KNOWLEDGE_BOOK, "&3" + SlimefunPlugin.getLocal().getMessage(p, "guide.title.wiki"), "", "&7Do you need help with an Item or machine?", "&7You cannot figure out what to do?", "&7Check out our community-maintained Wiki", "&7and become one of our Editors!", "", "&7\u21E8 &eClick to go to the official Slimefun Wiki"), (pl, slot, item, action) -> {
            pl.closeInventory();
            ChatUtils.sendURL(pl, "https://github.com/TheBusyBiscuit/Slimefun4/wiki");
            return false;
        });

        menu.addItem(47, new CustomItem(Material.BOOKSHELF, "&3" + SlimefunPlugin.getLocal().getMessage(p, "guide.title.addons"), "", "&7Slimefun is huge. But its addons are what makes", "&7this plugin truly shine. Go check them out, some", "&7of them may be exactly what you were missing out on!", "", "&7Installed on this Server: &b" + SlimefunPlugin.getInstalledAddons().size(), "", "&7\u21E8 &eClick to see all available Addons for Slimefun4"), (pl, slot, item, action) -> {
            pl.closeInventory();
            ChatUtils.sendURL(pl, "https://github.com/TheBusyBiscuit/Slimefun4/wiki/Addons");
            return false;
        });

        menu.addItem(49, new CustomItem(Material.REDSTONE_TORCH, "&4" + SlimefunPlugin.getLocal().getMessage(p, "guide.title.bugs"), "", "&7&oBug reports have to be made in English!", "", "&7Open Issues: &a" + SlimefunPlugin.getGitHubService().getIssues(), "&7Pending Pull Requests: &a" + SlimefunPlugin.getGitHubService().getPullRequests(), "", "&7\u21E8 &eClick to go to the Slimefun4 Bug Tracker"), (pl, slot, item, action) -> {
            pl.closeInventory();
            ChatUtils.sendURL(pl, "https://github.com/TheBusyBiscuit/Slimefun4/issues");
            return false;
        });

        menu.addItem(51, new CustomItem(Material.TOTEM_OF_UNDYING, "&cSoon", "", "&7Something will be added here later..."), (pl, slot, item, action) -> {
            // Add something here
            return false;
        });
    }

    private static void addConfigurableOptions(Player p, ChestMenu menu, ItemStack guide) {
        int i = 19;

        if (SlimefunManager.isItemSimilar(guide, getItem(SlimefunGuideLayout.CHEST), true)) {
            if (p.hasPermission("slimefun.cheat.items")) {
                menu.addItem(i, new CustomItem(Material.CHEST, "&7Guide Layout: &eChest GUI", "", "&aChest GUI", "&7Book GUI", "&7Cheat Sheet", "", "&7\u21E8 &eClick to change your layout"));
                menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                    pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.BOOK));
                    openSettings(pl, pl.getInventory().getItemInMainHand());
                    return false;
                });
            }
            else {
                menu.addItem(i, new CustomItem(Material.CHEST, "&7Guide Layout: &eChest GUI", "", "&aChest GUI", "&7Book GUI", "", "&7\u21E8 &eClick to change your layout"));
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
                menu.addItem(i, new CustomItem(Material.BOOK, "&7Guide Layout: &eBook GUI", "", "&7Chest GUI", "&aBook GUI", "&7Cheat Sheet", "", "&7\u21E8 &eClick to change your layout"));
                menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                    pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.CHEAT_SHEET));
                    openSettings(pl, pl.getInventory().getItemInMainHand());
                    return false;
                });
            }
            else {
                menu.addItem(i, new CustomItem(Material.BOOK, "&7Guide Layout: &eBook GUI", "", "&7Chest GUI", "&aBook GUI", "", "&7\u21E8 &eClick to change your layout"));
                menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                    pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.CHEST));
                    openSettings(pl, pl.getInventory().getItemInMainHand());
                    return false;
                });
            }

            i++;
        }
        else if (SlimefunManager.isItemSimilar(guide, getItem(SlimefunGuideLayout.CHEAT_SHEET), true)) {
            menu.addItem(i, new CustomItem(Material.COMMAND_BLOCK, "&7Guide Layout: &eCheat Sheet", "", "&7Chest GUI", "&7Book GUI", "&aCheat Sheet", "", "&7\u21E8 &eClick to change your layout"));
            menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.CHEST));
                openSettings(pl, pl.getInventory().getItemInMainHand());
                return false;
            });

            i++;
        }

        if (SlimefunPlugin.getRegistry().isResearchFireworkEnabled()) {
            if (!PersistentDataAPI.hasByte(p, FIREWORKS_KEY) || PersistentDataAPI.getByte(p, FIREWORKS_KEY) == (byte) 1) {
                menu.addItem(i, new CustomItem(Material.FIREWORK_ROCKET, "&bFireworks: &aYes", "", "&7When researching items, you will", "&7be presented with a big firework.", "", "&7\u21E8 &eClick to disable your fireworks"), (pl, slot, item, action) -> {
                    PersistentDataAPI.setByte(pl, FIREWORKS_KEY, (byte) 0);
                    openSettings(pl, guide);
                    return false;
                });
            }
            else {
                menu.addItem(i, new CustomItem(Material.FIREWORK_ROCKET, "&bFireworks: &4No", "", "&7When researching items, you will", "&7not be presented with a big firework.", "", "&7\u21E8 &eClick to enable your fireworks"), (pl, slot, item, action) -> {
                    PersistentDataAPI.setByte(pl, FIREWORKS_KEY, (byte) 1);
                    openSettings(pl, guide);
                    return false;
                });
            }

            i++;
        }

        if (SlimefunPlugin.getLocal().isEnabled()) {
            Language language = SlimefunPlugin.getLocal().getLanguage(p);
            String languageName = language.isDefault() ? (SlimefunPlugin.getLocal().getMessage(p, "languages.default") + ChatColor.DARK_GRAY + " (" + language.getName(p) + ")") : SlimefunPlugin.getLocal().getMessage(p, "languages." + language.getID());

            menu.addItem(i, new CustomItem(language.getItem(), "&7" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.selected-language") + " &a" + languageName, "", "&7You now have the option to change", "&7the language in which Slimefun", "&7will send you messages.", "&7Note that this only translates", "&7messages, not items.", "", "&7\u21E8 &eClick to change your language"), (pl, slot, item, action) -> {
                openLanguages(pl);
                return false;
            });

            // i++;
        }
    }

    private static void openLanguages(Player p) {
        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocal().getMessage(p, "guide.title.languages"));

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F));

        for (int i = 0; i < 9; i++) {
            if (i == 1) {
                menu.addItem(1, ChestMenuUtils.getBackButton(p, "", "&7" + SlimefunPlugin.getLocal().getMessage(p, "guide.back.settings")), (pl, slot, item, action) -> {
                    openSettings(pl, p.getInventory().getItemInMainHand());
                    return false;
                });
            }
            else if (i == 7) {
                menu.addItem(7, new CustomItem(SkullItem.fromHash("3edd20be93520949e6ce789dc4f43efaeb28c717ee6bfcbbe02780142f716"), SlimefunPlugin.getLocal().getMessage(p, "guide.languages.translations.name"), "", "&7\u21E8 &e" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.translations.lore")), (pl, slot, item, action) -> {
                    ChatUtils.sendURL(pl, "https://github.com/TheBusyBiscuit/Slimefun4/wiki/Translating-Slimefun");
                    pl.closeInventory();
                    return false;
                });
            }
            else {
                menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
            }
        }

        Language defaultLanguage = SlimefunPlugin.getLocal().getDefaultLanguage();
        String defaultLanguageString = SlimefunPlugin.getLocal().getMessage(p, "languages.default");

        menu.addItem(9, new CustomItem(defaultLanguage.getItem(), ChatColor.GRAY + defaultLanguageString + ChatColor.DARK_GRAY + " (" + defaultLanguage.getName(p) + ")", "", "&7\u21E8 &e" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.select-default")), (pl, i, item, action) -> {
            SlimefunPlugin.instance.getServer().getPluginManager().callEvent(new PlayerLanguageChangeEvent(pl, SlimefunPlugin.getLocal().getLanguage(pl), defaultLanguage));
            PersistentDataAPI.remove(pl, SlimefunPlugin.getLocal().getKey());

            SlimefunPlugin.getLocal().sendMessage(pl, "guide.languages.updated", msg -> msg.replace("%lang%", defaultLanguageString));

            openSettings(pl, p.getInventory().getItemInMainHand());
            return false;
        });

        int slot = 10;

        for (Language language : SlimefunPlugin.getLocal().getLanguages()) {
            menu.addItem(slot, new CustomItem(language.getItem(), ChatColor.GREEN + language.getName(p), "&b" + SlimefunPlugin.getLocal().getProgress(language) + '%', "", "&7\u21E8 &e" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.select")), (pl, i, item, action) -> {
                SlimefunPlugin.instance.getServer().getPluginManager().callEvent(new PlayerLanguageChangeEvent(pl, SlimefunPlugin.getLocal().getLanguage(pl), language));
                PersistentDataAPI.setString(pl, SlimefunPlugin.getLocal().getKey(), language.getID());

                String name = language.getName(pl);
                SlimefunPlugin.getLocal().sendMessage(pl, "guide.languages.updated", msg -> msg.replace("%lang%", name));

                openSettings(pl, p.getInventory().getItemInMainHand());
                return false;
            });

            slot++;
        }

        menu.open(p);
    }

    private static void openCredits(Player p, int page) {
        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocal().getMessage(p, "guide.title.credits"));

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F));

        ChestMenuUtils.drawBackground(menu, 0, 2, 3, 4, 5, 6, 7, 8, 45, 47, 48, 49, 50, 51, 52);

        menu.addItem(1, new CustomItem(ChestMenuUtils.getBackButton(p, "", "&7" + SlimefunPlugin.getLocal().getMessage(p, "guide.back.settings"))));
        menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
            openSettings(pl, p.getInventory().getItemInMainHand());
            return false;
        });

        List<Contributor> contributors = new ArrayList<>(SlimefunPlugin.getGitHubService().getContributors().values());
        contributors.sort(Comparator.comparingInt(Contributor::index));

        int pages = (contributors.size() - 1) / 36 + 1;

        for (int i = page * 36; i < contributors.size() && i < (page + 1) * 36; i++) {
            Contributor contributor = contributors.get(i);
            ItemStack skull = getContributorHead(p, contributor);

            menu.addItem(i - page * 36 + 9, skull);
            menu.addMenuClickHandler(i - page * 36 + 9, (pl, slot, item, action) -> {
                if (contributor.getProfile() != null) {
                    pl.closeInventory();
                    ChatUtils.sendURL(pl, contributor.getProfile());
                }
                return false;
            });
        }

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page + 1, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            if (page > 0) openCredits(pl, page - 1);
            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page + 1, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            if (page + 1 < pages) openCredits(pl, page + 1);
            return false;
        });

        menu.open(p);
    }

    private static ItemStack getContributorHead(Player p, Contributor contributor) {
        ItemStack skull = SkullItem.fromBase64(contributor.getTexture());

        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setDisplayName(contributor.getDisplayName());

        List<String> lore = new LinkedList<>();
        lore.add("");

        for (Map.Entry<String, Integer> entry : contributor.getContributions()) {
            String info = entry.getKey();

            if (!info.startsWith("&")) {
                String[] segments = PatternUtils.COMMA.split(info);
                info = SlimefunPlugin.getLocal().getMessage(p, "guide.credits.roles." + segments[0]);

                if (segments.length == 2) {
                    info += " &7(" + SlimefunPlugin.getLocal().getMessage(p, "languages." + segments[1]) + ')';
                }
            }

            if (entry.getValue() > 0) {
                String commits = SlimefunPlugin.getLocal().getMessage(p, "guide.credits." + (entry.getValue() > 1 ? "commits" : "commit"));

                info += " &7(" + entry.getValue() + ' ' + commits + ')';
            }

            lore.add(ChatColors.color(info));
        }

        if (contributor.getProfile() != null) {
            lore.add("");
            lore.add(ChatColors.color("&7\u21E8 &e") + SlimefunPlugin.getLocal().getMessage(p, "guide.credits.profile-link"));
        }

        meta.setLore(lore);
        skull.setItemMeta(meta);
        return skull;
    }

    private static ItemStack getItem(SlimefunGuideLayout layout) {
        return SlimefunGuide.getItem(layout);
    }

}
