package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class VanillaMachinesListener implements Listener {

	public VanillaMachinesListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onGrindstone(InventoryClickEvent e) {
		if (e.getRawSlot() == 2 && e.getWhoClicked() instanceof Player && e.getInventory().getType() == InventoryType.GRINDSTONE) {
			ItemStack item1 = e.getInventory().getContents()[0];
			ItemStack item2 = e.getInventory().getContents()[1];
			
			if (checkForUnallowedItems(item1, item2)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onCraft(CraftItemEvent e) {
		for (ItemStack item : e.getInventory().getContents()) {
			SlimefunItem sfItem = SlimefunItem.getByItem(item);

			if (sfItem != null && !sfItem.isUseableInWorkbench()) {
				e.setCancelled(true);
				SlimefunPlugin.getLocal().sendMessage((Player) e.getWhoClicked(), "workbench.not-enhanced", true);
				break;
			}
		}
	}

	@EventHandler
	public void onPrepareCraft(PrepareItemCraftEvent e) {
		for (ItemStack item : e.getInventory().getContents()) {
			SlimefunItem sfItem = SlimefunItem.getByItem(item);

			if (sfItem != null && !sfItem.isUseableInWorkbench()) {
				e.getInventory().setResult(null);
				break;
			}
		}
	}

	@EventHandler
	public void onAnvil(InventoryClickEvent e) {
		if (e.getRawSlot() == 2 && e.getWhoClicked() instanceof Player && e.getInventory().getType() == InventoryType.ANVIL) {
			ItemStack item1 = e.getInventory().getContents()[0];
			ItemStack item2 = e.getInventory().getContents()[1];
			
			if (!SlimefunManager.isItemSimilar(item1, SlimefunItems.ELYTRA, true) && checkForUnallowedItems(item1, item2)) {
				e.setCancelled(true);
				SlimefunPlugin.getLocal().sendMessage((Player) e.getWhoClicked(), "anvil.not-working", true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPreBrew(InventoryClickEvent e) {
		Inventory inventory = e.getInventory();

		if (inventory instanceof BrewerInventory && inventory.getHolder() instanceof BrewingStand && e.getRawSlot() < inventory.getSize()) {
			e.setCancelled(SlimefunItem.getByItem(e.getCursor()) != null);
		}
	}
	
	private boolean checkForUnallowedItems(ItemStack item1, ItemStack item2) {
		if (SlimefunManager.isItemSimilar(item1, SlimefunGuide.getItem(SlimefunGuideLayout.BOOK), true))
			return true;
		else if (SlimefunManager.isItemSimilar(item1, SlimefunGuide.getItem(SlimefunGuideLayout.CHEST), true))
			return true;
		else if (SlimefunManager.isItemSimilar(item2, SlimefunGuide.getItem(SlimefunGuideLayout.BOOK), true))
			return true;
		else if (SlimefunManager.isItemSimilar(item2, SlimefunGuide.getItem(SlimefunGuideLayout.CHEST), true))
			return true;
		else {
			SlimefunItem sfItem1 = SlimefunItem.getByItem(item1);
			SlimefunItem sfItem2 = SlimefunItem.getByItem(item2);
			
			if ((sfItem1 != null && !sfItem1.isDisabled()) || (sfItem2 != null && !sfItem2.isDisabled())) {
				return true;
			}
		}
		
		return false;
	}
}
