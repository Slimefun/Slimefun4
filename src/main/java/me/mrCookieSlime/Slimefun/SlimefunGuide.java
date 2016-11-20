package me.mrCookieSlime.Slimefun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.mrCookieSlime.CSCoreLibPlugin.PlayerRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.HoverAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuOpeningHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.CustomBookOverlay;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SeasonCategory;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunGadget;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunMachine;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.NuclearReactor;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.URID.URID;
import me.mrCookieSlime.Slimefun.api.GuideHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;

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
import org.bukkit.material.MaterialData;

public class SlimefunGuide {
	
	public static Map<UUID, List<URID>> history = new HashMap<UUID, List<URID>>();
	public static int month = 0;
	
	static boolean all_recipes = true;
	private static final int category_size = 36;

	public static ItemStack getItem() {
		return getItem(true);
	}

	public static ItemStack getItem(boolean book) {
		return new CustomItem(new MaterialData(Material.ENCHANTED_BOOK), "&eSlimefun Guide &7(Right Click)", (book ? "": "�2"), "&rThis is your basic Guide for Slimefun", "&rYou can see all Items added by this Plugin", "&ror its Addons including their Recipes", "&ra bit of information and more");
	}
	
	public static void openSettings(Player p, ItemStack guide) {
		TellRawMessage page = new TellRawMessage();
		page.addText("�a�l- Guide Settings -\n\n");
		if (SlimefunManager.isItemSimiliar(guide, getItem(true), true)) {
			page.addText("�7Design: �3Book\n");
			page.addHoverEvent(HoverAction.SHOW_TEXT, "�e> Click to change your Guide Design to �bInventory");
			page.addClickEvent(new PlayerRunnable(3) {
				
				@Override
				public void run(Player p) {
					p.getInventory().setItemInMainHand(getItem(false));
					openSettings(p, p.getInventory().getItemInMainHand());
				}
			});
		}
		else if (SlimefunManager.isItemSimiliar(guide, getItem(false), true)) {
			page.addText("�7Design: �3Inventory\n");
			page.addHoverEvent(HoverAction.SHOW_TEXT, "�e> Click to change your Guide Design to �bBook");
			page.addClickEvent(new PlayerRunnable(3) {
				
				@Override
				public void run(Player p) {
					p.getInventory().setItemInMainHand(getItem(true));
					openSettings(p, p.getInventory().getItemInMainHand());
				}
			});
		}
		
		new CustomBookOverlay("SlimefunGuide Settings", "mrCookieSlime", page).open(p);
	}

	public static void openCheatMenu(Player p) {
		openMainMenu(p, false, false, 1);
	}
	
