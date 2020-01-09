package io.github.thebusybiscuit.slimefun4.core.guide;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.PlayerRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage;
import me.mrCookieSlime.CSCoreLibPlugin.general.Chat.TellRawMessage.HoverAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.CustomBookOverlay;
import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SeasonalCategory;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.GuideHandler;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class BookSlimefunGuide implements ISlimefunGuide {

	@Override
	public SlimefunGuideLayout getLayout() {
		return SlimefunGuideLayout.BOOK;
	}

	@Override
	public ItemStack getItem() {
		return new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&aSlimefun Guide &7(Book GUI)", "", "&eRight Click &8\u21E8 &7Browse Items", "&eShift + Right Click &8\u21E8 &7Open Settings / Credits");
	}

	@Override
	public void openMainMenu(PlayerProfile profile, boolean survival, int page) {
		Player p = profile.getPlayer();
		if (p == null) return;

		if (survival) {
			profile.getGuideHistory().clear();
		}
		
		List<TellRawMessage> pages = new ArrayList<>();
		List<String> texts = new ArrayList<>();
		List<String> tooltips = new ArrayList<>();
		List<PlayerRunnable> actions = new ArrayList<>();

		int tier = 0;

		for (Category category : Category.list()) {
			boolean locked = true;

			for (SlimefunItem item : category.getItems()) {
				if (Slimefun.isEnabled(p, item, false)) {
					locked = false;
					break;
				}
			}

			if (!locked) {
				if (tier < category.getTier()) {
					if (survival) {
						for (GuideHandler handler : Slimefun.getGuideHandlers(tier)) {
							handler.addEntry(texts, tooltips);
							actions.add(new PlayerRunnable(2) {

								@Override
								public void run(Player p) {
									handler.run(p, survival, true);
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
					
					texts.add(ChatColors.color("&8\u21E8 &6Tier " + tier));
					tooltips.add(null);
					actions.add(null);
				}
				if (category instanceof LockedCategory && !((LockedCategory) category).hasUnlocked(p, profile)) {
					StringBuilder parents = new StringBuilder(ChatColors.color("&4&lLOCKED\n\n&7In order to unlock this Category,\n&7you need to unlock all Items from\n&7the following Categories first:\n"));

					for (Category parent : ((LockedCategory) category).getParents()) {
						parents.append(ChatColors.color("\n&c" + ItemUtils.getItemName(parent.getItem())));
					}

					texts.add(ChatColors.color(shorten("&c" , ItemUtils.getItemName(category.getItem()))));
					tooltips.add(parents.toString());
					actions.add(null);
				}
				else if (category instanceof SeasonalCategory) {
					if (((SeasonalCategory) category).isUnlocked()) {
						texts.add(ChatColors.color(shorten("&a", ItemUtils.getItemName(category.getItem()))));
						tooltips.add(ChatColors.color("&eClick to open the following Category:\n" + ItemUtils.getItemName(category.getItem())));
						actions.add(new PlayerRunnable(1) {
							@Override
							public void run(final Player p) {
								Slimefun.runSync(() -> openCategory(profile, category, survival, 1), 1L);
							}
						});
					}
				}
				else {
					texts.add(ChatColors.color(shorten("&a", ItemUtils.getItemName(category.getItem()))));
					tooltips.add(ChatColors.color("&eClick to open the following Category:\n" + ItemUtils.getItemName(category.getItem())));
					actions.add(new PlayerRunnable(1) {
						@Override
						public void run(final Player p) {
							Slimefun.runSync(() -> openCategory(profile, category, survival, 1), 1L);
						}
					});
				}
			}
		}

		if (survival) {
			for (GuideHandler handler : Slimefun.getGuideHandlers(tier)) {
				handler.addEntry(texts, tooltips);
				actions.add(new PlayerRunnable(2) {
					@Override
					public void run(Player p) {
						handler.run(p, survival, true);
					}
				});
			}
		}

		for (int i = 0; i < texts.size(); i = i + 10) {
			TellRawMessage pageMessage = new TellRawMessage();
			pageMessage.addText(ChatColors.color("&b&l- Slimefun Guide -\n\n"));
			
			for (int j = i; j < texts.size() && j < i + 10; j++) {
				pageMessage.addText(texts.get(j) + "\n");
				if (tooltips.get(j) != null) pageMessage.addHoverEvent(HoverAction.SHOW_TEXT, tooltips.get(j));
				if (actions.get(j) != null) pageMessage.addClickEvent(actions.get(j));
			}
			
			pages.add(pageMessage);
		}

		new CustomBookOverlay("Slimefun Guide", "TheBusyBiscuit", pages.toArray(new TellRawMessage[0])).open(p);
	}

	@Override
	public void openCategory(PlayerProfile profile, Category category, boolean survival, int page) {
		Player p = profile.getPlayer();
		if (p == null) return;
		
		if (category.getItems().size() < 250) {
			
			if (survival) {
				profile.getGuideHistory().add(category);
			}
			
			List<TellRawMessage> pages = new ArrayList<>();
			List<String> texts = new ArrayList<>();
			List<String> tooltips = new ArrayList<>();
			List<PlayerRunnable> actions = new ArrayList<>();

			for (SlimefunItem item : category.getItems()) {
				if (Slimefun.hasPermission(p, item, false)) {
					if (Slimefun.isEnabled(p, item, false)) {
						if (survival && !Slimefun.hasUnlocked(p, item, false) && item.getResearch() != null) {
						    Research research = item.getResearch();

							texts.add(ChatColors.color(shorten("&7", item.getItemName())));
							tooltips.add(ChatColors.color(item.getItemName() + "\n&c&lLOCKED\n\n&7Cost: " + (p.getLevel() >= research.getCost() ? "&b": "&4") + research.getCost() + " Levels\n\n&a> Click to unlock"));
							actions.add(new PlayerRunnable(2) {

								@Override
								public void run(final Player p) {
									if (!Research.isResearching(p)) {
										if (research.canUnlock(p)) {
											if (profile.hasUnlocked(research)) {
												openCategory(profile, category, true, page);
											}
											else {
												if (!(p.getGameMode() == GameMode.CREATIVE && SlimefunPlugin.getSettings().researchesFreeInCreative)) {
													p.setLevel(p.getLevel() - research.getCost());
												}

												if (p.getGameMode() == GameMode.CREATIVE) {
													research.unlock(p, true);

													Slimefun.runSync(() -> openCategory(profile, category, survival, page), 1L);
												}
												else {
													research.unlock(p, false);
													Slimefun.runSync(() -> openCategory(profile, category, survival, page), 103L);
												}
											}
										} else SlimefunPlugin.getLocal().sendMessage(p, "messages.not-enough-xp", true);
									}
								}
							});
						}
						else {
							texts.add(ChatColors.color(shorten("&a", item.getItemName())));

							StringBuilder tooltip = new StringBuilder();

							tooltip.append(item.getItemName());

							if (item.getItem().hasItemMeta() && item.getItem().getItemMeta().hasLore()) {
								for (String line : item.getItem().getItemMeta().getLore()) {
									tooltip.append("\n" + line);
								}
							}

							tooltip.append(ChatColors.color("\n\n&e&oClick for more Info"));

							tooltips.add(tooltip.toString());
							actions.add(new PlayerRunnable(2) {

								@Override
								public void run(Player p) {
									displayItem(profile, item, true);
								}
							});
						}
					}
				}
				else {
					texts.add(ChatColors.color(shorten("&4", ItemUtils.getItemName(item.getItem()))));
					tooltips.add(ChatColors.color("&cNo Permission!"));
					actions.add(null);
				}
			}

			for (int i = 0; i < texts.size(); i = i + 10) {
				TellRawMessage pageMessage = new TellRawMessage();
				pageMessage.addText(ChatColors.color("&b&l- Slimefun Guide -\n\n"));

				for (int j = i; j < texts.size() && j < i + 10; j++) {
					pageMessage.addText(texts.get(j) + "\n");
					if (tooltips.get(j) != null) pageMessage.addHoverEvent(HoverAction.SHOW_TEXT, tooltips.get(j));
					if (actions.get(j) != null) pageMessage.addClickEvent(actions.get(j));
				}

				pageMessage.addText("\n");
				pageMessage.addText(ChatColors.color("&6\u21E6 &lBack"));
				pageMessage.addHoverEvent(HoverAction.SHOW_TEXT, ChatColors.color("&eClick to go back to the Category Overview"));
				pageMessage.addClickEvent(new PlayerRunnable(2) {

					@Override
					public void run(final Player p) {
						openMainMenu(profile, survival, 1);
					}

				});
				pages.add(pageMessage);
			}

			new CustomBookOverlay("Slimefun Guide", "TheBusyBiscuit", pages.toArray(new TellRawMessage[0])).open(p);
		}
		else {
			p.sendMessage(ChatColor.RED + "That Category is too big to open :/");
		}
	}

	@Override
	public void openSearch(PlayerProfile profile, String input, boolean survival, boolean addToHistory) {
		SlimefunGuide.openSearch(profile, input, survival, addToHistory);
	}

	@Override
	public void displayItem(PlayerProfile profile, ItemStack item, boolean addToHistory) {
		SlimefunGuide.displayItem(profile, item, addToHistory);
	}

	@Override
	public void displayItem(PlayerProfile profile, SlimefunItem item, boolean addToHistory) {
		SlimefunGuide.displayItem(profile, item, addToHistory);
	}

}
