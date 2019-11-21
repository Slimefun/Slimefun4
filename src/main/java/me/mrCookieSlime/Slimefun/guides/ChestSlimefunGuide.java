package me.mrCookieSlime.Slimefun.guides;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.MultiBlockMachine;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import io.github.thebusybiscuit.cscorelib2.chat.ChatInput;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SeasonalCategory;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.api.GuideHandler;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class ChestSlimefunGuide implements ISlimefunGuide {
	
	private static final int CATEGORY_SIZE = 36;

	@Override
	public SlimefunGuideLayout getLayout() {
		return SlimefunGuideLayout.CHEST;
	}

	@Override
	public ItemStack getItem() {
		return new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&a粘液科技指南 &7(箱子界面)", "", "&e右键 &8\u21E8 &7浏览物品", "&eShift + 右键 &8\u21E8 &7打开设置菜单");
	}

	@Override
	public void openMainMenu(PlayerProfile profile, boolean survival, int page) {
		Player p = profile.getPlayer();
		if (p == null) return;

		if (survival) {
			profile.getGuideHistory().clear();
		}
		
		final ChestMenu menu = create();

		List<Category> categories = SlimefunPlugin.getUtilities().enabledCategories;
		List<GuideHandler> handlers = SlimefunPlugin.getUtilities().guideHandlers.values().stream().flatMap(List::stream).collect(Collectors.toList());

		int index = 9;
		int pages = 1;

		fillInv(profile, p, menu, survival);

		int target = (CATEGORY_SIZE * (page - 1)) - 1;

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
				Category category = categories.get(target);
				boolean locked = true;

				for (SlimefunItem item: category.getItems()) {
					if (Slimefun.isEnabled(p, item, false)) {
						locked = false;
						break;
					}
				}

				if (!locked) {
					if (!(category instanceof LockedCategory)) {
						if (!(category instanceof SeasonalCategory)) {
							menu.addItem(index, category.getItem());
							menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
								openCategory(profile, category, survival, 1);
								return false;
							});
							index++;
						}
						else {
							if (((SeasonalCategory) category).isUnlocked()) {
								menu.addItem(index, category.getItem());
								menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
									openCategory(profile, category, survival, 1);
									return false;
								});
								index++;
							}
						}
					}
					else if (!survival || ((LockedCategory) category).hasUnlocked(p, profile)) {
						menu.addItem(index, category.getItem());
						menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
							openCategory(profile, category, survival, 1);
							return false;
						});
						index++;
					}
					else {
						List<String> parents = new ArrayList<>();
						parents.add("");
						parents.add(ChatColor.translateAlternateColorCodes('&', "&r你需要先解锁"));
						parents.add(ChatColor.translateAlternateColorCodes('&', "&r以下分类的所有物品:"));
						parents.add("");

						for (Category parent : ((LockedCategory) category).getParents()) {
							parents.add(parent.getItem().getItemMeta().getDisplayName());
						}

						menu.addItem(index, new CustomItem(Material.BARRIER, "&4已锁定 &7- &r" + category.getItem().getItemMeta().getDisplayName(), parents.toArray(new String[0])));
						menu.addMenuClickHandler(index, (pl, slot, item, action) -> false);
						index++;
					}
				}
			}
		}

		final int finalPages = pages;

		menu.addItem(46, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r\u21E6 上一页", "", "&7(" + page + " / " + pages + ")"));
		menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
			int next = page - 1;
			if (next < 1) next = finalPages;
			if (next != page) openMainMenu(profile, survival, next);
			return false;
		});

		menu.addItem(52, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r下一页 \u21E8", "", "&7(" + page + " / " + pages + ")"));
		menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
			int next = page + 1;
			if (next > finalPages) next = 1;
			if (next != page) openMainMenu(profile, survival, next);
			return false;
		});

		menu.open(p);
	}

	@Override
	public void openCategory(PlayerProfile profile, Category category, boolean survival, int page) {
		Player p = profile.getPlayer();
		if (p == null) return;
		
		if (survival) {
			profile.getGuideHistory().add(category);
		}
		
		final ChestMenu menu = create();
		
		int index = 9;
		final int pages = (category.getItems().size() - 1) / CATEGORY_SIZE + 1;
		for (int i = 0; i < 4; i++) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i, (pl, slot, item, action) -> false);
		}

		menu.addItem(4, new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&7\u21E6 返回"));
		menu.addMenuClickHandler(4, (pl, slot, item, action) -> {
			openMainMenu(profile, survival, 1);
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

		menu.addItem(46, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r\u21E6 上一页", "", "&7(" + page + " / " + pages + ")"));
		menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
			int next = page - 1;
			if (next < 1) next = pages;
			if (next != page) openCategory(profile, category, survival, next);
			return false;
		});

		menu.addItem(52, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r下一页 \u21E8", "", "&7(" + page + " / " + pages + ")"));
		menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
			int next = page + 1;
			if (next > pages) next = 1;
			if (next != page) openCategory(profile, category, survival, next);
			return false;
		});

		int categoryIndex = CATEGORY_SIZE * (page - 1);
		for (int i = 0; i < CATEGORY_SIZE; i++) {
			int target = categoryIndex + i;
			if (target >= category.getItems().size()) break;
			final SlimefunItem sfitem = category.getItems().get(target);

			if (Slimefun.isEnabled(p, sfitem, false)) {
				final Research research = sfitem.getResearch();
				if (survival && research != null && !profile.hasUnlocked(research)) {
					if (Slimefun.hasPermission(p, sfitem, false)) {
						menu.addItem(index, new CustomItem(Material.BARRIER, "&r" + ItemUtils.getItemName(sfitem.getItem()), "&4&l已锁定", "", "&a> 单击解锁", "", "&7耗费经验: &b" + research.getCost() + " 级"));
						menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
							if (!Research.isResearching(pl)) {
								if (research.canUnlock(pl)) {

									if (profile.hasUnlocked(research)) {
										openCategory(profile, category, true, page);
									}
									else {
										if (!(pl.getGameMode() == GameMode.CREATIVE && SlimefunPlugin.getSettings().researchesFreeInCreative)) {
											pl.setLevel(pl.getLevel() - research.getCost());
										}

										if (pl.getGameMode() == GameMode.CREATIVE) {
											research.unlock(pl, SlimefunPlugin.getSettings().researchesFreeInCreative);
											openCategory(profile, category, survival, page);
										}
										else {
											research.unlock(pl, false);
											Slimefun.runSync(() -> openCategory(profile, category, survival, page), 103L);
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
						if (survival) {
							displayItem(profile, sfitem, true);
						}
						else {
							if (sfitem instanceof MultiBlockMachine) {
								SlimefunPlugin.getLocal().sendMessage(pl, "guide.cheat.no-multiblocks");
							}
							else {
								pl.getInventory().addItem(sfitem.getItem().clone());
							}
						}
						return false;
					});
					index++;
				}
			}
		}

		menu.open(p);
	}

	@Override
	public void openSearch(PlayerProfile profile, String input, boolean survival, boolean addToHistory) {
		Player p = profile.getPlayer();
		if (p == null) return;
		
		final ChestMenu menu = new ChestMenu("正在搜索 " + shorten("", input));
		String searchTerm = input.toLowerCase();
		
		if (addToHistory) {
			profile.getGuideHistory().add(searchTerm);
		}
		
		menu.setEmptySlotsClickable(false);
		fillInv(profile, p, menu, survival);
		addBackButton(menu, 1, profile, survival);

		int index = 9;
		// Find items and add them
		for (SlimefunItem item : SlimefunItem.list()) {
			final String itemName = ChatColor.stripColor(item.getItemName()).toLowerCase();

			if (index == 44) break;
			
			if (!itemName.isEmpty()) {
				if (itemName.equals(searchTerm) || itemName.contains(searchTerm)) {
					menu.addItem(index, item.getItem());
					menu.addMenuClickHandler(index, (pl, slot, itm, action) -> {
						if (!survival) {
							pl.getInventory().addItem(item.getItem().clone());
						}
						else {
							displayItem(profile, item, true);
						}

						return false;
					});

					index++;
				}
			}
		}

		menu.open(p);
	}

    @Override
    public void displayItem(PlayerProfile profile, ItemStack item, boolean addToHistory) {
        Player p = profile.getPlayer();
        if (p == null) return;

        if (item == null || item.getType() == Material.AIR) return;

        final SlimefunItem sfItem = SlimefunItem.getByItem(item);

        if (sfItem != null) {
            displayItem(profile, sfItem, addToHistory);
            return;
        }

        if (!SlimefunPlugin.getSettings().guideShowVanillaRecipes) {
            return;
        }

        Recipe r = null;
        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            Recipe current = iterator.next();
            if (SlimefunManager.isItemSimiliar(new CustomItem(current.getResult(), 1), item, true)) {
                r = current;
                break;
            }
        }

        if (r == null) {
            return;
        }

        ItemStack[] recipe = new ItemStack[9];
        RecipeType recipeType = null;
        ItemStack result = null;

        if (r instanceof ShapedRecipe) {
            String[] shape = ((ShapedRecipe) r).getShape();
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length(); j++) {
                    recipe[i * 3 + j] = ((ShapedRecipe) r).getIngredientMap().get(shape[i].charAt(j));
                }
            }
            recipeType = RecipeType.SHAPED_RECIPE;
            result = r.getResult();
        }
        else if (r instanceof ShapelessRecipe) {
            List<ItemStack> ingredients = ((ShapelessRecipe) r).getIngredientList();
            for (int i = 0; i < ingredients.size(); i++) {
                recipe[i] = ingredients.get(i);
            }
            recipeType = RecipeType.SHAPELESS_RECIPE;
            result = r.getResult();
        }
        else if (r instanceof CookingRecipe) {
            recipe[4] = ((CookingRecipe<?>) r).getInput();

            recipeType = RecipeType.FURNACE;
            result = r.getResult();
        }

        if (recipeType == null) {
            return;
        }

        ChestMenu menu = create();
        displayItem(menu, profile, p, item, result, recipeType, recipe, addToHistory);
        menu.open(p);
    }
	
	@Override
	public void displayItem(PlayerProfile profile, SlimefunItem item, boolean addToHistory) {
		Player p = profile.getPlayer();
		if (p == null) return;
		
		ItemStack result = item.getRecipeOutput() != null ? item.getRecipeOutput(): item.getItem();
		RecipeType recipeType = item.getRecipeType();
		ItemStack[] recipe = item.getRecipe();
		
		ChestMenu menu = create();
		
		if (item.hasWiki()) {
			try {
				menu.addItem(8, new CustomItem(Material.KNOWLEDGE_BOOK, "&rWiki 界面 &7(Slimefun Wiki)", "", "&7\u21E8 单击打开"));
				menu.addMenuClickHandler(8, (pl, slot, itemstack, action) -> {
					pl.closeInventory();
					pl.sendMessage("");
					pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&o" + item.getWiki()));
					pl.sendMessage("");
					return false;
				});
			} catch (Exception x) {
				Slimefun.getLogger().log(Level.SEVERE, "An Error occured while adding a Wiki Page for Slimefun " + Slimefun.getVersion(), x);
			}
		}

		if (Slimefun.getItemConfig().contains(item.getID() + ".youtube")) {
			try {
				menu.addItem(7, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQzNTNmZDBmODYzMTQzNTM4NzY1ODYwNzViOWJkZjBjNDg0YWFiMDMzMWI4NzJkZjExYmQ1NjRmY2IwMjllZCJ9fX0="), "&r演示视频 &7(Youtube)", "", "&7\u21E8 单击观看"));
				menu.addMenuClickHandler(7, (pl, slot, itemstack, action) -> {
					pl.closeInventory();
					pl.sendMessage("");
					pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&o" + Slimefun.getItemConfig().getString(item.getID() + ".youtube")));
					pl.sendMessage("");
					return false;
				});
			} catch (Exception x) {
				Slimefun.getLogger().log(Level.SEVERE, "An Error occured while adding a Youtube Video for Slimefun " + Slimefun.getVersion(), x);
			}
		}
		
		displayItem(menu, profile, p, item, result, recipeType, recipe, addToHistory);

		if (item instanceof RecipeDisplayItem) {
			displayRecipes(profile, menu, (RecipeDisplayItem) item, 0);
		}
		
		menu.open(p);
	}
	
	private void displayItem(ChestMenu menu, PlayerProfile profile, Player p, Object obj, ItemStack output, RecipeType recipeType, ItemStack[] recipe, boolean addToHistory) {
		LinkedList<Object> history = profile.getGuideHistory();
		boolean isSlimefunRecipe = obj instanceof SlimefunItem;

		if (addToHistory) {
			history.add(obj);
		}
		
		addBackButton(menu, 0, profile, true);

		menu.addItem(3, getDisplayItem(p, isSlimefunRecipe, recipe[0]));
		menu.addMenuClickHandler(3, (pl, slot, itemstack, action) -> {
			displayItem(profile, itemstack, true);
			return false;
		});

		menu.addItem(4, getDisplayItem(p, isSlimefunRecipe, recipe[1]));
		menu.addMenuClickHandler(4, (pl, slot, itemstack, action) -> {
			displayItem(profile, itemstack, true);
			return false;
		});

		menu.addItem(5, getDisplayItem(p, isSlimefunRecipe, recipe[2]));
		menu.addMenuClickHandler(5, (pl, slot, itemstack, action) -> {
			displayItem(profile, itemstack, true);
			return false;
		});

		menu.addItem(10, recipeType.toItem());
		menu.addMenuClickHandler(10, (pl, slot, itemstack, action) -> false);

		menu.addItem(12, getDisplayItem(p, isSlimefunRecipe, recipe[3]));
		menu.addMenuClickHandler(12, (pl, slot, itemstack, action) -> {
			displayItem(profile, itemstack, true);
			return false;
		});

		menu.addItem(13, getDisplayItem(p, isSlimefunRecipe, recipe[4]));
		menu.addMenuClickHandler(13, (pl, slot, itemstack, action) -> {
			displayItem(profile, itemstack, true);
			return false;
		});

		menu.addItem(14, getDisplayItem(p, isSlimefunRecipe, recipe[5]));
		menu.addMenuClickHandler(14, (pl, slot, itemstack, action) -> {
			displayItem(profile, itemstack, true);
			return false;
		});

		menu.addItem(16, output);
		menu.addMenuClickHandler(16, (pl, slot, itemstack, action) -> false);

		menu.addItem(21, getDisplayItem(p, isSlimefunRecipe, recipe[6]));
		menu.addMenuClickHandler(21, (pl, slot, itemstack, action) -> {
			displayItem(profile, itemstack, true);
			return false;
		});

		menu.addItem(22, getDisplayItem(p, isSlimefunRecipe, recipe[7]));
		menu.addMenuClickHandler(22, (pl, slot, itemstack, action) -> {
			displayItem(profile, itemstack, true);
			return false;
		});

		menu.addItem(23, getDisplayItem(p, isSlimefunRecipe, recipe[8]));
		menu.addMenuClickHandler(23, (pl, slot, itemstack, action) -> {
			displayItem(profile, itemstack, true);
			return false;
		});
	}

	private void fillInv(PlayerProfile profile, Player p, ChestMenu menu, boolean survival) {
		for (int i = 0; i < 9; i++) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i, (pl, s, itemstack, action) -> false);
		}

		/*
		// Settings Panel
		menu.addItem(1, new CustomItem(Material.ENCHANTED_BOOK, "&eSettings / Info", "", "&7\u21E8 Click to see more"));
		menu.addMenuClickHandler(1, (player, i, itemStack, clickAction) -> {
			SlimefunGuide.openSettings(player, getItem());
			return false;
		});

		// Stats
		menu.addItem(4, new CustomItem(SkullItem.fromPlayer(p), "&7Player Stats: &e" + p.getName(), "", "&7Progress: &a" + Math.round(((profile.getResearches().size() * 100.0F) / Research.list().size()) * 100.0F) / 100.0F + "% &e(" + profile.getResearches().size() + " / " + Research.list().size() + ")", "", "&7\u21E8 Click for a full summary"));
		menu.addMenuClickHandler(4, (player, i, itemStack, clickAction) -> {
			player.closeInventory();
			profile.sendStats(player);
			return false;
		});
		*/
	
		// Search feature!
		menu.addItem(7, new CustomItem(Material.NAME_TAG, SlimefunPlugin.getLocal().getMessage("guide.search.name"), SlimefunPlugin.getLocal().getMessagesArray("guide.search.lore")));
		menu.addMenuClickHandler(7, (player, i, itemStack, clickAction) -> {
			player.closeInventory();
			SlimefunPlugin.getLocal().sendMessage(player, "search.message");

			ChatInput.waitForPlayer(SlimefunPlugin.instance, player, msg ->
				SlimefunGuide.openSearch(profile, msg, survival, true)
			);

			return false;
		});

		for (int i = 45; i < 54; i++) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i, (pl, s, itemstack, action) -> false);
		}
	}

	private void addBackButton(ChestMenu menu, int slot, PlayerProfile profile, boolean survival) {
		List<Object> playerHistory = profile.getGuideHistory();
		
		if (playerHistory.size() > 1) {

			menu.addItem(slot, new CustomItem(new ItemStack(Material.ENCHANTED_BOOK),
				"&7\u21E6 返回", "",
				"&r左键: &7返回上一页",
				"&rShift + 左键: &7返回主菜单")
			);

			menu.addMenuClickHandler(slot, (pl, s, is, action) -> {
				if (action.isShiftClicked()) {
					openMainMenu(profile, survival, 1);
				}
				else {
					Object last = getLastEntry(profile, true);
					handleHistory(profile, last, survival);
				}
				return false;
			});

		}
		else {
			menu.addItem(slot, new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&7\u21E6 返回", "", "&r左键: &7返回主菜单"));
			menu.addMenuClickHandler(slot, (pl, s, is, action) -> {
				openMainMenu(profile, survival, 1);
				return false;
			});
		}
	}
	
	private static ItemStack getDisplayItem(Player p, boolean isSlimefunRecipe, ItemStack item) {
		if (isSlimefunRecipe) {
			SlimefunItem slimefunItem = SlimefunItem.getByItem(item);
			if (slimefunItem == null) return item;

			String lore = Slimefun.hasPermission(p, slimefunItem, false) ? "&r需要在别处解锁" : "&r没有权限";
			return Slimefun.hasUnlocked(p, slimefunItem, false) ? item: new CustomItem(Material.BARRIER, ItemUtils.getItemName(item), "&4&l已锁定", "", lore);
		}
		else {
			return item;
		}
	}
	
	private void displayRecipes(PlayerProfile profile, ChestMenu menu, RecipeDisplayItem sfItem, int page) {
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
				menu.replaceExistingItem(28, new CustomItem(Material.LIME_STAINED_GLASS_PANE, "&a\u21E6 上一页"));
				menu.addMenuClickHandler(28, (pl, slot, itemstack, action) -> {
					displayRecipes(profile, menu, sfItem, page - 1);
					pl.playSound(pl.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
					return false;
				});
			}

			if (recipes.size() > (18 * (page + 1))) {
				menu.replaceExistingItem(34, new CustomItem(Material.LIME_STAINED_GLASS_PANE, "&a下一页 \u21E8"));
				menu.addMenuClickHandler(34, (pl, slot, itemstack, action) -> {
					displayRecipes(profile, menu, sfItem, page + 1);
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
							displayItem(profile, itemstack, true);
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
	
	private static ChestMenu create() {
		ChestMenu menu = new ChestMenu("粘液科技指南");

		menu.setEmptySlotsClickable(false);
		menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1));
		return menu;
	}

}
