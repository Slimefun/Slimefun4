package me.mrCookieSlime.Slimefun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.MenuHelper;
import me.mrCookieSlime.Slimefun.Setup.SlimefunLocalization;
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
import org.bukkit.inventory.meta.SkullMeta;

import me.mrCookieSlime.CSCoreLibPlugin.PlayerRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.HoverAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.CustomBookOverlay;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SeasonalCategory;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.GuideHandler;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunGuideLayout;
import me.mrCookieSlime.Slimefun.hooks.github.Contributor;
import me.mrCookieSlime.Slimefun.hooks.github.IntegerFormat;

public final class SlimefunGuide {
	
	private SlimefunGuide() {}
	
	private static final int CATEGORY_SIZE = 36;
	private static final int[] slots = new int[] {0, 2, 3, 5, 6, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
	
	@Deprecated
	public static ItemStack getItem() {
		return getItem(SlimefunGuideLayout.CHEST);
	}

	public static ItemStack getItem(SlimefunGuideLayout design) {
		switch (design) {
		case BOOK:
			return new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&aSlimefun Guide &7(Book GUI)", "", "&eRight Click &8\u21E8 &7Browse Items", "&eShift + Right Click &8\u21E8 &7Open Settings / Credits");
		case CHEAT_SHEET:
			return new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&cSlimefun Guide &4(Cheat Sheet)", "", "&4&lOnly openable by Admins", "", "&eRight Click &8\u21E8 &7Browse Items", "&eShift + Right Click &8\u21E8 &7Open Settings / Credits");
		case CHEST:
			return new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&aSlimefun Guide &7(Chest GUI)", "", "&eRight Click &8\u21E8 &7Browse Items", "&eShift + Right Click &8\u21E8 &7Open Settings / Credits");
		default:
			return null;
		}
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

		for (int i: slots) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i,
				(pl, slot, item, action) -> false
			);
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
		
		menu.addItem(1, new CustomItem(new ItemStack(Material.WRITABLE_BOOK), "&aCredits", "", "&7Version: &a" + SlimefunPlugin.instance.getDescription().getVersion(), "&7Contributors: &e" + SlimefunPlugin.getUtilities().contributors.size(), "", "&7\u21E8 Click to see the people behind this Plugin"));
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
		
		menu.addItem(7, new CustomItem(new ItemStack(Material.REDSTONE), "&4Bug Tracker", "", "&7Unsolved Issues: &a" + SlimefunPlugin.getUtilities().issues, "", "&7\u21E8 Click to go to the Slimefun Bug Tracker"));
		menu.addMenuClickHandler(7, (pl, slot, item, action) -> {
			pl.closeInventory();
			pl.sendMessage("");
			pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&ohttps://github.com/TheBusyBiscuit/Slimefun4/issues"));
			pl.sendMessage("");
			return false;
		});
		
		menu.open(p);
	}

	public static void openCredits(Player p, final ItemStack guide) {
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
		openMainMenu(p, false, false, 1);
	}
	
