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
import io.github.thebusybiscuit.slimefun4.core.services.github.Contributor;
import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public final class GuideSettings {

    public static final NamespacedKey FIREWORKS_KEY = new NamespacedKey(SlimefunPlugin.instance, "research_fireworks");
    private static final int[] BACKGROUND_SLOTS = {1, 3, 5, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 48, 50, 52, 53};

    private GuideSettings() {}

    public static void openSettings(Player p, ItemStack guide) {
        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocal().getMessage(p, "guide.title.settings"));

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
        menu.addItem(0, new CustomItem(getItem(SlimefunGuideLayout.CHEST), "&e\u21E6 " + SlimefunPlugin.getLocal().getMessage(p, "guide.back.title"), "", "&7" + SlimefunPlugin.getLocal().getMessage(p, "guide.back.guide")),
                (pl, slot, item, action) -> {
                    SlimefunGuide.openGuide(pl, guide);
                    return false;
                });

        menu.addItem(2, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTk1MmQyYjNmMzUxYTZiMDQ4N2NjNTlkYjMxYmY1ZjI2NDExMzNlNWJhMDAwNmIxODU3NmU5OTZhMDI5M2U1MiJ9fX0="),
                        "&c" + SlimefunPlugin.getLocal().getMessage(p, "guide.title.credits"),
                        "",
                        "&7贡献者: &e" + SlimefunPlugin.getGitHubService().getContributors().size(),
                        "",
                        "&7Slimefun 是一个由大型社区维护的开源项目.",
                        "&7在这里你可以找到 TA 们",
                        "",
                        "&7\u21E8 &e单击查看 TA 们"
                ),
                (pl, slot, action, item) -> {
                    openCredits(pl, 0);
                    return false;
                });

        menu.addItem(4, new CustomItem(Material.WRITABLE_BOOK,
                "&aSlimefun 版本",
                "&7&o" + SlimefunPlugin.getLocal().getMessage(p, "guide.tooltips.versions-notice"),
                "",
                "&7Minecraft 版本: &a" + Bukkit.getBukkitVersion(),
                "&7Slimefun 版本: &a" + SlimefunPlugin.getVersion(),
                "&7CS-CoreLib 版本: &a" + CSCoreLib.getLib().getDescription().getVersion()
        ),  ChestMenuUtils.getEmptyClickHandler());

        menu.addItem(6, new CustomItem(Material.COMPARATOR,
                        "&e" + SlimefunPlugin.getLocal().getMessage(p, "guide.title.source"),
                        "",
                        "&7Last Activity: &a" + NumberUtils.timeDelta(SlimefunPlugin.getGitHubService().getLastUpdate()) + " ago",
                        "&7Forks: &e" + SlimefunPlugin.getGitHubService().getForks(),
                        "&7Stars: &e" + SlimefunPlugin.getGitHubService().getStars(),
                        "",
                        "&7&oSlimefun 4 是一个社区型项目,",
                        "&7&o其代码开源在 Github 上",
                        "&7&o如果你想让插件保持更新,",
                        "&7&o请考虑为其做出贡献.",
                        "",
                        "&7\u21E8 &e单击前往 Github 页面"
                ),
                (pl, slot, item, action) -> {
                    pl.closeInventory();
                    ChatUtils.sendURL(pl, "https://github.com/TheBusyBiscuit/Slimefun4");
                    return false;
                });

        menu.addItem(8, new CustomItem(Material.KNOWLEDGE_BOOK,
                        "&3" + SlimefunPlugin.getLocal().getMessage(p, "guide.title.wiki"),
                        "",
                        "&7对物品或机器有问题?",
                        "&7不知道该怎么做?",
                        "&7看看我们社区维护的 Wiki 页面吧",
                        "&7或许你可以为百科编写做出贡献.",
                        "",
                        "&7\u21E8 &e单击前往官方 Slimefun Wiki"
                ),
                (pl, slot, item, action) -> {
                    pl.closeInventory();
                    ChatUtils.sendURL(pl, "https://github.com/TheBusyBiscuit/Slimefun4/wiki");
                    return false;
                });

        menu.addItem(47, new CustomItem(Material.BOOKSHELF,
                        "&3" + SlimefunPlugin.getLocal().getMessage(p, "guide.title.addons"),
                        "",
                        "&7尽管 Slimefun 是一个庞大的插件,",
                        "&7但它的附属插件使它能够真正闪耀.",
                        "&7所以赶紧去看看有什么附属插件吧!",
                        "",
                        "&7服务器安装的附属个数: &b" + SlimefunPlugin.getInstalledAddons().size(),
                        "",
                        "&7\u21E8 &e单击查看 Slimefun4 可用的附属插件"
                ),
                (pl, slot, item, action) -> {
                    pl.closeInventory();
                    ChatUtils.sendURL(pl, "https://github.com/TheBusyBiscuit/Slimefun4/wiki/Addons");
                    return false;
                });

        menu.addItem(49, new CustomItem(Material.REDSTONE_TORCH,
                        "&4" + SlimefunPlugin.getLocal().getMessage(p, "guide.title.bugs"),
                        "",
                        "&7&oBug 反馈必须使用英语!",
                        "",
                        "&7未解决的问题: &a" + SlimefunPlugin.getGitHubService().getIssues(),
                        "&7未合并的合并请求: &a" + SlimefunPlugin.getGitHubService().getPullRequests(),
                        "",
                        "&7\u21E8 &e单击前往 Slimefun4 问题追踪器"
                ),
                (pl, slot, item, action) -> {
                    pl.closeInventory();
                    ChatUtils.sendURL(pl, "https://github.com/TheBusyBiscuit/Slimefun4/issues");
                    return false;
                });

        menu.addItem(51, new CustomItem(Material.TOTEM_OF_UNDYING,
                        "&c敬请期待",
                        "",
                        "&7之后这里会有新东西的..."
                ),
                (pl, slot, item, action) -> {
                    // Add something here
                    return false;
                });
    }

    private static void addConfigurableOptions(Player p, ChestMenu menu, ItemStack guide) {
        int i = 19;

        if (SlimefunManager.isItemSimilar(guide, getItem(SlimefunGuideLayout.CHEST), true)) {
            if (p.hasPermission("slimefun.cheat.items")) {
                menu.addItem(i, new CustomItem(Material.CHEST, "&7指南样式: &e箱子界面", "", "&a箱子界面", "&7书本界面", "&7作弊界面", "", "&7\u21E8 &e单击修改指南样式"));
                menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                    pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.BOOK));
                    openSettings(pl, pl.getInventory().getItemInMainHand());
                    return false;
                });
            }
            else {
                menu.addItem(i, new CustomItem(Material.CHEST, "&7指南样式: &e箱子界面", "", "&a箱子界面", "&7书本界面", "", "&7\u21E8 &e单击修改指南样式"));
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
                menu.addItem(i, new CustomItem(Material.BOOK, "&7指南样式: &e书本界面", "", "&7箱子界面", "&a书本界面", "&7作弊界面", "", "&7\u21E8 &e单击修改指南样式"));
                menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                    pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.CHEAT_SHEET));
                    openSettings(pl, pl.getInventory().getItemInMainHand());
                    return false;
                });
            }
            else {
                menu.addItem(i, new CustomItem(Material.BOOK, "&7指南样式: &e书本界面", "", "&7箱子界面", "&a书本界面", "", "&7\u21E8 &e单击修改指南样式"));
                menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                    pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.CHEST));
                    openSettings(pl, pl.getInventory().getItemInMainHand());
                    return false;
                });
            }

            i++;
        }
        else if (SlimefunManager.isItemSimilar(guide, getItem(SlimefunGuideLayout.CHEAT_SHEET), true)) {
            menu.addItem(i, new CustomItem(Material.COMMAND_BLOCK, "&7指南样式: &e作弊界面", "", "&7箱子界面", "&7书本界面", "&a作弊界面", "", "&7\u21E8 &e单击修改指南样式"));
            menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                pl.getInventory().setItemInMainHand(getItem(SlimefunGuideLayout.CHEST));
                openSettings(pl, pl.getInventory().getItemInMainHand());
                return false;
            });

            i++;
        }

        if (SlimefunPlugin.getSettings().researchFireworksEnabled) {
            if (!PersistentDataAPI.hasByte(p, FIREWORKS_KEY) || PersistentDataAPI.getByte(p, FIREWORKS_KEY) == (byte) 1) {
                menu.addItem(i, new CustomItem(Material.FIREWORK_ROCKET, "&b烟花: &a启用", "", "&7在解锁物品时, 将会燃放一个大烟花", "", "&7\u21E8 &e单击禁用烟花"),
                        (pl, slot, item, action) -> {
                            PersistentDataAPI.setByte(pl, FIREWORKS_KEY, (byte) 0);
                            openSettings(pl, guide);
                            return false;
                        });
            }
            else {
                menu.addItem(i, new CustomItem(Material.FIREWORK_ROCKET, "&b烟花: &4禁用", "", "&7在解锁物品时, 将会燃放一个大烟花", "", "&7\u21E8 &e单击启用烟花"),
                        (pl, slot, item, action) -> {
                            PersistentDataAPI.setByte(pl, FIREWORKS_KEY, (byte) 1);
                            openSettings(pl, guide);
                            return false;
                        });
            }

            i++;
        }

        if (SlimefunPlugin.getSettings().translationsEnabled) {
            Language language = SlimefunPlugin.getLocal().getLanguage(p);
            String languageName = language.isDefault() ? (SlimefunPlugin.getLocal().getMessage(p, "languages.default") + ChatColor.DARK_GRAY + " (" + language.getName(p) + ")"): SlimefunPlugin.getLocal().getMessage(p, "languages." + language.getID());

            menu.addItem(i, new CustomItem(language.getItem(), "&7" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.selected-language") + " &a" + languageName, "", "&7You now have the option to change", "&7the language in which Slimefun", "&7will send you messages.", "&7Note that this only translates", "&7messages, not items.", "", "&7\u21E8 &eClick to change your language"),
                    (pl, slot, item, action) -> {
                        openLanguages(pl);
                        return false;
                    });

//			i++;
        }
    }

    private static void openLanguages(Player p) {
        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocal().getMessage(p, "guide.title.languages"));

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F));

        for (int i = 0; i < 9; i++) {
            if (i == 1) {
                menu.addItem(1, ChestMenuUtils.getBackButton(p, "", "&7" + SlimefunPlugin.getLocal().getMessage(p, "guide.back.settings"))
                        , (pl, slot, item, action) -> {
                            openSettings(pl, p.getInventory().getItemInMainHand());
                            return false;
                        });
            }
            else if (i == 7) {
                menu.addItem(7, new CustomItem(SkullItem.fromHash("3edd20be93520949e6ce789dc4f43efaeb28c717ee6bfcbbe02780142f716"), SlimefunPlugin.getLocal().getMessage(p, "guide.languages.translations.name"), "", "&7\u21E8 &e" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.translations.lore"))
                        , (pl, slot, item, action) -> {
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
        menu.addItem(9, new CustomItem(defaultLanguage.getItem(), ChatColor.GRAY + SlimefunPlugin.getLocal().getMessage(p, "languages.default") + ChatColor.DARK_GRAY + " (" + defaultLanguage.getName(p) + ")", "", "&7\u21E8 &e" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.select-default")),
                (pl, i, item, action) -> {
                    PersistentDataAPI.remove(pl, SlimefunPlugin.getLocal().getKey());

                    String name = SlimefunPlugin.getLocal().getMessage(p, "languages.default");
                    SlimefunPlugin.getLocal().sendMessage(pl, "guide.languages.updated", msg -> msg.replace("%lang%", name));

                    openSettings(pl, p.getInventory().getItemInMainHand());
                    return false;
                });

        int slot = 10;

        for (Language language : SlimefunPlugin.getLocal().getLanguages()) {
            menu.addItem(slot, new CustomItem(language.getItem(),
                    ChatColor.GREEN + language.getName(p),
                    "&7" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.progress.messages") + ": &b" + SlimefunPlugin.getLocal().getProgress(language, Language::getMessages) + '%',
                    "&7" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.progress.researches") + ": &b" + SlimefunPlugin.getLocal().getProgress(language, Language::getResearches) + '%',
                    "",
                    "&7\u21E8 &e" + SlimefunPlugin.getLocal().getMessage(p, "guide.languages.select")
            ), (pl, i, item, action) -> {
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

        for (int i = 0; i < 9; i++) {
            if (i != 1) {
                menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
            }
            else {
                menu.addItem(1, new CustomItem(ChestMenuUtils.getBackButton(p, "", "&7" + SlimefunPlugin.getLocal().getMessage(p, "guide.back.settings"))));
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
            ItemStack skull = SkullItem.fromBase64(contributor.getTexture());

            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setDisplayName(contributor.getDisplayName());

            List<String> lore = new LinkedList<>();
            lore.add("");

            for (Map.Entry<String, Integer> entry : contributor.getContributions()) {
                String info = entry.getKey();

                if (!info.startsWith("&")) {
                    String[] segments = info.split(",");
                    info = SlimefunPlugin.getLocal().getMessage(p, "guide.credits.roles." + segments[0]);

                    if (segments.length == 2) {
                        info += " &7(" + SlimefunPlugin.getLocal().getMessage(p, "languages." + segments[1]) + ")";
                    }
                }

                if (entry.getValue() > 0) {
                    String commits = SlimefunPlugin.getLocal().getMessage(p, "guide.credits." + (entry.getValue() > 1 ? "commits": "commit"));

                    info += " &7(" + entry.getValue() + " " + commits + ")";
                }

                lore.add(ChatColors.color(info));
            }

            if (contributor.getProfile() != null) {
                lore.add("");
                lore.add(ChatColors.color("&7\u21E8 &e") + SlimefunPlugin.getLocal().getMessage(p, "guide.credits.profile-link"));
            }

            meta.setLore(lore);
            skull.setItemMeta(meta);

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

    private static ItemStack getItem(SlimefunGuideLayout layout) {
        return SlimefunGuide.getItem(layout);
    }

}