	public static void openGuide(Player p, boolean experimental) {
		if (!SlimefunStartup.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled")) return;
		if (!SlimefunStartup.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled-items.SLIMEFUN_GUIDE")) return;
		if (!history.containsKey(p.getUniqueId())) openMainMenu(p, true, experimental, 1);
		else {
			URID last = getLastEntry(p, false);
			if (URID.decode(last) instanceof Category) openCategory(p, (Category) URID.decode(last), true, 1, experimental);
			else if (URID.decode(last) instanceof SlimefunItem) displayItem(p, ((SlimefunItem) URID.decode(last)).getItem(), false, experimental, 0);
			else if (URID.decode(last) instanceof GuideHandler) ((GuideHandler) URID.decode(last)).run(p, true);
			else displayItem(p, (ItemStack) URID.decode(last), false, experimental, 0);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void openMainMenu(final Player p, final boolean survival, final boolean experimental, final int selected_page) {
		clearHistory(p.getUniqueId());
		
		if (experimental) {
			List<TellRawMessage> pages = new ArrayList<TellRawMessage>();
			List<String> texts = new ArrayList<String>();
			List<String> tooltips = new ArrayList<String>();
			List<PlayerRunnable> actions = new ArrayList<PlayerRunnable>();
			
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
										handler.run(p, survival);
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
						texts.add("�8\u21E8 �6Tier " + tier);
						tooltips.add(null);
						actions.add(null);
					}
					if (category instanceof LockedCategory && !((LockedCategory) category).hasUnlocked(p)) {
						StringBuilder parents = new StringBuilder("�4�lLOCKED\n\n�7In order to unlock this Category,\n�7you need to unlock all Items from\n�7the following Categories first:\n");
						
						for (Category parent: ((LockedCategory) category).getParents()) {
							parents.append("\n�c" + StringUtils.formatItemName(parent.getItem(), false));
						}
						
						texts.add(shorten("�c" , StringUtils.formatItemName(category.getItem(), false)));
						tooltips.add(parents.toString());
						actions.add(null);
					}
					else if (category instanceof SeasonCategory) {
						if (((SeasonCategory) category).isUnlocked()) {
							texts.add(shorten("�a", StringUtils.formatItemName(category.getItem(), false)));
							tooltips.add("�eClick to open the following Category:\n" + StringUtils.formatItemName(category.getItem(), false));
							actions.add(new PlayerRunnable(1) {
								
								@Override
								public void run(final Player p) {
									Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {
										
										@Override
										public void run() {
											openCategory(p, category, survival, 1, experimental);
										}
									}, 1L);
								}
							});
						}
					}
					else {
						texts.add(shorten("�a", StringUtils.formatItemName(category.getItem(), false)));
						tooltips.add("�eClick to open the following Category:\n" + StringUtils.formatItemName(category.getItem(), false));
						actions.add(new PlayerRunnable(1) {
							
							@Override
							public void run(final Player p) {
								Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {
									
									@Override
									public void run() {
										openCategory(p, category, survival, 1, experimental);
									}
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
							handler.run(p, survival);
						}
					});
				}
			}
			
			for (int i = 0; i < texts.size(); i = i + 10) {
				TellRawMessage page = new TellRawMessage();
				page.addText("�b�l- Slimefun Guide -\n\n");
				for (int j = i; j < texts.size() && j < i + 10; j++) {
					page.addText(texts.get(j) + "\n");
					if (tooltips.get(j) != null) page.addHoverEvent(HoverAction.SHOW_TEXT, tooltips.get(j));
					if (actions.get(j) != null) page.addClickEvent(actions.get(j));
				}
//				page.addText("\n");
//				if (i > 0) {
//					page.addText("�c<- Prev");
//					page.addHoverEvent(HoverAction.SHOW_TEXT, "�eGo to Page " + (i));
//					page.addClickEvent(me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.ClickAction.CHANGE_PAGE, String.valueOf(i));
//					page.addText("    ");
//				}
//				if (texts.size() > i * 10) {
//					page.addText("    ");
//					page.addText("�cNext ->");
//					page.addHoverEvent(HoverAction.SHOW_TEXT, "�eGo to Page " + (i + 2));
//					page.addClickEvent(me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.ClickAction.CHANGE_PAGE, String.valueOf(i + 2));
//				}
				pages.add(page);
			}
			
			new CustomBookOverlay("Slimefun Guide", "mrCookieSlime", pages.toArray(new TellRawMessage[pages.size()])).open(p);
		}
		else {
			final ChestMenu menu = new ChestMenu("Slimefun Guide");
			
			menu.addMenuOpeningHandler(new MenuOpeningHandler() {
				
				@Override
				public void onOpen(Player p) {
					p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.7F, 0.7F);
				}
			});
			
			List<Category> categories = Slimefun.current_categories;
			List<GuideHandler> handlers = Slimefun.guide_handlers2;
			
			int index = 9;
			int pages = 1;
			
			for (int i = 0; i < 9; i++) {
				menu.addItem(i, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 7), " "));
				menu.addMenuClickHandler(i, new MenuClickHandler() {
					
					@Override
					public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
						return false;
					}
				});
			}
			
