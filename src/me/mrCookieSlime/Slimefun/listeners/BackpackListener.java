package me.mrCookieSlime.Slimefun.listeners;

import java.util.List;

import me.mrCookieSlime.Slimefun.Setup.SlimefunLocalization;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Juice;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunBackpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.BackpackInventory;

public class BackpackListener implements Listener {
	
	public BackpackListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (SlimefunPlugin.getUtilities().enchanting.containsKey(e.getPlayer().getUniqueId())) {
			SlimefunPlugin.getUtilities().enchanting.remove(e.getPlayer().getUniqueId());
		}
		
		if (SlimefunPlugin.getUtilities().backpack.containsKey(e.getPlayer().getUniqueId())) {
			((Player) e.getPlayer()).playSound(e.getPlayer().getLocation(), Sound.ENTITY_HORSE_ARMOR, 1F, 1F);
			PlayerProfile.getBackpack(SlimefunPlugin.getUtilities().backpack.get(e.getPlayer().getUniqueId())).markDirty();
			SlimefunPlugin.getUtilities().backpack.remove(e.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		if (SlimefunPlugin.getUtilities().backpack.containsKey(e.getPlayer().getUniqueId())){
			ItemStack item = e.getItemDrop().getItemStack();
			SlimefunItem sfItem = SlimefunItem.getByItem(item);
			if (sfItem instanceof SlimefunBackpack) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		ItemStack item = SlimefunPlugin.getUtilities().backpack.get(e.getWhoClicked().getUniqueId());
		if (item != null) {
			if (e.getClick() == ClickType.NUMBER_KEY) {
				ItemStack hotbarItem = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());
				SlimefunItem sfItem = SlimefunItem.getByItem(hotbarItem);
				if ((hotbarItem != null && hotbarItem.getType().toString().contains("SHULKER_BOX")) ||
						sfItem instanceof SlimefunBackpack)

							e.setCancelled(true);
			}
			else if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
				SlimefunItem sfItem = SlimefunItem.getByItem(e.getCurrentItem());
				if ((SlimefunManager.isItemSimiliar(item, SlimefunItems.COOLER, false) && !(sfItem instanceof Juice)) ||
						e.getCurrentItem().getType().toString().contains("SHULKER_BOX") ||
						sfItem instanceof SlimefunBackpack)

							e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player p = e.getPlayer();
			ItemStack item = e.getItem();
			SlimefunItem sfItem = SlimefunItem.getByItem(item);
			if (sfItem == null) return;

			// Surely this can be even simpler?
			if (sfItem.isItem(SlimefunItems.BACKPACK_SMALL))
				openBackpack(SlimefunItems.BACKPACK_SMALL, 9, e, p);
			else if (sfItem.isItem(SlimefunItems.BACKPACK_MEDIUM))
				openBackpack(SlimefunItems.BACKPACK_MEDIUM, 18, e, p);
			else if (sfItem.isItem(SlimefunItems.BACKPACK_LARGE))
				openBackpack(SlimefunItems.BACKPACK_LARGE, 27, e, p);
			else if (sfItem.isItem(SlimefunItems.WOVEN_BACKPACK))
				openBackpack(SlimefunItems.WOVEN_BACKPACK, 36, e, p);
			else if (sfItem.isItem(SlimefunItems.GILDED_BACKPACK))
				openBackpack(SlimefunItems.GILDED_BACKPACK, 45, e, p);
			else if (sfItem.isItem(SlimefunItems.RADIANT_BACKPACK))
				openBackpack(SlimefunItems.RADIANT_BACKPACK, 54, e, p);
			else if (sfItem.isItem(SlimefunItems.BOUND_BACKPACK))
				openBackpack(SlimefunItems.BOUND_BACKPACK, 36, e, p);
			else if (sfItem.isItem(SlimefunItems.COOLER))
				openBackpack(SlimefunItems.COOLER, 27, e, p);
		}
	}

	private void openBackpack(ItemStack item, int size, PlayerInteractEvent e, Player p) {
		e.setCancelled(true);

		if (item.getAmount() == 1) {
			if (Slimefun.hasUnlocked(p, item, true)) {
				if (!PlayerProfile.get(p, profile -> openBackpack(item, profile, size)))
					Slimefun.getLocal().sendMessage(p, "messages.opening-backpack");
			}
		}
		else SlimefunPlugin.getLocal().sendMessage(p, "backpack.no-stack", true);
	}

	private void openBackpack(ItemStack item, PlayerProfile profile, int size) {
		Player p = profile.getPlayer();
		for (int line = 0; line < item.getItemMeta().getLore().size(); line++) {
			if (item.getItemMeta().getLore().get(line).equals(ChatColor.translateAlternateColorCodes('&', "&7ID: <ID>"))) {
				BackpackInventory backpack = profile.createBackpack(size);

				setBackpackId(p, item, line, backpack.getID());
				break;
			}
		}

		if(!SlimefunPlugin.getUtilities().backpack.containsValue(item)) {
			p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1F, 1F);
			SlimefunPlugin.getUtilities().backpack.put(p.getUniqueId(), item);

			Slimefun.runSync(() -> PlayerProfile.getBackpack(item).open(p));
		}
		else SlimefunPlugin.getLocal().sendMessage(p, "backpack.already-open", true);
	}

    public static void setBackpackId(Player p, ItemStack item, int line, int id) {
        ItemMeta im = item.getItemMeta();
        List<String> lore = im.getLore();
        lore.set(line, lore.get(line).replace("<ID>", p.getUniqueId() + "#" + id));
        im.setLore(lore);
        item.setItemMeta(im);
    }
}
