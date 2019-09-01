package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionManager;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class StormStaff extends SlimefunItem {
	
	private final static int MAX_USES = 8;
	
	public StormStaff(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe, getCraftedOutput());
	}
	
	private static ItemStack getCraftedOutput() {
		ItemStack item = SlimefunItems.STAFF_STORM.clone();
		ItemMeta im = item.getItemMeta();
		List<String> lore = im.getLore();
		
		lore.set(4, ChatColor.translateAlternateColorCodes('&', "&e" + MAX_USES + " Uses &7left"));
		
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	@Override
	public void register(boolean slimefun) {
		addItemHandler(new ItemInteractionHandler() {

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
				//Not checking if lores equals because we need a special one for that.
				if (SlimefunManager.isItemSimiliar(item, SlimefunItems.STAFF_STORM, false)) {

					if (!item.hasItemMeta()) return false;
					ItemMeta itemM = item.getItemMeta();
					if (!itemM.hasLore()) return false;
					List<String> itemML = itemM.getLore();

					ItemStack SFitem = SlimefunItems.STAFF_STORM;
					ItemMeta SFitemM = SFitem.getItemMeta();
					List<String> SFitemML = SFitemM.getLore();
					
					if (itemML.size() < 6) {
						// Index 1 and 3 in SlimefunItems.STAFF_STORM has lores with words and stuff so we check for them.
						if (itemML.get(1).equals(SFitemML.get(1)) && itemML.get(3).equals(SFitemML.get(3))) {
							if (p.getFoodLevel() >= 4 || p.getGameMode() == GameMode.CREATIVE) {
								// Get a target block with max. 30 blocks of distance
								Location loc = p.getTargetBlock(null, 30).getLocation();
								
								if (loc.getWorld() != null && loc.getChunk().isLoaded()) {
									if (new ProtectionManager(Bukkit.getServer()).hasPermission(p, loc, ProtectionModule.Action.PVP)) {
										loc.getWorld().strikeLightning(loc);

										if (p.getInventory().getItemInMainHand().getType() != Material.SHEARS && p.getGameMode() != GameMode.CREATIVE) {
											FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, p.getFoodLevel() - 4);
											Bukkit.getPluginManager().callEvent(event);
											p.setFoodLevel(event.getFoodLevel());
										}
										
										for (int i = MAX_USES; i > 0; i--) {
											if (i == 1 && ChatColor.translateAlternateColorCodes('&', "&e1 Use &7left").equals(itemML.get(4))) {
												e.setCancelled(true);
												p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
												item.setAmount(0);
												return true;
											}
											else if (ChatColor.translateAlternateColorCodes('&', "&e" + i + " Uses &7left").equals(itemML.get(4))) {
												itemML.set(4, ChatColor.translateAlternateColorCodes('&', "&e" + (i - 1) + " " + (i > 2 ? "Uses": "Use") + " &7left"));
												e.setCancelled(true);
												
												// Saving the changes to lore and item.
												itemM.setLore(itemML);
												item.setItemMeta(itemM);
												
												if (e.getParentEvent().getHand() == EquipmentSlot.HAND) {
													p.getInventory().setItemInMainHand(item);
												} 
												else {
													p.getInventory().setItemInOffHand(item);
												}
												
												return true;
											}
										}
										
										return false;
									} 
									else {
										Messages.local.sendTranslation(p, "messages.no-pvp", true);
									}
								}
							} 
							else {
								Messages.local.sendTranslation(p, "messages.hungry", true);
							}
							return true;
						}
					}
				}
				return false;
			}
		});
		super.register(slimefun);
	}

}
