package me.mrCookieSlime.Slimefun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import me.mrCookieSlime.Slimefun.Objects.SeasonalCategory;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.CSCoreLibPlugin.PlayerRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.HoverAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.CustomBookOverlay;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.SkullItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.hooks.github.Contributor;
import me.mrCookieSlime.Slimefun.hooks.github.IntegerFormat;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Misc.BookDesign;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunGadget;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AReactor;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.GuideHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public final class SlimefunGuide {

    private SlimefunGuide(){}

	public static int month = 0;

	public static int issues = 0;
	public static int forks = 0;
	/**
	 * Represents the number of stars on the Slimefun4 GitHub repository.
	 * @since 4.1.13
	 */
	public static int stars = 0;
	public static int codeBytes = 0;
	public static Date lastUpdate = new Date();

	private static final int category_size = 36;

	@Deprecated
	public static ItemStack getItem() {
		return getItem(BookDesign.CHEST);
	}

	public static ItemStack getItem(BookDesign design) {
		switch (design) {
		case BOOK:
			return new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&a粘液科技指南 &7(书与笔界面)", "", "&e右键 &8\u21E8 &7浏览物品", "&eShift + 右键 &8\u21E8 &7打开设置菜单");
		case CHEAT_SHEET:
			return new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&c粘液科技指南 &4(作弊模式)", "", "&4&l仅限管理员使用", "", "&e右键 &8\u21E8 &7浏览物品", "&eShift + 右键 &8\u21E8 &7打开设置菜单");
		case CHEST:
			return new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&a粘液科技指南 &7(箱子界面)", "", "&e右键 &8\u21E8 &7浏览物品", "&eShift + 右键 &8\u21E8 &7打开设置菜单");
		default:
			return null;
		}
	}

	@Deprecated
	public static ItemStack getItem(boolean book) {
		return getItem(book ? BookDesign.BOOK: BookDesign.CHEST);
	}

	@Deprecated
	public static ItemStack getDeprecatedItem(boolean book) {
		return new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&e粘液科技指南 &7(右键打开)", (book ? "": "&2"), "&r这是粘液科技的基础指南", "&r指南内可以查看粘液科技的所有物品", "&r以及扩展的物品和更多信息");
	}

	private static final int[] slots = new int[] {0, 2, 3, 5, 6, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};

	public static void openSettings(Player p, final ItemStack guide) {
		final ChestMenu menu = new ChestMenu("设置 / 关于");

		menu.setEmptySlotsClickable(false);
		menu.addMenuOpeningHandler(p1 -> p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F));

		for (int i: slots) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i, (arg0, arg1, arg2, arg3) -> false);
		}


		if (SlimefunManager.isItemSimiliar(guide, getItem(BookDesign.CHEST), true)) {
			if (p.hasPermission("slimefun.cheat.items")) {
				menu.addItem(19, new CustomItem(new ItemStack(Material.CHEST), "&7指南样式: &e箱子界面", "", "&a箱子界面", "&7书与笔界面", "&7作弊模式", "", "&e 单击 &8\u21E8 &7修改样式"));
				menu.addMenuClickHandler(19, (p12, arg1, arg2, arg3) -> {
                    p12.getInventory().setItemInMainHand(getItem(BookDesign.BOOK));
                    openSettings(p12, p12.getInventory().getItemInMainHand());
                    return false;
                });
			}
			else {
				menu.addItem(19, new CustomItem(new ItemStack(Material.CHEST), "&7指南样式: &e箱子界面", "", "&a箱子界面", "&7书与笔界面", "", "&e 单击 &8\u21E8 &7修改样式"));
				menu.addMenuClickHandler(19, new MenuClickHandler() {

					@Override
					public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
						p.getInventory().setItemInMainHand(getItem(BookDesign.BOOK));
						openSettings(p, p.getInventory().getItemInMainHand());
						return false;
					}
				});
			}
		}
		else if (SlimefunManager.isItemSimiliar(guide, getItem(BookDesign.BOOK), true)) {
			if (p.hasPermission("slimefun.cheat.items")) {
				menu.addItem(19, new CustomItem(new ItemStack(Material.CHEST), "&7指南样式: &e书与笔界面", "", "&7箱子界面", "&a书与笔界面", "&7作弊模式", "", "&e 单击 &8\u21E8 &7修改样式"));
				menu.addMenuClickHandler(19, new MenuClickHandler() {

					@Override
					public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
						p.getInventory().setItemInMainHand(getItem(BookDesign.CHEAT_SHEET));
						openSettings(p, p.getInventory().getItemInMainHand());
						return false;
					}
				});
			}
			else {
				menu.addItem(19, new CustomItem(new ItemStack(Material.CHEST), "&7指南样式: &e书与笔界面", "", "&7箱子界面", "&a书与笔界面", "", "&e 单击 &8\u21E8 &7修改样式"));
				menu.addMenuClickHandler(19, new MenuClickHandler() {

					@Override
					public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
						p.getInventory().setItemInMainHand(getItem(BookDesign.CHEST));
						openSettings(p, p.getInventory().getItemInMainHand());
						return false;
					}
				});
			}
		}
		else if (SlimefunManager.isItemSimiliar(guide, getItem(BookDesign.CHEAT_SHEET), true)) {
			menu.addItem(19, new CustomItem(new ItemStack(Material.CHEST), "&7指南样式: &e作弊模式", "", "&7箱子界面", "&7书与笔界面", "&a作弊模式", "", "&e 单击 &8\u21E8 &7修改样式"));
			menu.addMenuClickHandler(19, new MenuClickHandler() {

				@Override
				public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
					p.getInventory().setItemInMainHand(getItem(BookDesign.CHEST));
					openSettings(p, p.getInventory().getItemInMainHand());
					return false;
				}
			});
		}

		menu.addItem(1, new CustomItem(new ItemStack(Material.WRITABLE_BOOK), "&a制作人员", "", "&7目前版本: &a" + SlimefunStartup.instance.getDescription().getVersion(), "&7贡献者: &e" + SlimefunStartup.instance.getUtilities().contributors.size(), "", "&7\u21E8 单击查看在插件背后工作的人们"));
		menu.addMenuClickHandler(1, (p14, arg1, arg2, arg3) -> {
            openCredits(p14, guide);
            return false;
        });

		try {
			menu.addItem(4, new CustomItem(new ItemStack(Material.COMPARATOR), "&e源码", "&7代码大小: &6" + IntegerFormat.formatBigNumber(codeBytes), "&7上次更新于: &a" + IntegerFormat.timeDelta(lastUpdate) + " 前", "&7分支: &e" + forks, "&7点赞数: &e" + stars, "", "&7&oSlimefun 4 是一个社区性的项目,", "&7&o其源码公开于 Github 上", "&7&o如果你有 Java 编程经验,", "&7&o请考虑为此项目做出贡献", "", "&7\u21E8 单击前往 Github 页面"));
			menu.addMenuClickHandler(4, (p13, arg1, arg2, arg3) -> {
                p13.closeInventory();
                p13.sendMessage("");
                p13.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&ohttps://github.com/TheBusyBiscuit/Slimefun4"));
                p13.sendMessage("");
                return false;
            });
		} catch (Exception e) {
			e.printStackTrace();
		}

		menu.addItem(7, new CustomItem(new ItemStack(Material.REDSTONE), "&4问题追踪器", "", "&7未解决的问题: &a" + issues, "", "&7\u21E8 单击前往 Slimefun 问题追踪器"));
		menu.addMenuClickHandler(7, (p15, arg1, arg2, arg3) -> {
            p15.closeInventory();
            p15.sendMessage("");
            p15.sendMessage("&7&o单击下方链接打开:");
            p15.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&ohttps://github.com/TheBusyBiscuit/Slimefun4/issues"));
            p15.sendMessage("");
            return false;
        });

		menu.open(p);
	}

	public static void openCredits(Player p, final ItemStack guide) {
		final ChestMenu menu = new ChestMenu("Credits");

		menu.setEmptySlotsClickable(false);
		menu.addMenuOpeningHandler(p1 -> p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.7F, 0.7F));

		for (int i = 0; i < 9; i++) {
			if (i != 4) {
				menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
				menu.addMenuClickHandler(i, (arg0, arg1, arg2, arg3) -> false);
			}
			else {
				menu.addItem(4, new CustomItem(new ItemStack(Material.EMERALD), "&7\u21E6 返回至设置"));
				menu.addMenuClickHandler(4, (p12, arg1, arg2, arg3) -> {
                    openSettings(p12, guide);
                    return false;
                });
			}
		}

		int index = 9;

		double total = 0;

		for (Contributor contributor: SlimefunStartup.instance.getUtilities().contributors) {
			total += contributor.getCommits();
		}

		for (final Contributor contributor: SlimefunStartup.instance.getUtilities().contributors) {
			ItemStack skull = new SkullItem("&a" + contributor.getName(), contributor.getName());

			ItemMeta meta = skull.getItemMeta();

			if (contributor.getCommits() > 0) {
				double percentage = DoubleHandler.fixDouble((contributor.getCommits() * 100.0) / total, 2);

				meta.setLore(Arrays.asList("", ChatColor.translateAlternateColorCodes('&', "&7角色: &r" + contributor.getJob()), ChatColor.translateAlternateColorCodes('&', "&7Contributions: &r" + contributor.getCommits() + " commits &7(&r" + percentage + "%&7)"), "", ChatColor.translateAlternateColorCodes('&', "&7\u21E8 单击查看我的 Github 个人页面")));
			}
			else {
				meta.setLore(Arrays.asList("", ChatColor.translateAlternateColorCodes('&', "&7角色: &r" + contributor.getJob())));
			}

			skull.setItemMeta(meta);

			menu.addItem(index, skull);
			menu.addMenuClickHandler(index, (p13, arg1, arg2, arg3) -> {
                if (contributor.getCommits() > 0) {
                    p13.closeInventory();
                    p13.sendMessage("");
                    p13.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&o" + contributor.getProfile()));
                    p13.sendMessage("");
                }
                return false;
            });

			index++;
		}

		for (int i = 0; i < 9; i++) {
			menu.addItem(36 + i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(36 + i, (arg0, arg1, arg2, arg3) -> false);
		}

		menu.open(p);
	}

	public static void openCheatMenu(Player p) {
		openMainMenu(p, false, false, 1);
	}

	public static void openGuide(Player p, boolean book) {
		if (!SlimefunStartup.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled")) return;
		if (!SlimefunStartup.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled-items.SLIMEFUN_GUIDE")) return;
		if (!getHistory().containsKey(p.getUniqueId())) openMainMenu(p, true, book, 1);
		else {
            Object last = getLastEntry(p, false);
            if (last instanceof Category) openCategory(p, (Category) last, true, 1, book);
            else if (last instanceof SlimefunItem) displayItem(p, ((SlimefunItem) last).getItem(), false, book, 0);
            else if (last instanceof GuideHandler) ((GuideHandler) last).run(p, true, book);
            else displayItem(p, (ItemStack) last, false, book, 0);
		}
	}

	public static void openMainMenu(final Player p, final boolean survival, final boolean book, final int selected_page) {
		clearHistory(p.getUniqueId());

        if (survival)
            clearHistory(p.getUniqueId());

		if (book) {
			List<TellRawMessage> pages = new ArrayList<>();
			List<String> texts = new ArrayList<>();
			List<String> tooltips = new ArrayList<>();
			List<PlayerRunnable> actions = new ArrayList<>();

			int tier = 0;

			for (final Category category: Category.list()) {

				boolean locked = true;

				for (SlimefunItem item: category.getItems()) {
					if (Slimefun.isEnabled(p, item, false)) {
						locked = false;
						break;
					}
				}

				if (locked) {
					// Dont display that Category...
				}
				else {
					if (tier < category.getTier()) {
						if (survival) {
							for (final GuideHandler handler: Slimefun.getGuideHandlers(tier)) {
								handler.addEntry(texts, tooltips);
								actions.add(new PlayerRunnable(2) {

									@Override
									public void run(Player p) {
										handler.run(p, survival, book);
									}
								});
							}
						}
						tier = category.getTier();
						if (tier > 1) {
							for (int i = 0; i < 10; i++) {
								if (texts.size() % 10 == 0) break;
								texts.add(" ");
								tooltips.add(null);
								actions.add(null);
							}
						}
						texts.add(ChatColor.translateAlternateColorCodes('&', "&8\u21E8 &6等级 " + tier));
						tooltips.add(null);
						actions.add(null);
					}
					if (category instanceof LockedCategory && !((LockedCategory) category).hasUnlocked(p)) {
						StringBuilder parents = new StringBuilder(ChatColor.translateAlternateColorCodes('&', "&4&l已锁定\n\n&7为了解锁这一类别,\n&7你需要先解锁以下类别中\n&7的所有物品:\n"));

						for (Category parent: ((LockedCategory) category).getParents()) {
							parents.append(ChatColor.translateAlternateColorCodes('&', "\n&c" + StringUtils.formatItemName(parent.getItem(), false)));
						}

						texts.add(ChatColor.translateAlternateColorCodes('&', shorten("&c" , StringUtils.formatItemName(category.getItem(), false))));
						tooltips.add(parents.toString());
						actions.add(null);
					}
					else if (category instanceof SeasonalCategory) {
						if (((SeasonalCategory) category).isUnlocked()) {
							texts.add(ChatColor.translateAlternateColorCodes('&', shorten("&a", StringUtils.formatItemName(category.getItem(), false))));
							tooltips.add(ChatColor.translateAlternateColorCodes('&', "&e单击打开以下类别: \n" + StringUtils.formatItemName(category.getItem(), false)));
							actions.add(new PlayerRunnable(1) {

								@Override
								public void run(final Player p) {
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, () -> {
                                        openCategory(p, category, survival, 1, book);
									}, 1L);
								}
							});
						}
					}
					else {
						texts.add(ChatColor.translateAlternateColorCodes('&', shorten("&a", StringUtils.formatItemName(category.getItem(), false))));
						tooltips.add(ChatColor.translateAlternateColorCodes('&', "&e单击打开以下类别:\n" + StringUtils.formatItemName(category.getItem(), false)));
						actions.add(new PlayerRunnable(1) {

							@Override
							public void run(final Player p) {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, () -> {
                                    openCategory(p, category, survival, 1, book);
								}, 1L);
							}
						});
					}
				}
			}

			if (survival) {
				for (final GuideHandler handler: Slimefun.getGuideHandlers(tier)) {
					handler.addEntry(texts, tooltips);
					actions.add(new PlayerRunnable(2) {

						@Override
						public void run(Player p) {
							handler.run(p, survival, book);
						}
					});
				}
			}

			for (int i = 0; i < texts.size(); i = i + 10) {
				TellRawMessage page = new TellRawMessage();
				page.addText(ChatColor.translateAlternateColorCodes('&', "&b&l- 粘液科技指南 -\n\n"));
				for (int j = i; j < texts.size() && j < i + 10; j++) {
					page.addText(texts.get(j) + "\n");
					if (tooltips.get(j) != null) page.addHoverEvent(HoverAction.SHOW_TEXT, tooltips.get(j));
					if (actions.get(j) != null) page.addClickEvent(actions.get(j));
				}
//				page.addText("\n");
//				if (i > 0) {
//					page.addText(ChatColor.translateAlternateColorCodes('&', "&c<- 上一页"));
//					page.addHoverEvent(HoverAction.SHOW_TEXT, ChatColor.translateAlternateColorCodes('&', "&e翻页至 " + (i)));
//					page.addClickEvent(me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.ClickAction.CHANGE_PAGE, String.valueOf(i));
//					page.addText("    ");
//				}
//				if (texts.size() > i * 10) {
//					page.addText("    ");
//					page.addText(ChatColor.translateAlternateColorCodes('&', "&c下一页 ->"));
//					page.addHoverEvent(HoverAction.SHOW_TEXT, ChatColor.translateAlternateColorCodes('&', "&e翻页至 " + (i + 2)));
//					page.addClickEvent(me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.ClickAction.CHANGE_PAGE, String.valueOf(i + 2));
//				}
				pages.add(page);
			}

			new CustomBookOverlay("粘液科技指南", "mrCookieSlime", pages.toArray(new TellRawMessage[pages.size()])).open(p);
		}
		else {
			final ChestMenu menu = new ChestMenu("粘液科技指南");

			menu.setEmptySlotsClickable(false);
			menu.addMenuOpeningHandler(p1 -> p1.playSound(p1.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.7F, 0.7F));

			List<Category> categories = Slimefun.currentCategories;
            List<GuideHandler> handlers = Slimefun.guideHandlers.values().stream().flatMap(List::stream).collect(Collectors.toList());

			int index = 9;
			int pages = 1;

			for (int i = 0; i < 9; i++) {
				menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
				menu.addMenuClickHandler(i, (arg0, arg1, arg2, arg3) -> false);
			}

			for (int i = 45; i < 54; i++) {
				menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
				menu.addMenuClickHandler(i, (arg0, arg1, arg2, arg3) -> false);
			}

			int target = (category_size * (selected_page - 1)) - 1;

			while (target < (categories.size() + handlers.size() - 1)) {
				if (index >= category_size + 9) {
					pages++;
					break;
				}

				target++;

				if (target >= categories.size()) {
					if (!survival) break;
					index = handlers.get(target - categories.size()).next(p, index, menu);
				}
				else {
					final Category category = categories.get(target);

					boolean locked = true;

					for (SlimefunItem item: category.getItems()) {
						if (Slimefun.isEnabled(p, item, false)) {
							locked = false;
							break;
						}
					}

					if (locked) {
						// Dont display that Category...
					}
					else if (!(category instanceof LockedCategory)) {
						if (!(category instanceof SeasonalCategory)) {
							menu.addItem(index, category.getItem());
							menu.addMenuClickHandler(index, (p12, slot, item, action) -> {
                                openCategory(p12, category, survival, 1, book);
                                return false;
                            });
							index++;
						}
						else {
							if (((SeasonalCategory) category).isUnlocked()) {
								menu.addItem(index, category.getItem());
								menu.addMenuClickHandler(index, (p14, slot, item, action) -> {
                                    openCategory(p14, category, survival, 1, book);
                                    return false;
                                });
								index++;
							}
						}
					}
					else if (((LockedCategory) category).hasUnlocked(p)) {
						menu.addItem(index, category.getItem());
						menu.addMenuClickHandler(index, (p13, slot, item, action) -> {
                            openCategory(p13, category, survival, 1, book);
                            return false;
                        });
						index++;
					}
					else {
						List<String> parents = new ArrayList<String>();
						parents.add("");
						parents.add(ChatColor.translateAlternateColorCodes('&', "&r你需要先解锁以下类别中"));
						parents.add(ChatColor.translateAlternateColorCodes('&', "&r的所有物品:"));
						parents.add("");
						for (Category parent: ((LockedCategory) category).getParents()) {
							parents.add(parent.getItem().getItemMeta().getDisplayName());
						}
						menu.addItem(index, new CustomItem(Material.BARRIER, "&4已锁定 &7- &r" + category.getItem().getItemMeta().getDisplayName(), 0, parents.toArray(new String[parents.size()])));
						menu.addMenuClickHandler(index, (arg0, arg1, arg2, arg3) -> false);
						index++;
					}
				}
			}

			final int finalPages = pages;

			menu.addItem(46, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r\u21E6 上一页", "", "&7(" + selected_page + " / " + pages + ")"));
			menu.addMenuClickHandler(46, (arg0, arg1, arg2, arg3) -> {
                int next = selected_page - 1;
                if (next < 1) next = finalPages;
                if (next != selected_page) openMainMenu(p, survival, book, next);
                return false;
            });

			menu.addItem(52, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r下一页 \u21E8", "", "&7(" + selected_page + " / " + pages + ")"));
			menu.addMenuClickHandler(52, (arg0, arg1, arg2, arg3) -> {
                int next = selected_page + 1;
                if (next > finalPages) next = 1;
                if (next != selected_page) openMainMenu(p, survival, book, next);
                return false;
            });

			menu.open(p);
		}
	}

	public static String shorten(String string, String string2) {
		if (ChatColor.stripColor(string + string2).length() > 19) return (string + ChatColor.stripColor(string2)).substring(0, 18) + "...";
		else return (string + ChatColor.stripColor(string2));
	}

	public static void openCategory(final Player p, final Category category, final boolean survival, final int selected_page, final boolean book) {
		if (category == null) return;

		if (book && category.getItems().size() < 250) {
			List<TellRawMessage> pages = new ArrayList<>();
			List<String> texts = new ArrayList<>();
			List<String> tooltips = new ArrayList<>();
			List<PlayerRunnable> actions = new ArrayList<>();

			for (final SlimefunItem item: category.getItems()) {
				if (Slimefun.hasPermission(p, item, false)) {
					if (Slimefun.isEnabled(p, item, false)) {
						if (survival && !Slimefun.hasUnlocked(p, item, false) && item.getResearch() != null) {
						    final Research research = item.getResearch();

							texts.add(ChatColor.translateAlternateColorCodes('&', shorten("&7", StringUtils.formatItemName(item.getItem(), false))));
							tooltips.add(ChatColor.translateAlternateColorCodes('&', StringUtils.formatItemName(item.getItem(), false) + "\n&c&l已锁定\n\n&7需要经验 " + (p.getLevel() >= research.getCost() ? "&b": "&4") + research.getCost() + " 级\n\n&a> 单击解锁"));
							actions.add(new PlayerRunnable(2) {

								@Override
								public void run(final Player p) {
									if (!Research.isResearching(p)) {
										if (research.canUnlock(p)) {
                                            PlayerProfile profile = PlayerProfile.fromUUID(p.getUniqueId());
                                            if (profile.hasUnlocked(research)) {
                                                openCategory(p, category, true, selected_page, book);
                                            }
											else {
												if (!(p.getGameMode() == GameMode.CREATIVE && SlimefunStartup.instance.getSettings().RESEARCHES_FREE_IN_CREATIVE)) {
													p.setLevel(p.getLevel() - research.getCost());
												}

												if (p.getGameMode() == GameMode.CREATIVE) {
													research.unlock(p, true);
                                                    Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, () -> {
                                                        openCategory(p, category, survival, selected_page, book);
													}, 1L);
												} else {
													research.unlock(p, false);
                                                    Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, () -> {
                                                        openCategory(p, category, survival, selected_page, book);
													}, 103L);
												}
											}
										} else Messages.local.sendTranslation(p, "messages.not-enough-xp", true);
									}
								}
							});
						}
						else {
							texts.add(ChatColor.translateAlternateColorCodes('&', shorten("&a", StringUtils.formatItemName(item.getItem(), false))));

							StringBuilder tooltip = new StringBuilder();

							tooltip.append(StringUtils.formatItemName(item.getItem(), false));

							if (item.getItem().hasItemMeta() && item.getItem().getItemMeta().hasLore()) {
								for (String line: item.getItem().getItemMeta().getLore()) {
									tooltip.append("\n" + line);
								}
							}

							tooltip.append(ChatColor.translateAlternateColorCodes('&', "\n\n&e&o单击查看更多信息"));

							tooltips.add(tooltip.toString());
							actions.add(new PlayerRunnable(2) {

								@Override
								public void run(Player p) {
									displayItem(p, item.getItem(), true, book, 0);
								}
							});
						}
					}
				}
				else {
					texts.add(ChatColor.translateAlternateColorCodes('&', shorten("&4", StringUtils.formatItemName(item.getItem(), false))));
					tooltips.add(ChatColor.translateAlternateColorCodes('&', "&c没有权限!"));
					actions.add(null);
				}
			}

			for (int i = 0; i < texts.size(); i = i + 10) {
				TellRawMessage page = new TellRawMessage();
				page.addText(ChatColor.translateAlternateColorCodes('&', "&b&l- 粘液科技指南 -\n\n"));
				for (int j = i; j < texts.size() && j < i + 10; j++) {
					page.addText(texts.get(j) + "\n");
					if (tooltips.get(j) != null) page.addHoverEvent(HoverAction.SHOW_TEXT, tooltips.get(j));
					if (actions.get(j) != null) page.addClickEvent(actions.get(j));
				}
				page.addText("\n");
				page.addText(ChatColor.translateAlternateColorCodes('&', "&6\u21E6 &lBack"));
				page.addHoverEvent(HoverAction.SHOW_TEXT, ChatColor.translateAlternateColorCodes('&', "&e单击返回此类别概览"));
				page.addClickEvent(new PlayerRunnable(2) {

					@Override
					public void run(final Player p) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, () -> openMainMenu(p, survival, true, 1), 1L);
					}
				});
				pages.add(page);
			}

			new CustomBookOverlay("粘液科技指南", "mrCookieSlime", pages.toArray(new TellRawMessage[0])).open(p);
		}
		else {
			final ChestMenu menu = new ChestMenu("粘液科技指南");

			menu.setEmptySlotsClickable(false);
			menu.addMenuOpeningHandler(p12 -> p12.playSound(p12.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.7F, 0.7F));

			int index = 9;
			final int pages = (category.getItems().size() - 1) / category_size + 1;
			for (int i = 0; i < 4; i++) {
				menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
				menu.addMenuClickHandler(i, (arg0, arg1, arg2, arg3) -> false);
			}

			menu.addItem(4, new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&7\u21E6 返回"));
			menu.addMenuClickHandler(4, (arg0, arg1, arg2, arg3) -> {
                openMainMenu(p, survival, book, 1);
                return false;
            });

			for (int i = 5; i < 9; i++) {
				menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
				menu.addMenuClickHandler(i, (arg0, arg1, arg2, arg3) -> false);
			}

			for (int i = 45; i < 54; i++) {
				menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
				menu.addMenuClickHandler(i, (arg0, arg1, arg2, arg3) -> false);
			}

			menu.addItem(46, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r\u21E6 上一页", "", "&7(" + selected_page + " / " + pages + ")"));
			menu.addMenuClickHandler(46, (arg0, arg1, arg2, arg3) -> {
                int next = selected_page - 1;
                if (next < 1) next = pages;
                if (next != selected_page) openCategory(p, category, survival, next, book);
                return false;
            });

			menu.addItem(52, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r下一页 \u21E8", "", "&7(" + selected_page + " / " + pages + ")"));
			menu.addMenuClickHandler(52, (arg0, arg1, arg2, arg3) -> {
                int next = selected_page + 1;
                if (next > pages) next = 1;
                if (next != selected_page) openCategory(p, category, survival, next, book);
                return false;
            });

			int categoryIndex = category_size * (selected_page - 1);
			for (int i = 0; i < category_size; i++) {
				int target = categoryIndex + i;
				if (target >= category.getItems().size()) break;
				final SlimefunItem sfitem = category.getItems().get(target);
				if (Slimefun.isEnabled(p, sfitem, false)) {
					if (survival && !Slimefun.hasUnlocked(p, sfitem.getItem(), false) && sfitem.getResearch() != null) {
						if (Slimefun.hasPermission(p, sfitem, false)) {
						    final Research research = sfitem.getResearch();
							menu.addItem(index, new CustomItem(Material.BARRIER, "&r" + StringUtils.formatItemName(sfitem.getItem(), false), 0, new String[] {"&4&l已锁定", "", "&a> 单击解锁", "", "&7需要经验: &b" + research.getCost() + "级"}));
                            menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                                if (!Research.isResearching(pl)) {
                                    if (research.canUnlock(pl)) {
                                        PlayerProfile profile = PlayerProfile.fromUUID(p.getUniqueId());

                                        if (profile.hasUnlocked(research)) {
                                            openCategory(p, category, true, selected_page, book);
                                        }
                                        else {
                                            if (!(pl.getGameMode() == GameMode.CREATIVE && SlimefunStartup.instance.getSettings().RESEARCHES_FREE_IN_CREATIVE)) {
                                                pl.setLevel(pl.getLevel() - research.getCost());
                                            }

                                            if (pl.getGameMode() == GameMode.CREATIVE) {
                                                research.unlock(pl, SlimefunStartup.instance.getSettings().RESEARCHES_FREE_IN_CREATIVE);
                                                openCategory(pl, category, survival, selected_page, book);
                                            }
                                            else {
                                                research.unlock(pl, false);
                                                Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, () -> {
                                                    openCategory(pl, category, survival, selected_page, book);
                                                }, 103L);
                                            }
                                        }
                                    } else Messages.local.sendTranslation(pl, "messages.not-enough-xp", true);
                                }
                                return false;
                            });
							index++;
						}
						else {
							menu.addItem(index, new CustomItem(Material.BARRIER, StringUtils.formatItemName(sfitem.getItem(), false), 0, new String[] {"", "&r你没有权限", "&r与此物品交互"}));
							menu.addMenuClickHandler(index, (arg0, arg1, arg2, arg3) -> false);
							index++;
						}
					}
					else {
						menu.addItem(index, sfitem.getItem());
						menu.addMenuClickHandler(index, (p1, slot, item, action) -> {
                            if (survival) displayItem(p1, item, true, book, 0);
                            else p1.getInventory().addItem(item);
                            return false;
                        });
						index++;
					}
				}
			}

			menu.open(p);
		}

		if (survival) {
            addToHistory(p, category);
		}
	}

	public static void addToHistory(Player p, Object obj) {
		List<Object> list = new ArrayList<>();
		if (getHistory().containsKey(p.getUniqueId())) list = getHistory().get(p.getUniqueId());
		list.add(obj);
		getHistory().put(p.getUniqueId(), list);
	}

	private static Object getLastEntry(Player p, boolean remove) {
		List<Object> list = new ArrayList<>();
		if (getHistory().containsKey(p.getUniqueId())) list = getHistory().get(p.getUniqueId());
        if (remove && !list.isEmpty()) {
            Object obj = list.get(list.size() - 1);
            list.remove(obj);
		}
		if (list.isEmpty()) getHistory().remove(p.getUniqueId());
		else getHistory().put(p.getUniqueId(), list);
		return list.isEmpty() ? null: list.get(list.size() - 1);
	}

	@SuppressWarnings("deprecation")
	public static void displayItem(Player p, final ItemStack item, boolean addToHistory, final boolean book, final int page) {
		if (item == null || item.getType() == Material.AIR) return;

		final SlimefunItem sfItem = SlimefunItem.getByItem(item);
        if (sfItem == null && !SlimefunStartup.instance.getSettings().GUIDE_SHOW_VANILLA_RECIPES) return;

		ItemStack[] recipe = new ItemStack[9];
		ItemStack recipeType = null;
		ItemStack recipeOutput = item;

		ChestMenu menu = new ChestMenu("粘液科技指南");

		menu.setEmptySlotsClickable(false);
		menu.addMenuOpeningHandler(p1 -> p1.playSound(p1.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.7F, 0.7F));

		if (sfItem != null) {
			recipe = sfItem.getRecipe();
			recipeType = sfItem.getRecipeType().toItem();
			recipeOutput = sfItem.getRecipeOutput() != null ? sfItem.getRecipeOutput(): sfItem.getItem();
		}
		else {
			List<Recipe> recipes = new ArrayList<Recipe>();
			Iterator<Recipe> iterator = Bukkit.recipeIterator();
			while (iterator.hasNext()) {
				Recipe r = iterator.next();
                if (SlimefunManager.isItemSimiliar(new CustomItem(r.getResult(), 1), item, true)) recipes.add(r);
			}

			if (recipes.isEmpty()) return;
			 Recipe r = recipes.get(page);

			 if (recipes.size() > page + 1) {
				 menu.addItem(1, new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&7Next \u21E8", "", "&e&l! &r此物品有多种合成方式"));
					menu.addMenuClickHandler(1, (p12, slot, stack, action) -> {
                        displayItem(p12, item, false, book, page + 1);
                        return false;
                    });
			 }

			 if (r instanceof ShapedRecipe) {
					String[] shape = ((ShapedRecipe) r).getShape();
					for (int i = 0; i < shape.length; i++) {
			            for (int j = 0; j < shape[i].length(); j++) {
                            recipe[i * 3 + j] = ((ShapedRecipe) r).getIngredientMap().get(shape[i].charAt(j));
			            }
			        }
					recipeType = RecipeType.SHAPED_RECIPE.toItem();
					recipeOutput = r.getResult();
				}
				else if (r instanceof ShapelessRecipe) {
			        List<ItemStack> ingredients = ((ShapelessRecipe) r).getIngredientList();
					for (int i = 0; i < ingredients.size(); i++) {
                        recipe[i] = ingredients.get(i);
			        }
					recipeType = RecipeType.SHAPELESS_RECIPE.toItem();
					recipeOutput = r.getResult();
				}
				else if (r instanceof FurnaceRecipe) {
					ItemStack ingredient = ((FurnaceRecipe) r).getInput();
					recipe[4] = ((FurnaceRecipe) r).getInput();

					recipeType = RecipeType.FURNACE.toItem();
					recipeOutput = r.getResult();
				}
		}



        if (addToHistory) addToHistory(p, sfItem != null ? sfItem: item);

		if (getHistory().containsKey(p.getUniqueId()) && getHistory().get(p.getUniqueId()).size() > 1) {
			menu.addItem(0, new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&7\u21E6 返回", "", "&r左键: &7返回上一页", "&rShift +左键: &7返回主菜单"));
			menu.addMenuClickHandler(0, (p13, slot, item1, action) -> {
                if (action.isShiftClicked()) openMainMenu(p13, true, book, 1);
                else {
                    Object last = getLastEntry(p13, true);
                    if (last instanceof Category) openCategory(p13, (Category) last, true, 1, book);
                    else if (last instanceof SlimefunItem) displayItem(p13, ((SlimefunItem) last).getItem(), false, book, 0);
                    else if (last instanceof GuideHandler) ((GuideHandler) last).run(p13, true, book);
                    else displayItem(p13, (ItemStack) last, false, book, 0);
                }
                return false;
            });
		}
		else {
			menu.addItem(0, new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&7\u21E6 返回", "", "&r左键: &7返回主菜单"));
			menu.addMenuClickHandler(0, (p14, slot, item12, action) -> {
                openMainMenu(p14, true, book, 1);
                return false;
            });
		}

		menu.addItem(3, Slimefun.hasUnlocked(p, recipe[0], false) ? recipe[0]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[0], false), 0, new String[] {"&4&l已锁定", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[0]), false) ? "&r需要在其他地方解锁" : "&r无权限"}));
		menu.addMenuClickHandler(3, (p15, slot, item13, action) -> {
            displayItem(p15, item13, true, book, 0);
            return false;
        });

		menu.addItem(4, Slimefun.hasUnlocked(p, recipe[1], false) ? recipe[1]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[1], false), 0, new String[] {"&4&l已锁定", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[1]), false) ? "&r需要在其他地方解锁" : "&r无权限"}));
		menu.addMenuClickHandler(4, (p16, slot, item14, action) -> {
            displayItem(p16, item14, true, book, 0);
            return false;
        });

		menu.addItem(5, Slimefun.hasUnlocked(p, recipe[2], false) ? recipe[2]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[2], false), 0, new String[] {"&4&l已锁定", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[2]), false) ? "&r需要在其他地方解锁" : "&r无权限"}));
		menu.addMenuClickHandler(5, (p17, slot, item15, action) -> {
            displayItem(p17, item15, true, book, 0);
            return false;
        });

		if (sfItem != null) {
			if (Slimefun.getItemConfig().contains(sfItem.getID() + ".wiki")) {
				try {
					menu.addItem(8, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY2OTJmOTljYzZkNzgyNDIzMDQxMTA1NTM1ODk0ODQyOThiMmU0YTAyMzNiNzY3NTNmODg4ZTIwN2VmNSJ9fX0="), "&r单击查看此物品的Wiki页面 &7(Slimefun Wiki)", "", "&7\u21E8 单击打开"));
					menu.addMenuClickHandler(8, (p18, slot, item16, action) -> {
                        p18.closeInventory();
                        p18.sendMessage("");
                        p18.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&o" + Slimefun.getItemConfig().getString(sfItem.getID() + ".wiki")));
                        p18.sendMessage("");
                        return false;
                    });
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (Slimefun.getItemConfig().contains(sfItem.getID() + ".youtube")) {
				try {
					menu.addItem(7, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzNTNmZDBmODYzMTQzNTM4NzY1ODYwNzViOWJkZjBjNDg0YWFiMDMzMWI4NzJkZjExYmQ1NjRmY2IwMjllZCJ9fX0="), "&r演示视频 &7(Youtube)", "", "&7\u21E8 单击观看"));
					menu.addMenuClickHandler(7, (p19, slot, item17, action) -> {
                        p19.closeInventory();
                        p19.sendMessage("");
                        p19.sendMessage("&r&l无法访问此网站:");
                        p19.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&o" + Slimefun.getItemConfig().getString(sfItem.getID() + ".youtube")));
                        p19.sendMessage("&r的响应时间过长.");
                        p19.sendMessage("&r错误代码: ERR_CONNECTION_TIMED_OUT");
                        p19.sendMessage("");
                        return false;
                    });
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		menu.addItem(10, recipeType);
		menu.addMenuClickHandler(10, (p110, slot, item18, action) -> false);

		menu.addItem(12, Slimefun.hasUnlocked(p, recipe[3], false) ? recipe[3]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[3], false), 0, new String[] {"&4&l已锁定", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[3]), false) ? "&r需要在其他地方解锁" : "&r无权限"}));
		menu.addMenuClickHandler(12, (p112, slot, item110, action) -> {
            displayItem(p112, item110, true, book, 0);
            return false;
        });

		menu.addItem(13, Slimefun.hasUnlocked(p, recipe[4], false) ? recipe[4]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[4], false), 0, new String[] {"&4&l已锁定", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[4]), false) ? "&r需要在其他地方解锁" : "&r无权限"}));
		menu.addMenuClickHandler(13, (p111, slot, item19, action) -> {
            displayItem(p111, item19, true, book, 0);
            return false;
        });

		menu.addItem(14, Slimefun.hasUnlocked(p, recipe[5], false) ? recipe[5]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[5], false), 0, new String[] {"&4&l已锁定", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[5]), false) ? "&r需要在其他地方解锁" : "&r无权限"}));
		menu.addMenuClickHandler(14, (p116, slot, item114, action) -> {
            displayItem(p116, item114, true, book, 0);
            return false;
        });

		menu.addItem(16, recipeOutput);
		menu.addMenuClickHandler(16, (p114, slot, item112, action) -> false);

		menu.addItem(21, Slimefun.hasUnlocked(p, recipe[6], false) ? recipe[6]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[6], false), 0, new String[] {"&4&l已锁定", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[6]), false) ? "&r需要在其他地方解锁" : "&r无权限"}));
		menu.addMenuClickHandler(21, (p113, slot, item111, action) -> {
            displayItem(p113, item111, true, book, 0);
            return false;
        });

		menu.addItem(22, Slimefun.hasUnlocked(p, recipe[7], false) ? recipe[7]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[7], false), 0, new String[] {"&4&l已锁定", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[7]), false) ? "&r需要在其他地方解锁" : "&r无权限"}));
		menu.addMenuClickHandler(22, (p115, slot, item113, action) -> {
            displayItem(p115, item113, true, book, 0);
            return false;
        });

		menu.addItem(23, Slimefun.hasUnlocked(p, recipe[8], false) ? recipe[8]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[8], false), 0, new String[] {"&4&l已锁定", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[8]), false) ? "&r需要在其他地方解锁" : "&r无权限"}));
		menu.addMenuClickHandler(23, (p118, slot, item116, action) -> {
            displayItem(p118, item116, true, book, 0);
            return false;
        });

		if (sfItem != null) {

            if ((sfItem instanceof SlimefunMachine && !((SlimefunMachine) sfItem).getDisplayRecipes().isEmpty()) || (sfItem instanceof SlimefunGadget && !((SlimefunGadget) sfItem).getRecipes().isEmpty())) {
                for (int i = 27; i < 36; i++) {
					menu.addItem(i, new CustomItem(Material.LIME_STAINED_GLASS_PANE, SlimefunItem.getByItem(item) instanceof SlimefunMachine ? "&7\u21E9 此机器可用的合成配方 \u21E9": " ", 7));
					menu.addMenuClickHandler(i, (arg0, arg1, arg2, arg3) -> false);
				}

				List<ItemStack> recipes = SlimefunItem.getByItem(item) instanceof SlimefunMachine ? ((SlimefunMachine) SlimefunItem.getByItem(item)).getDisplayRecipes(): ((SlimefunGadget) SlimefunItem.getByItem(item)).getDisplayRecipes();
				int recipeSize = recipes.size();
				if (recipeSize > 18) recipeSize = 18;
				int inputs = -1, outputs = -1;

				for (int i = 0; i < recipeSize; i++) {
					int slot = 36;
					if (i % 2 == 1) {
						slot = slot + 9;
						outputs++;
					}
					else inputs++;

					int addition = (i % 2 == 0 ? inputs: outputs);

					menu.addItem(slot + addition, recipes.get(i));
					menu.addMenuClickHandler(slot + addition, (p117, slot1, item115, action) -> {
                        displayItem(p117, item115, true, book, 0);
                        return false;
                    });
				}
			}
			else if (sfItem instanceof AGenerator) {
				int slot = 27;
				for (MachineFuel fuel: ((AGenerator) sfItem).getFuelTypes()) {
					if (slot >= 54) break;
					ItemStack fItem = fuel.getInput().clone();
					ItemMeta im = fItem.getItemMeta();
					List<String> lore = new ArrayList<String>();
					lore.add(ChatColor.translateAlternateColorCodes('&', "&8\u21E8 &7剩余 " + getTimeLeft(fuel.getTicks() / 2)));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&8\u21E8 &e\u26A1 &7" + (((AGenerator) sfItem).getEnergyProduction() * 2) + " J/s"));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&8\u21E8 &e\u26A1 &7 共发电" + DoubleHandler.getFancyDouble((double) fuel.getTicks() * ((AGenerator) sfItem).getEnergyProduction()) + " J"));
					im.setLore(lore);
					fItem.setItemMeta(im);
					menu.addItem(slot, fItem);
					menu.addMenuClickHandler(slot, (p119, slot12, item117, action) -> false);
					slot++;
				}
			}
			else if (sfItem instanceof AReactor) {
				int slot = 27;
				for (MachineFuel fuel: ((AReactor) sfItem).getFuelTypes()) {
					if (slot >= 54) break;
					ItemStack fItem = fuel.getInput().clone();
					ItemMeta im = fItem.getItemMeta();
					List<String> lore = new ArrayList<String>();
					lore.add(ChatColor.translateAlternateColorCodes('&', "&8\u21E8 &7剩余 " + getTimeLeft(fuel.getTicks() / 2)));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&8\u21E8 &e\u26A1 &7" + (((AReactor) sfItem).getEnergyProduction() * 2) + " J/s"));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&8\u21E8 &e\u26A1 &7 共发电" + DoubleHandler.getFancyDouble((double) fuel.getTicks() * ((AReactor) sfItem).getEnergyProduction()) + " J"));
					im.setLore(lore);
					fItem.setItemMeta(im);
					menu.addItem(slot, fItem);
					menu.addMenuClickHandler(slot, (p120, slot13, item118, action) -> false);
					slot++;
				}
			}
		}

		menu.open(p);
	}

    private static Map<UUID, List<Object>> getHistory() {
        return SlimefunStartup.instance.getUtilities().guideHistory;
    }


    public static void clearHistory(UUID uuid) {
        getHistory().remove(uuid);
	}

	private static String getTimeLeft(int l) {
		String timeleft = "";
        final int minutes = (int) (l / 60L);
        if (minutes > 0) {
            timeleft = timeleft + minutes + "m ";
        }
        l -= minutes * 60;
        final int seconds = l;
        timeleft = timeleft + seconds + "s";
        return "&7" + timeleft;
	}

}