	public static void openGuide(Player p, boolean book) {
		if (!SlimefunPlugin.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled")) return;
		if (!SlimefunPlugin.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled-items.SLIMEFUN_GUIDE")) return;
		
		if (!getHistory().containsKey(p.getUniqueId())) {
			openMainMenu(p, true, book, 1);
		}
		else {
			Object last = getLastEntry(p, false);
			handleHistory(p, last, book, false);
		}
	}

	public static void openMainMenu(final Player p, final boolean survival, final boolean book, final int selected_page) {
		if (survival) {
			clearHistory(p.getUniqueId());
		}
		
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
				
				if (!locked) {
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
						texts.add(ChatColor.translateAlternateColorCodes('&', "&8\u21E8 &6Tier " + tier));
						tooltips.add(null);
						actions.add(null);
					}
					if (category instanceof LockedCategory && !((LockedCategory) category).hasUnlocked(p)) {
						StringBuilder parents = new StringBuilder(ChatColor.translateAlternateColorCodes('&', "&4&lLOCKED\n\n&7In order to unlock this Category,\n&7you need to unlock all Items from\n&7the following Categories first:\n"));
						
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
							tooltips.add(ChatColor.translateAlternateColorCodes('&', "&eClick to open the following Category:\n" + StringUtils.formatItemName(category.getItem(), false)));
							actions.add(new PlayerRunnable(1) {
								@Override
								public void run(final Player p) {
									Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> openCategory(p, category, survival, 1, book), 1L);
								}
							});
						}
					}
					else {
						texts.add(ChatColor.translateAlternateColorCodes('&', shorten("&a", StringUtils.formatItemName(category.getItem(), false))));
						tooltips.add(ChatColor.translateAlternateColorCodes('&', "&eClick to open the following Category:\n" + StringUtils.formatItemName(category.getItem(), false)));
						actions.add(new PlayerRunnable(1) {
							@Override
							public void run(final Player p) {
								Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> openCategory(p, category, survival, 1, book), 1L);
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
				page.addText(ChatColor.translateAlternateColorCodes('&', "&b&l- Slimefun Guide -\n\n"));
				for (int j = i; j < texts.size() && j < i + 10; j++) {
					page.addText(texts.get(j) + "\n");
					if (tooltips.get(j) != null) page.addHoverEvent(HoverAction.SHOW_TEXT, tooltips.get(j));
					if (actions.get(j) != null) page.addClickEvent(actions.get(j));
				}
//				page.addText("\n");
//				if (i > 0) {
//					page.addText(ChatColor.translateAlternateColorCodes('&', "&c<- Prev"));
//					page.addHoverEvent(HoverAction.SHOW_TEXT, ChatColor.translateAlternateColorCodes('&', "&eGo to Page " + (i)));
//					page.addClickEvent(me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.ClickAction.CHANGE_PAGE, String.valueOf(i));
//					page.addText("    ");
//				}
//				if (texts.size() > i * 10) {
//					page.addText("    ");
//					page.addText(ChatColor.translateAlternateColorCodes('&', "&cNext ->"));
//					page.addHoverEvent(HoverAction.SHOW_TEXT, ChatColor.translateAlternateColorCodes('&', "&eGo to Page " + (i + 2)));
//					page.addClickEvent(me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.ClickAction.CHANGE_PAGE, String.valueOf(i + 2));
//				}
				pages.add(page);
			}
			
			new CustomBookOverlay("Slimefun Guide", "mrCookieSlime", pages.toArray(new TellRawMessage[pages.size()])).open(p);
		}
		else {
			final ChestMenu menu = new ChestMenu("Slimefun Guide");
			
			menu.setEmptySlotsClickable(false);
			menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1));
			
			List<Category> categories = SlimefunPlugin.getUtilities().enabledCategories;
			List<GuideHandler> handlers = SlimefunPlugin.getUtilities().guideHandlers.values().stream().flatMap(List::stream).collect(Collectors.toList());
			
			int index = 9;
			int pages = 1;

			fillInv(menu, !survival);

			int target = (CATEGORY_SIZE * (selected_page - 1)) - 1;
			
			while (target < (categories.size() + handlers.size() - 1)) {
				if (index >= CATEGORY_SIZE + 9) {
					pages++;
					break;
				}
				
				target++;
				
				if (target >= categories.size()) {
					if (!survival) {
						break;
					}
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
							menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
								openCategory(pl, category, survival, 1, book);
								return false;
							});
							index++;
						}
						else {
							if (((SeasonalCategory) category).isUnlocked()) {
								menu.addItem(index, category.getItem());
								menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
									openCategory(pl, category, survival, 1, book);
									return false;
								});
								index++;
							}
						}
					}
					else if (((LockedCategory) category).hasUnlocked(p)) {
						menu.addItem(index, category.getItem());
						menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
							openCategory(pl, category, survival, 1, book);
							return false;
						});
						index++;
					}
					else {
						List<String> parents = new ArrayList<>();
						parents.add("");
						parents.add(ChatColor.translateAlternateColorCodes('&', "&rYou need to unlock all Items"));
						parents.add(ChatColor.translateAlternateColorCodes('&', "&rfrom the following Categories first:"));
						parents.add("");
						
						for (Category parent : ((LockedCategory) category).getParents()) {
							parents.add(parent.getItem().getItemMeta().getDisplayName());
						}
						
						menu.addItem(index, new CustomItem(Material.BARRIER, "&4LOCKED &7- &r" + category.getItem().getItemMeta().getDisplayName(), parents.toArray(new String[parents.size()])));
						menu.addMenuClickHandler(index, (pl, slot, item, action) -> false);
						index++;
					}
				}
			}

			final int finalPages = pages;
			
			menu.addItem(46, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r\u21E6 Previous Page", "", "&7(" + selected_page + " / " + pages + ")"));
			menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
				int next = selected_page - 1;
				if (next < 1) next = finalPages;
				if (next != selected_page) openMainMenu(pl, survival, book, next);
				return false;
			});
			
			menu.addItem(52, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&rNext Page \u21E8", "", "&7(" + selected_page + " / " + pages + ")"));
			menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
				int next = selected_page + 1;
				if (next > finalPages) next = 1;
				if (next != selected_page) openMainMenu(pl, survival, book, next);
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
							tooltips.add(ChatColor.translateAlternateColorCodes('&', StringUtils.formatItemName(item.getItem(), false) + "\n&c&lLOCKED\n\n&7Cost: " + (p.getLevel() >= research.getCost() ? "&b": "&4") + research.getCost() + " Levels\n\n&a> Click to unlock"));
							actions.add(new PlayerRunnable(2) {
								
								@Override
								public void run(final Player p) {
									if (!Research.isResearching(p)) {
										if (research.canUnlock(p)) {
											PlayerProfile profile = PlayerProfile.get(p);
											
											if (profile.hasUnlocked(research)) {
												openCategory(p, category, true, selected_page, book);
											}
											else {
												if (!(p.getGameMode() == GameMode.CREATIVE && SlimefunPlugin.getSettings().researchesFreeInCreative)) {
													p.setLevel(p.getLevel() - research.getCost());
												}

												if (p.getGameMode() == GameMode.CREATIVE) {
													research.unlock(p, true);
													
													Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> openCategory(p, category, survival, selected_page, book), 1L);
												} 
												else {
													research.unlock(p, false);
													
													Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> openCategory(p, category, survival, selected_page, book), 103L);
												}
											}
										} else SlimefunPlugin.getLocal().sendMessage(p, "messages.not-enough-xp", true);
									}
								}
							});
						}
						else {
							texts.add(ChatColor.translateAlternateColorCodes('&', shorten("&a", StringUtils.formatItemName(item.getItem(), false))));
							
							StringBuilder tooltip = new StringBuilder();
							
							tooltip.append(StringUtils.formatItemName(item.getItem(), false));
							
							if (item.getItem().hasItemMeta() && item.getItem().getItemMeta().hasLore()) {
								for (String line : item.getItem().getItemMeta().getLore()) {
									tooltip.append("\n" + line);
								}
							}
							
							tooltip.append(ChatColor.translateAlternateColorCodes('&', "\n\n&e&oClick for more Info"));
							
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
					tooltips.add(ChatColor.translateAlternateColorCodes('&', "&cNo Permission!"));
					actions.add(null);
				}
			}
			
			for (int i = 0; i < texts.size(); i = i + 10) {
				TellRawMessage page = new TellRawMessage();
				page.addText(ChatColor.translateAlternateColorCodes('&', "&b&l- Slimefun Guide -\n\n"));
				
				for (int j = i; j < texts.size() && j < i + 10; j++) {
					page.addText(texts.get(j) + "\n");
					if (tooltips.get(j) != null) page.addHoverEvent(HoverAction.SHOW_TEXT, tooltips.get(j));
					if (actions.get(j) != null) page.addClickEvent(actions.get(j));
				}
				
				page.addText("\n");
				page.addText(ChatColor.translateAlternateColorCodes('&', "&6\u21E6 &lBack"));
				page.addHoverEvent(HoverAction.SHOW_TEXT, ChatColor.translateAlternateColorCodes('&', "&eClick to go back to the Category Overview"));
				page.addClickEvent(new PlayerRunnable(2) {
					
					@Override
					public void run(final Player p) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> openMainMenu(p, survival, true, 1), 1L);
					}
					
				});
				pages.add(page);
			}
			
			new CustomBookOverlay("Slimefun Guide", "mrCookieSlime", pages.toArray(new TellRawMessage[pages.size()])).open(p);
		}
		else {
			final ChestMenu menu = new ChestMenu("Slimefun Guide");
			
			menu.setEmptySlotsClickable(false);
			menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1));
			
			int index = 9;
			final int pages = (category.getItems().size() - 1) / CATEGORY_SIZE + 1;
			for (int i = 0; i < 4; i++) {
				menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
				menu.addMenuClickHandler(i, (pl, slot, item, action) -> false);
			}
			
			menu.addItem(4, new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&7\u21E6 Back"));
			menu.addMenuClickHandler(4, (pl, slot, item, action) -> {
				openMainMenu(pl, survival, book, 1);
				return false;
			});
			
			for (int i = 5; i < 9; i++) {
				menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
				menu.addMenuClickHandler(i, (pl, slot, item, action) -> false);
			}
			
			for (int i = 45; i < 54; i++) {
				menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
				menu.addMenuClickHandler(i, (pl, slot, item, action) -> false);
			}
			
			menu.addItem(46, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r\u21E6 Previous Page", "", "&7(" + selected_page + " / " + pages + ")"));
			menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
				int next = selected_page - 1;
				if (next < 1) next = pages;
				if (next != selected_page) openCategory(pl, category, survival, next, book);
				return false;
			});
			
			menu.addItem(52, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&rNext Page \u21E8", "", "&7(" + selected_page + " / " + pages + ")"));
			menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
				int next = selected_page + 1;
				if (next > pages) next = 1;
				if (next != selected_page) openCategory(pl, category, survival, next, book);
				return false;
			});
			
			int categoryIndex = CATEGORY_SIZE * (selected_page - 1);
			for (int i = 0; i < CATEGORY_SIZE; i++) {
				int target = categoryIndex + i;
				if (target >= category.getItems().size()) break;
				final SlimefunItem sfitem = category.getItems().get(target);
				
				if (Slimefun.isEnabled(p, sfitem, false)) {
					if (survival && !Slimefun.hasUnlocked(p, sfitem.getItem(), false) && sfitem.getResearch() != null) {
						if (Slimefun.hasPermission(p, sfitem, false)) {
							final Research research = sfitem.getResearch();
						    
							menu.addItem(index, new CustomItem(Material.BARRIER, "&r" + StringUtils.formatItemName(sfitem.getItem(), false), "&4&lLOCKED", "", "&a> Click to unlock", "", "&7Cost: &b" + research.getCost() + " Level"));
							menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
								if (!Research.isResearching(pl)) {
									if (research.canUnlock(pl)) {
										PlayerProfile profile = PlayerProfile.get(p);
										
										if (profile.hasUnlocked(research)) {
											openCategory(p, category, true, selected_page, book);
										}
										else {
											if (!(pl.getGameMode() == GameMode.CREATIVE && SlimefunPlugin.getSettings().researchesFreeInCreative)) {
												pl.setLevel(pl.getLevel() - research.getCost());
											}

											if (pl.getGameMode() == GameMode.CREATIVE) {
												research.unlock(pl, SlimefunPlugin.getSettings().researchesFreeInCreative);
												openCategory(pl, category, survival, selected_page, book);
											}
											else {
												research.unlock(pl, false);
												Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> openCategory(pl, category, survival, selected_page, book), 103L);
											}
										}
									}
									else SlimefunPlugin.getLocal().sendMessage(pl, "messages.not-enough-xp", true);
								}
								return false;
							});

							index++;
						}
						else {
							List<String> message = sfitem.getNoPermissionTooltip();
						    menu.addItem(index, new CustomItem(Material.BARRIER, StringUtils.formatItemName(sfitem.getItem(), false), message.toArray(new String[message.size()])));
							menu.addMenuClickHandler(index, (pl, slot, item, action) -> false);
							index++;
						}
					}
					else {
						menu.addItem(index, sfitem.getItem());
						menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
							if (survival) displayItem(pl, item, true, book, 0);
							else pl.getInventory().addItem(item);
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

	public static void openSearch(Player player, String searchTerm, boolean cheat, boolean addToHistory) {
		final ChestMenu menu = new ChestMenu("Slimefun Guide Search");

		menu.setEmptySlotsClickable(false);

		fillInv(menu, cheat);

		addBackButton(menu, player, false, cheat);

		searchTerm = searchTerm.toLowerCase();

		int index = 9;
		// Find items and add them
		for (SlimefunItem item : SlimefunItem.list()) {
			final String itemName = ChatColor.stripColor(item.getItem().getItemMeta().getDisplayName()).toLowerCase();

			if (itemName.isEmpty()) continue;

			if (index == 44) break;

			if (itemName.equals(searchTerm) || itemName.contains(searchTerm)) {
				menu.addItem(index, item.getItem());
				menu.addMenuClickHandler(index, (pl, slot, itm, action) -> {
					if (cheat)
						pl.getInventory().addItem(itm);
					else
						displayItem(pl, itm, true, false, 0);
					return false;
				});

				index++;
			}
		}

		if (addToHistory)
			addToHistory(player, searchTerm);

		menu.open(player);
	}

	private static void fillInv(ChestMenu menu, boolean cheat) {
		for (int i = 0; i < 9; i++) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i, (arg0, arg1, arg2, arg3) -> false);
		}

		// Search feature!
		menu.addItem(7, new CustomItem(Material.NAME_TAG, SlimefunPlugin.getLocal().getMessage("guide.search.name"),
			SlimefunPlugin.getLocal().getMessages("guide.search.lore").toArray(new String[0])));
		menu.addMenuClickHandler(7, (player, i, itemStack, clickAction) -> {
			player.closeInventory();
			SlimefunPlugin.getLocal().sendMessage(player, "search.message");
			MenuHelper.awaitChatInput(player, (p, s) -> {
				openSearch(p, s, cheat, true);
				return true; // ?
			});

			return false;
		});

		for (int i = 45; i < 54; i++) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i, (arg0, arg1, arg2, arg3) -> false);
		}
	}

	private static void addBackButton(ChestMenu menu, Player player, boolean book, boolean cheat) {
		List<Object> playerHistory = getHistory().getOrDefault(player.getUniqueId(), new LinkedList<>());
		if (playerHistory != null && playerHistory.size() > 1) {
			menu.addItem(0, new CustomItem(new ItemStack(Material.ENCHANTED_BOOK),
				"&7\u21E6 Back", "",
				"&rLeft Click: &7Go back to previous Page",
				"&rShift + left Click: &7Go back to Main Menu")
			);
			menu.addMenuClickHandler(0, (pl, slot, item118, action) -> {
				if (action.isShiftClicked()) openMainMenu(pl, true, false, 1);
				else {
					Object last = getLastEntry(pl, true);
					handleHistory(pl, last, book, cheat);
				}
				return false;
			});
		} else {
			menu.addItem(0, new CustomItem(new ItemStack(Material.ENCHANTED_BOOK),
				"&7\u21E6 Back", "", "&rLeft Click: &7Go back to Main Menu"));
			menu.addMenuClickHandler(0, (p117, slot, item117, action) -> {
				openMainMenu(p117, true, book, 1);
				return false;
			});
		}
	}

	public static void addToHistory(Player p, Object obj) {
		LinkedList<Object> list = getHistory().computeIfAbsent(p.getUniqueId(), k -> new LinkedList<>());
		list.add(obj);
	}

	private static Object getLastEntry(Player p, boolean remove) {
		LinkedList<Object> history = getHistory().get(p.getUniqueId());
		
		if (remove && history != null && !history.isEmpty()) {
			history.removeLast();
		}
		
		if (history != null && history.isEmpty()) {
			getHistory().remove(p.getUniqueId());
		}
		
		return history == null || history.isEmpty() ? null: history.getLast();
	}

	public static void displayItem(Player p, final ItemStack item, boolean addToHistory, final boolean book, final int page) {
		if (item == null || item.getType() == Material.AIR) return;

		final SlimefunItem sfItem = SlimefunItem.getByItem(item);
		
		if (sfItem == null && !SlimefunPlugin.getSettings().guideShowVanillaRecipes) return;
		
		ItemStack[] recipe = new ItemStack[9];
		ItemStack recipeType = null;
		ItemStack recipeOutput = item;
		
		ChestMenu menu = new ChestMenu("Slimefun Guide");
		
		menu.setEmptySlotsClickable(false);
		menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1));

		if (sfItem != null) {
			recipe = sfItem.getRecipe();
			recipeType = sfItem.getRecipeType().toItem();
			recipeOutput = sfItem.getRecipeOutput() != null ? sfItem.getRecipeOutput(): sfItem.getItem();
		}
		else {
			List<Recipe> recipes = new ArrayList<>();
			Iterator<Recipe> iterator = Bukkit.recipeIterator();
			while (iterator.hasNext()) {
				Recipe r = iterator.next();
				if (SlimefunManager.isItemSimiliar(new CustomItem(r.getResult(), 1), item, true)) recipes.add(r);
			}
			
			if (recipes.isEmpty()) return;
			Recipe r = recipes.get(page);
			 
			if (recipes.size() > page + 1) {
				menu.addItem(1, new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&7Next \u21E8", "", "&e&l! &rThere are multiple recipes for this Item"));
				menu.addMenuClickHandler(1, (pl, slot, itemstack, action) -> {
					displayItem(pl, itemstack, false, book, page + 1);
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
				recipe[4] = ((FurnaceRecipe) r).getInput();
				
				recipeType = RecipeType.FURNACE.toItem();
				recipeOutput = r.getResult();
			}
		}
		
		if (addToHistory) addToHistory(p, sfItem != null ? sfItem: item);

		addBackButton(menu, p, book, false);
		
		LinkedList<Object> history = getHistory().get(p.getUniqueId());
		
		if (history != null && history.size() > 1) {
			menu.addItem(0, new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&7\u21E6 Back", "", "&rLeft Click: &7Go back to previous Page", "&rShift + left Click: &7Go back to Main Menu"));
			menu.addMenuClickHandler(0, (pl, slot, itemstack, action) -> {
				if (action.isShiftClicked()) openMainMenu(p, true, book, 1);
				else {
					Object last = getLastEntry(pl, true);
					handleHistory(pl, last, book, false);
				}
				return false;
			});
		}
		else {
			menu.addItem(0, new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&7\u21E6 Back", "", "&rLeft Click: &7Go back to Main Menu"));
			menu.addMenuClickHandler(0, (pl, slot, itemstack, action) -> {
				openMainMenu(pl, true, book, 1);
				return false;
			});
		}
		
		menu.addItem(3, Slimefun.hasUnlocked(p, recipe[0], false) ? recipe[0]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[0], false), "&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[0]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"));
		menu.addMenuClickHandler(3, (pl, slot, itemstack, action) -> {
			displayItem(pl, itemstack, true, book, 0);
			return false;
		});
		
		menu.addItem(4, Slimefun.hasUnlocked(p, recipe[1], false) ? recipe[1]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[1], false), "&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[1]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"));
		menu.addMenuClickHandler(4, (pl, slot, itemstack, action) -> {
			displayItem(pl, itemstack, true, book, 0);
			return false;
		});
		
		menu.addItem(5, Slimefun.hasUnlocked(p, recipe[2], false) ? recipe[2]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[2], false), "&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[2]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"));
		menu.addMenuClickHandler(5, (pl, slot, itemstack, action) -> {
			displayItem(pl, itemstack, true, book, 0);
			return false;
		});
		
		if (sfItem != null) {
			if (sfItem.hasWiki()) {
				try {
					menu.addItem(8, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY2OTJmOTljYzZkNzgyNDIzMDQxMTA1NTM1ODk0ODQyOThiMmU0YTAyMzNiNzY3NTNmODg4ZTIwN2VmNSJ9fX0="), "&rView this Item in our Wiki &7(Slimefun Wiki)", "", "&7\u21E8 Click to open"));
					menu.addMenuClickHandler(8, (pl, slot, itemstack, action) -> {
						pl.closeInventory();
						pl.sendMessage("");
						pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&o" + sfItem.getWiki()));
						pl.sendMessage("");
						return false;
					});
				} catch (Exception x) {
					Slimefun.getLogger().log(Level.SEVERE, "An Error occured while adding a Wiki Page for Slimefun " + Slimefun.getVersion(), x);
				}
			}
			
			if (Slimefun.getItemConfig().contains(sfItem.getID() + ".youtube")) {
				try {
					menu.addItem(7, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzNTNmZDBmODYzMTQzNTM4NzY1ODYwNzViOWJkZjBjNDg0YWFiMDMzMWI4NzJkZjExYmQ1NjRmY2IwMjllZCJ9fX0="), "&rDemonstration Video &7(Youtube)", "", "&7\u21E8 Click to watch"));
					menu.addMenuClickHandler(7, (pl, slot, itemstack, action) -> {
						pl.closeInventory();
						pl.sendMessage("");
						pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&o" + Slimefun.getItemConfig().getString(sfItem.getID() + ".youtube")));
						pl.sendMessage("");
						return false;
					});
				} catch (Exception x) {
					Slimefun.getLogger().log(Level.SEVERE, "An Error occured while adding a Youtube Video for Slimefun " + Slimefun.getVersion(), x);
				}
			}
		}

		menu.addItem(10, recipeType);
		menu.addMenuClickHandler(10, (pl, slot, itemstack, action) -> false);
		
		menu.addItem(12, Slimefun.hasUnlocked(p, recipe[3], false) ? recipe[3]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[3], false), "&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[3]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"));
		menu.addMenuClickHandler(12, (pl, slot, itemstack, action) -> {
			displayItem(pl, itemstack, true, book, 0);
			return false;
		});
		
		menu.addItem(13, Slimefun.hasUnlocked(p, recipe[4], false) ? recipe[4]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[4], false), "&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[4]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"));
		menu.addMenuClickHandler(13, (pl, slot, itemstack, action) -> {
			displayItem(pl, itemstack, true, book, 0);
			return false;
		});
		
		menu.addItem(14, Slimefun.hasUnlocked(p, recipe[5], false) ? recipe[5]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[5], false), "&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[5]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"));
		menu.addMenuClickHandler(14, (pl, slot, itemstack, action) -> {
			displayItem(pl, itemstack, true, book, 0);
			return false;
		});
		
		menu.addItem(16, recipeOutput);
		menu.addMenuClickHandler(16, (pl, slot, itemstack, action) -> false);
		
		menu.addItem(21, Slimefun.hasUnlocked(p, recipe[6], false) ? recipe[6]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[6], false), "&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[6]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"));
		menu.addMenuClickHandler(21, (pl, slot, itemstack, action) -> {
			displayItem(pl, itemstack, true, book, 0);
			return false;
		});
		
		menu.addItem(22, Slimefun.hasUnlocked(p, recipe[7], false) ? recipe[7]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[7], false), "&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[7]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"));
		menu.addMenuClickHandler(22, (pl, slot, itemstack, action) -> {
			displayItem(pl, itemstack, true, book, 0);
			return false;
		});
		
		menu.addItem(23, Slimefun.hasUnlocked(p, recipe[8], false) ? recipe[8]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[8], false), "&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[8]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"));
		menu.addMenuClickHandler(23, (pl, slot, itemstack, action) -> {
			displayItem(pl, itemstack, true, book, 0);
			return false;
		});
		
		if (sfItem instanceof RecipeDisplayItem) {
			displayRecipes(menu, (RecipeDisplayItem) sfItem, 0);
		}
		
		menu.open(p);
	}

	private static void handleHistory(Player pl, Object last, boolean book, boolean cheat) {
		if (last instanceof Category) openCategory(pl, (Category) last, true, 1, book);
		else if (last instanceof SlimefunItem) displayItem(pl, ((SlimefunItem) last).getItem(), false, book, 0);
		else if (last instanceof GuideHandler) ((GuideHandler) last).run(pl, true, book);
		else if (last instanceof String) openSearch(pl, (String) last, cheat, true);
		else displayItem(pl, (ItemStack) last, false, book, 0);
	}

	private static void displayRecipes(ChestMenu menu, RecipeDisplayItem sfItem, int page) {
		List<ItemStack> recipes = sfItem.getDisplayRecipes();
		
		if (!recipes.isEmpty()) {
			menu.addItem(53, null);
			
			if (page == 0) {
				for (int i = 27; i < 36; i++) {
					menu.replaceExistingItem(i, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, sfItem.getRecipeSectionLabel()));
					menu.addMenuClickHandler(i, (pl, slot, itemstack, action) -> false);
				}
			}
			else {
				menu.replaceExistingItem(28, new CustomItem(Material.LIME_STAINED_GLASS_PANE, "&a\u21E6 Previous Page"));
				menu.addMenuClickHandler(28, (pl, slot, itemstack, action) -> {
					displayRecipes(menu, sfItem, page - 1);
					pl.playSound(pl.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
					return false;
				});
			}
			
			if (recipes.size() > (18 * (page + 1))) {
				menu.replaceExistingItem(34, new CustomItem(Material.LIME_STAINED_GLASS_PANE, "&aNext Page \u21E8"));
				menu.addMenuClickHandler(34, (pl, slot, itemstack, action) -> {
					displayRecipes(menu, sfItem, page + 1);
					pl.playSound(pl.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
					return false;
				});
			}
			else {
				menu.replaceExistingItem(34, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, sfItem.getRecipeSectionLabel()));
				menu.addMenuClickHandler(34, (pl, slot, itemstack, action) -> false);
			}
			
			int inputs = 36;
			int outputs = 45;
			
			for (int i = 0; i < 18; i++) {
				int slot = i % 2 == 0 ? inputs++: outputs++;
				
				if ((i + (page * 18)) < recipes.size()) {
					if (page == 0) {
						menu.replaceExistingItem(slot, recipes.get(i + (page * 18)));
						menu.addMenuClickHandler(slot, (pl, s, itemstack, action) -> {
							displayItem(pl, itemstack, true, false, 0);
							return false;
						});
					}
					else {
						menu.replaceExistingItem(slot, recipes.get(i + (page * 18)));
					}
				}
				else {
					menu.replaceExistingItem(slot, null);
					menu.addMenuClickHandler(slot, (pl, s, itemstack, action) -> false);
				}
			}
		}
	}

	private static Map<UUID, LinkedList<Object>> getHistory() {
		return SlimefunPlugin.getUtilities().guideHistory;
	}
	
	public static void clearHistory(UUID uuid) {
		getHistory().remove(uuid);
	}
}