			for (int i = 45; i < 54; i++) {
				menu.addItem(i, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 7), " "));
				menu.addMenuClickHandler(i, new MenuClickHandler() {
					
					@Override
					public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
						return false;
					}
				});
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
						if (!(category instanceof SeasonCategory)) {
							menu.addItem(index, category.getItem());
							menu.addMenuClickHandler(index, new MenuClickHandler() {
								
								@Override
								public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
									openCategory(p, category, survival, 1, experimental);
									return false;
								}
							});
							index++;
						}
						else {
							if (((SeasonCategory) category).isUnlocked()) {
								menu.addItem(index, category.getItem());
								menu.addMenuClickHandler(index, new MenuClickHandler() {
									
									@Override
									public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
										openCategory(p, category, survival, 1, experimental);
										return false;
									}
								});
								index++;
							}
						}
					}
					else if (((LockedCategory) category).hasUnlocked(p)) {
						menu.addItem(index, category.getItem());
						menu.addMenuClickHandler(index, new MenuClickHandler() {
							
							@Override
							public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
								openCategory(p, category, survival, 1, experimental);
								return false;
							}
						});
						index++;
					}
					else {
						List<String> parents = new ArrayList<String>();
						parents.add("");
						parents.add("&rYou need to unlock all Items");
						parents.add("&rfrom the following Categories first:");
						parents.add("");
						for (Category parent: ((LockedCategory) category).getParents()) {
							parents.add(parent.getItem().getItemMeta().getDisplayName());
						}
						menu.addItem(index, new CustomItem(Material.BARRIER, "&4LOCKED &7- &r" + category.getItem().getItemMeta().getDisplayName(), 0, parents.toArray(new String[parents.size()])));
						menu.addMenuClickHandler(index, new MenuClickHandler() {
							
							@Override
							public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
								return false;
							}
						});
						index++;
					}
				}
			}

			final int finalPages = pages;
			
			menu.addItem(46, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 5), "&r\u21E6 Previous Page", "", "&7(" + selected_page + " / " + pages + ")"));
			menu.addMenuClickHandler(46, new MenuClickHandler() {
				
				@Override
				public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
					int next = selected_page - 1;
					if (next < 1) next = finalPages;
					if (next != selected_page) openMainMenu(p, survival, experimental, next);
					return false;
				}
			});
			
			menu.addItem(52, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 5), "&rNext Page \u21E8", "", "&7(" + selected_page + " / " + pages + ")"));
			menu.addMenuClickHandler(52, new MenuClickHandler() {
				
				@Override
				public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
					int next = selected_page + 1;
					if (next > finalPages) next = 1;
					if (next != selected_page) openMainMenu(p, survival, experimental, next);
					return false;
				}
			});
			
			menu.open(p);
		}
	}

	public static String shorten(String string, String string2) {
		if (ChatColor.stripColor(string + string2).length() > 19) return (string + ChatColor.stripColor(string2)).substring(0, 18) + "...";
		else return (string + ChatColor.stripColor(string2));
	}

	@SuppressWarnings("deprecation")
	public static void openCategory(final Player p, final Category category, final boolean survival, final int selected_page, final boolean experimental) {
		if (category == null) return;

		if (experimental && category.getItems().size() < 250) {
			List<TellRawMessage> pages = new ArrayList<TellRawMessage>();
			List<String> texts = new ArrayList<String>();
			List<String> tooltips = new ArrayList<String>();
			List<PlayerRunnable> actions = new ArrayList<PlayerRunnable>();
			
			for (final SlimefunItem item: category.getItems()) {
				if (Slimefun.hasPermission(p, item, false)) {
					if (Slimefun.isEnabled(p, item, false)) {
						if (survival && !Slimefun.hasUnlocked(p, item, false) && item.getResearch() != null) {
							texts.add(shorten("�7", StringUtils.formatItemName(item.getItem(), false)));
							final int cost = SlimefunStartup.getResearchCfg().getInt(item.getResearch().getID() + ".cost");
							tooltips.add(StringUtils.formatItemName(item.getItem(), false) + "\n�c�lLOCKED\n\n�7Cost: " + (p.getLevel() >= cost ? "�b": "�4") + cost + " Levels\n\n�a> Click to unlock");
							actions.add(new PlayerRunnable(2) {
								
								@Override
								public void run(final Player p) {
									boolean canBuy = false;
									if (p.getGameMode() == GameMode.CREATIVE) canBuy = true;
									else if (p.getLevel() >= cost) {
										p.setLevel(p.getLevel() - cost);
										canBuy = true;
									}
									if (canBuy) {
										Research research = item.getResearch();
										boolean researched = research == null ? true: research.hasUnlocked(p);
										
										if (researched) openCategory(p, category, true, selected_page, experimental);
										else if (!Research.isResearching(p)){
											if (p.getGameMode() == GameMode.CREATIVE) {
												research.unlock(p, true);
												Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {
													
													@Override
													public void run() {
														openCategory(p, category, survival, selected_page, experimental);
													}
												}, 1L);
											}
											else {
												research.unlock(p, false);
												Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {
													
													@Override
													public void run() {
														openCategory(p, category, survival, selected_page, experimental);
													}
												}, 103L);
											}
										}
									}
									else Messages.local.sendTranslation(p, "messages.not-enough-xp", true);
								}
							});
						}
						else {
							texts.add(shorten("�a", StringUtils.formatItemName(item.getItem(), false)));
							
							StringBuilder tooltip = new StringBuilder();
							
							tooltip.append(StringUtils.formatItemName(item.getItem(), false));
							
							if (item.getItem().hasItemMeta() && item.getItem().getItemMeta().hasLore()) {
								for (String line: item.getItem().getItemMeta().getLore()) {
									tooltip.append("\n" + line);
								}
							}
							
							tooltip.append("\n\n�e�oClick for more Info");
							
							tooltips.add(tooltip.toString());
							actions.add(new PlayerRunnable(2) {
								
								@Override
								public void run(Player p) {
									displayItem(p, item.getItem(), true, experimental, 0);
								}
							});
						}
					}
				}
				else {
					texts.add(shorten("�4", StringUtils.formatItemName(item.getItem(), false)));
					tooltips.add("�cNo Permission!");
					actions.add(null);
				}
			}
			
			for (int i = 0; i < texts.size(); i = i + 10) {
				TellRawMessage page = new TellRawMessage();
				page.addText("�b�l- Slimefun Guide -\n\n");
				for (int j = i; j < texts.size() && j < i + 10; j++) {
					page.addText(texts.get(j) + "\n");
					if (tooltips.get(j) != null) page.addHoverEvent(HoverAction.SHOW_TEXT, tooltips.get(j));
					if (actions.get(j) != null) page.addClickEvent(actions.get(j));
				}
				page.addText("\n");
				page.addText("�6\u21E6 �lBack");
				page.addHoverEvent(HoverAction.SHOW_TEXT, "�eClick to go back to the Category Overview");
				page.addClickEvent(new PlayerRunnable(2) {
					
					@Override
					public void run(final Player p) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {
							
							@Override
							public void run() {
								openMainMenu(p, survival, true, 1);
							}
						}, 1L);
					}
				});
				pages.add(page);
			}
			
			new CustomBookOverlay("Slimefun Guide", "mrCookieSlime", pages.toArray(new TellRawMessage[pages.size()])).open(p);
		}
		else {
			final ChestMenu menu = new ChestMenu("Slimefun Guide");
			
			menu.addMenuOpeningHandler(new MenuOpeningHandler() {
				
				@Override
				public void onOpen(Player p) {
					p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.7F, 0.7F);
				}
			});
			
			int index = 9;
			final int pages = category.getItems().size() / category_size + 1;
			for (int i = 0; i < 4; i++) {
				menu.addItem(i, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 7), " "));
				menu.addMenuClickHandler(i, new MenuClickHandler() {
					
					@Override
					public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
						return false;
					}
				});
			}
			
			menu.addItem(4, new CustomItem(new MaterialData(Material.ENCHANTED_BOOK), "&7\u21E6 Back"));
			menu.addMenuClickHandler(4, new MenuClickHandler() {
				
				@Override
				public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
					openMainMenu(p, survival, experimental, 1);
					return false;
				}
			});
			
			for (int i = 5; i < 9; i++) {
				menu.addItem(i, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 7), " "));
				menu.addMenuClickHandler(i, new MenuClickHandler() {
					
					@Override
					public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
						return false;
					}
				});
			}
			
			for (int i = 45; i < 54; i++) {
				menu.addItem(i, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 7), " "));
				menu.addMenuClickHandler(i, new MenuClickHandler() {
					
					@Override
					public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
						return false;
					}
				});
			}
			
			menu.addItem(46, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 5), "&r\u21E6 Previous Page", "", "&7(" + selected_page + " / " + pages + ")"));
			menu.addMenuClickHandler(46, new MenuClickHandler() {
				
				@Override
				public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
					int next = selected_page - 1;
					if (next < 1) next = pages;
					if (next != selected_page) openCategory(p, category, survival, next, experimental);
					return false;
				}
			});
			
			menu.addItem(52, new CustomItem(new MaterialData(Material.STAINED_GLASS_PANE, (byte) 5), "&rNext Page \u21E8", "", "&7(" + selected_page + " / " + pages + ")"));
			menu.addMenuClickHandler(52, new MenuClickHandler() {
				
				@Override
				public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
					int next = selected_page + 1;
					if (next > pages) next = 1;
					if (next != selected_page) openCategory(p, category, survival, next, experimental);
					return false;
				}
			});
			
			int category_index = category_size * (selected_page - 1);
			for (int i = 0; i < category_size; i++) {
				int target = category_index + i;
				if (target >= category.getItems().size()) break;
				final SlimefunItem sfitem = category.getItems().get(target);
				if (Slimefun.isEnabled(p, sfitem, false)) {
					if (survival && !Slimefun.hasUnlocked(p, sfitem.getItem(), false) && sfitem.getResearch() != null) {
						if (Slimefun.hasPermission(p, sfitem, false)) {
							final int cost = SlimefunStartup.getResearchCfg().getInt(sfitem.getResearch().getID() + ".cost");
							menu.addItem(index, new CustomItem(Material.BARRIER, StringUtils.formatItemName(sfitem.getItem(), false), 0, new String[] {"&4&lLOCKED", "", "&a> Click to unlock", "", "&7Cost: &b" + cost + " Level"}));
							menu.addMenuClickHandler(index, new MenuClickHandler() {
								
								@Override
								public boolean onClick(final Player p, int slot, ItemStack item, ClickAction action) {
									boolean canBuy = false;
									if (p.getGameMode() == GameMode.CREATIVE) canBuy = true;
									else if (p.getLevel() >= cost) {
										p.setLevel(p.getLevel() - cost);
										canBuy = true;
									}
									if (canBuy) {
										Research research = sfitem.getResearch();
										boolean researched = research == null ? true: research.hasUnlocked(p);
										
										if (researched) openCategory(p, category, true, selected_page, experimental);
										else if (!Research.isResearching(p)){
											if (p.getGameMode() == GameMode.CREATIVE) {
												research.unlock(p, true);
												openCategory(p, category, survival, selected_page, experimental);
											}
											else {
												research.unlock(p, false);
												Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {
													
													@Override
													public void run() {
														openCategory(p, category, survival, selected_page, experimental);
													}
												}, 103L);
											}
										}
									}
									else Messages.local.sendTranslation(p, "messages.not-enough-xp", true);
									return false;
								}
							});
							index++;
						}
						else {
							menu.addItem(index, new CustomItem(Material.BARRIER, StringUtils.formatItemName(sfitem.getItem(), false), 0, new String[] {"", "&rYou do not have Permission", "&rto access this Item"}));
							menu.addMenuClickHandler(index, new MenuClickHandler() {
								
								@Override
								public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
									return false;
								}
							});
							index++;
						}
					}
					else {
						menu.addItem(index, sfitem.getItem());
						menu.addMenuClickHandler(index, new MenuClickHandler() {
							
							@Override
							public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
								if (survival) displayItem(p, item, true, experimental, 0);
								else p.getInventory().addItem(item);
								return false;
							}
						});
						index++;
					}
				}
			}
			
			menu.open(p);
		}		

		if (survival) {
			addToHistory(p, category.getURID());
		}
	}

	public static void addToHistory(Player p, URID urid) {
		List<URID> list = new ArrayList<URID>();
		if (history.containsKey(p.getUniqueId())) list = history.get(p.getUniqueId());
		list.add(urid);
		history.put(p.getUniqueId(), list);
	}
	
	private static URID getLastEntry(Player p, boolean remove) {
		List<URID> list = new ArrayList<URID>();
		if (history.containsKey(p.getUniqueId())) list = history.get(p.getUniqueId());
		if (remove && list.size() >= 1) {
			URID urid = list.get(list.size() - 1);
			urid.markDirty();
			list.remove(urid);
		}
		if (list.isEmpty()) history.remove(p.getUniqueId());
		else history.put(p.getUniqueId(), list);
		return list.isEmpty() ? null: list.get(list.size() - 1);
	}
	
	@SuppressWarnings("deprecation")
	public static void displayItem(Player p, final ItemStack item, boolean addToHistory, final boolean experimental, final int page) {
		if (item == null) return;

		final SlimefunItem sfItem = SlimefunItem.getByItem(item);
		if (sfItem == null) {
			if (!all_recipes) return;
		}
		
		ItemStack[] recipe = new ItemStack[9];
		ItemStack recipeType = null;
		ItemStack recipeOutput = item;
		
		ChestMenu menu = new ChestMenu("Slimefun Guide");
		
		menu.addMenuOpeningHandler(new MenuOpeningHandler() {
			
			@Override
			public void onOpen(Player p) {
				p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.7F, 0.7F);
			}
		});
		
		if (sfItem != null) {
			recipe = sfItem.getRecipe();
			recipeType = sfItem.getRecipeType().toItem();
			recipeOutput = sfItem.getCustomOutput() != null ? sfItem.getCustomOutput(): sfItem.getItem();
		}
		else {
			List<Recipe> recipes = new ArrayList<Recipe>();
			Iterator<Recipe> iterator = Bukkit.recipeIterator();
			while (iterator.hasNext()) {
				Recipe r = iterator.next();
				if (SlimefunManager.isItemSimiliar(new CustomItem(r.getResult(), 1), item, true) && r.getResult().getData().getData() == item.getData().getData()) recipes.add(r);
			}
			
			if (recipes.isEmpty()) return;
			 Recipe r = recipes.get(page);
			 
			 if (recipes.size() > page + 1) {
				 menu.addItem(1, new CustomItem(new MaterialData(Material.ENCHANTED_BOOK), "&7Next \u21E8", "", "�e�l! �rThere are multiple recipes for this Item"));
					menu.addMenuClickHandler(1, new MenuClickHandler() {
						
						@Override
						public boolean onClick(Player p, int slot, ItemStack stack, ClickAction action) {
							displayItem(p, item, false, experimental, page + 1);
							return false;
						}
					});
			 }
			 
			 if (r instanceof ShapedRecipe) {
					String[] shape = ((ShapedRecipe) r).getShape();
					for (int i = 0; i < shape.length; i++) {
			            for (int j = 0; j < shape[i].length(); j++) {
			            	ItemStack ingredient = ((ShapedRecipe) r).getIngredientMap().get(shape[i].charAt(j));
							if (ingredient != null) {
								MaterialData data = ingredient.getData();
								if (ingredient.getData().getData() < 0) data.setData((byte) 0);
								ingredient = data.toItemStack(ingredient.getAmount());
							}
							recipe[i * 3 + j] = ingredient;
			            }
			        }
					recipeType = RecipeType.SHAPED_RECIPE.toItem();
					recipeOutput = r.getResult();
				}
				else if (r instanceof ShapelessRecipe) {
			        List<ItemStack> ingredients = ((ShapelessRecipe) r).getIngredientList();
					for (int i = 0; i < ingredients.size(); i++) {
						ItemStack ingredient = ingredients.get(i);
						if (ingredient != null) {
							MaterialData data = ingredient.getData();
							if (ingredient.getData().getData() < 0) data.setData((byte) 0);
							ingredient = data.toItemStack(ingredient.getAmount());
						}
			            recipe[i] = ingredient;
			        }
					recipeType = RecipeType.SHAPELESS_RECIPE.toItem();
					recipeOutput = r.getResult();
				}
				else if (r instanceof FurnaceRecipe) {
					ItemStack ingredient = ((FurnaceRecipe) r).getInput();
					if (ingredient != null) {
						MaterialData data = ingredient.getData();
						if (ingredient.getData().getData() < 0) data.setData((byte) 0);
						ingredient = data.toItemStack(ingredient.getAmount());
					}
					recipe[4] = ingredient;
					
					recipeType = RecipeType.FURNACE.toItem();
					recipeOutput = r.getResult();
				}
		}
		

		
		if (addToHistory) addToHistory(p, sfItem != null ? sfItem.getURID(): URID.nextURID(item, true));
		
		if (history.containsKey(p.getUniqueId()) && history.get(p.getUniqueId()).size() > 1) {
			menu.addItem(0, new CustomItem(new MaterialData(Material.ENCHANTED_BOOK), "&7\u21E6 Back", "", "�rLeft Click: �7Go back to previous Page", "�rShift + left Click: �7Go back to Main Menu"));
			menu.addMenuClickHandler(0, new MenuClickHandler() {
				
				@Override
				public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
					if (action.isShiftClicked()) openMainMenu(p, true, experimental, 1);
					else {
						URID last = getLastEntry(p, true);
						if (URID.decode(last) instanceof Category) openCategory(p, (Category) URID.decode(last), true, 1, experimental);
						else if (URID.decode(last) instanceof SlimefunItem) displayItem(p, ((SlimefunItem) URID.decode(last)).getItem(), false, experimental, 0);
						else if (URID.decode(last) instanceof GuideHandler) ((GuideHandler) URID.decode(last)).run(p, true);
						else displayItem(p, (ItemStack) URID.decode(last), false, experimental, 0);
					}
					return false;
				}
			});
		}
		else {
			menu.addItem(0, new CustomItem(new MaterialData(Material.ENCHANTED_BOOK), "&7\u21E6 Back", "", "�rLeft Click: �7Go back to Main Menu"));
			menu.addMenuClickHandler(0, new MenuClickHandler() {
				
				@Override
				public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
					openMainMenu(p, true, experimental, 1);
					return false;
				}
			});
		}
		
		menu.addItem(3, Slimefun.hasUnlocked(p, recipe[0], false) ? recipe[0]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[0], false), 0, new String[] {"&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[0]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"}));
		menu.addMenuClickHandler(3, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				displayItem(p, item, true, experimental, 0);
				return false;
			}
		});
		
		menu.addItem(4, Slimefun.hasUnlocked(p, recipe[1], false) ? recipe[1]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[1], false), 0, new String[] {"&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[1]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"}));
		menu.addMenuClickHandler(4, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				displayItem(p, item, true, experimental, 0);
				return false;
			}
		});
		
		menu.addItem(5, Slimefun.hasUnlocked(p, recipe[2], false) ? recipe[2]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[2], false), 0, new String[] {"&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[2]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"}));
		menu.addMenuClickHandler(5, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				displayItem(p, item, true, experimental, 0);
				return false;
			}
		});
		
		if (sfItem != null) {
			if (Slimefun.getItemConfig().contains(sfItem.getName() + ".wiki")) {
				try {
					menu.addItem(8, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY2OTJmOTljYzZkNzgyNDIzMDQxMTA1NTM1ODk0ODQyOThiMmU0YTAyMzNiNzY3NTNmODg4ZTIwN2VmNSJ9fX0="), "�rView this Item in our Wiki �7(Slimefun Wiki)", "", "�7\u21E8 Click to open"));
					menu.addMenuClickHandler(8, new MenuClickHandler() {
						
						@Override
						public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
							p.closeInventory();
							p.sendMessage("");
							p.sendMessage("�7�o" + Slimefun.getItemConfig().getString(sfItem.getName() + ".wiki"));
							p.sendMessage("");
							return false;
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (Slimefun.getItemConfig().contains(sfItem.getName() + ".youtube")) {
				try {
					menu.addItem(7, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzNTNmZDBmODYzMTQzNTM4NzY1ODYwNzViOWJkZjBjNDg0YWFiMDMzMWI4NzJkZjExYmQ1NjRmY2IwMjllZCJ9fX0="), "�rDemonstration Video �7(Youtube)", "", "�7\u21E8 Click to watch"));
					menu.addMenuClickHandler(7, new MenuClickHandler() {
						
						@Override
						public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
							p.closeInventory();
							p.sendMessage("");
							p.sendMessage("�7�o" + Slimefun.getItemConfig().getString(sfItem.getName() + ".youtube"));
							p.sendMessage("");
							return false;
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		menu.addItem(10, recipeType);
		menu.addMenuClickHandler(10, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				return false;
			}
		});
		
		menu.addItem(12, Slimefun.hasUnlocked(p, recipe[3], false) ? recipe[3]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[3], false), 0, new String[] {"&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[3]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"}));
		menu.addMenuClickHandler(12, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				displayItem(p, item, true, experimental, 0);
				return false;
			}
		});
		
		menu.addItem(13, Slimefun.hasUnlocked(p, recipe[4], false) ? recipe[4]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[4], false), 0, new String[] {"&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[4]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"}));
		menu.addMenuClickHandler(13, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				displayItem(p, item, true, experimental, 0);
				return false;
			}
		});
		
		menu.addItem(14, Slimefun.hasUnlocked(p, recipe[5], false) ? recipe[5]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[5], false), 0, new String[] {"&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[5]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"}));
		menu.addMenuClickHandler(14, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				displayItem(p, item, true, experimental, 0);
				return false;
			}
		});
		
		menu.addItem(16, recipeOutput);
		menu.addMenuClickHandler(16, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				return false;
			}
		});
		
		menu.addItem(21, Slimefun.hasUnlocked(p, recipe[6], false) ? recipe[6]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[6], false), 0, new String[] {"&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[6]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"}));
		menu.addMenuClickHandler(21, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				displayItem(p, item, true, experimental, 0);
				return false;
			}
		});
		
		menu.addItem(22, Slimefun.hasUnlocked(p, recipe[7], false) ? recipe[7]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[7], false), 0, new String[] {"&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[7]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"}));
		menu.addMenuClickHandler(22, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				displayItem(p, item, true, experimental, 0);
				return false;
			}
		});
		
		menu.addItem(23, Slimefun.hasUnlocked(p, recipe[8], false) ? recipe[8]: new CustomItem(Material.BARRIER, StringUtils.formatItemName(recipe[8], false), 0, new String[] {"&4&lLOCKED", "", Slimefun.hasPermission(p, SlimefunItem.getByItem(recipe[8]), false) ? "&rNeeds to be unlocked elsewhere" : "&rNo Permission"}));
		menu.addMenuClickHandler(23, new MenuClickHandler() {
			
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				displayItem(p, item, true, experimental, 0);
				return false;
			}
		});
		
		if (sfItem != null) {
			
			if ((sfItem instanceof SlimefunMachine && ((SlimefunMachine) sfItem).getDisplayRecipes().size() > 0) || (sfItem instanceof SlimefunGadget && ((SlimefunGadget) sfItem).getRecipes().size() > 0)) {
				for (int i = 27; i < 36; i++) {
					menu.addItem(i, new CustomItem(Material.STAINED_GLASS_PANE, SlimefunItem.getByItem(item) instanceof SlimefunMachine ? "&7\u21E9 Recipes made in this Machine \u21E9": " ", 7));
					menu.addMenuClickHandler(i, new MenuClickHandler() {
						
						@Override
						public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
							return false;
						}
					});
				}
				
				List<ItemStack> recipes = SlimefunItem.getByItem(item) instanceof SlimefunMachine ? ((SlimefunMachine) SlimefunItem.getByItem(item)).getDisplayRecipes(): ((SlimefunGadget) SlimefunItem.getByItem(item)).getDisplayRecipes();
				int recipe_size = recipes.size();
				if (recipe_size > 18) recipe_size = 18;
				int inputs = -1, outputs = -1;
				
				for (int i = 0; i < recipe_size; i++) {
					int slot = 36;
					if (i % 2 == 1) {
						slot = slot + 9;
						outputs++;
					}
					else inputs++;
					
					int addition = (i % 2 == 0 ? inputs: outputs);
					
					menu.addItem(slot + addition, recipes.get(i));
					menu.addMenuClickHandler(slot + addition, new MenuClickHandler() {
						
						@Override
						public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
							return false;
						}
					});
				}
			}
			else if (sfItem instanceof AGenerator) {
				int slot = 27;
				for (MachineFuel fuel: ((AGenerator) sfItem).getFuelTypes()) {
					if (slot > 54) break;
					ItemStack fItem = fuel.getInput().clone();
					ItemMeta im = fItem.getItemMeta();
					List<String> lore = new ArrayList<String>();
					lore.add("�8\u21E8 �7Lasts " + getTimeLeft(fuel.getTicks() / 2));
					lore.add("�8\u21E8 �e\u26A1 �7" + (((AGenerator) sfItem).getEnergyProduction() * 2) + " J/s");
					lore.add("�8\u21E8 �e\u26A1 �7" + DoubleHandler.getFancyDouble(fuel.getTicks() * ((AGenerator) sfItem).getEnergyProduction()) + " J in total");
					im.setLore(lore);
					fItem.setItemMeta(im);
					menu.addItem(slot, fItem);
					menu.addMenuClickHandler(slot, new MenuClickHandler() {
						
						@Override
						public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
							return false;
						}
					});
					slot++;
				}
			}
			else if (sfItem instanceof NuclearReactor) {
				int slot = 27;
				for (MachineFuel fuel: ((NuclearReactor) sfItem).getFuelTypes()) {
					if (slot > 54) break;
					ItemStack fItem = fuel.getInput().clone();
					ItemMeta im = fItem.getItemMeta();
					List<String> lore = new ArrayList<String>();
					lore.add("�8\u21E8 �7Lasts " + getTimeLeft(fuel.getTicks() / 2));
					lore.add("�8\u21E8 �e\u26A1 �7" + (((NuclearReactor) sfItem).getEnergyProduction() * 2) + " J/s");
					lore.add("�8\u21E8 �e\u26A1 �7" + DoubleHandler.getFancyDouble(fuel.getTicks() * ((NuclearReactor) sfItem).getEnergyProduction()) + " J in total");
					im.setLore(lore);
					fItem.setItemMeta(im);
					menu.addItem(slot, fItem);
					menu.addMenuClickHandler(slot, new MenuClickHandler() {
						
						@Override
						public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
							return false;
						}
					});
					slot++;
				}
			}
		}
		
		menu.build().open(p);
	}
	
	public static void clearHistory(UUID uuid) {
		if (!history.containsKey(uuid)) return;
		
		for (URID urid: history.get(uuid)) {
			urid.markDirty();
		}
		history.remove(uuid);
	}
	
	private static String getTimeLeft(int l) {
		String timeleft = "";
        final int minutes = (int) (l / 60L);
        if (minutes > 0) {
            timeleft = String.valueOf(timeleft) + minutes + "m ";
        }
        l -= minutes * 60;
        final int seconds = (int)l;
        timeleft = String.valueOf(timeleft) + seconds + "s";
        return "�7" + timeleft;
	}

}
