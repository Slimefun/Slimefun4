package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.Cooler;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.Juice;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.SlimefunBackpack;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This {@link Listener} is responsible for all events centered around a {@link SlimefunBackpack}.
 * This also includes the {@link Cooler}
 * 
 * @author TheBusyBiscuit
 * @author Walshy
 * @author NihilistBrew
 * @author AtomicScience
 * @author VoidAngel
 * @author John000708
 * 
 * @see SlimefunBackpack
 * @see PlayerBackpack
 *
 */
public class BackpackListener implements Listener {

    private Map<UUID, ItemStack> backpacks = new HashMap<>();

    public void register(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = ((Player) e.getPlayer());
        ItemStack backpack = backpacks.remove(p.getUniqueId());

        if (backpack != null) {
            p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1F, 1F);
            PlayerProfile.getBackpack(backpack, PlayerBackpack::markDirty);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (backpacks.containsKey(e.getPlayer().getUniqueId())) {
            ItemStack item = e.getItemDrop().getItemStack();
            SlimefunItem sfItem = SlimefunItem.getByItem(item);

            if (sfItem instanceof SlimefunBackpack) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        ItemStack item = backpacks.get(e.getWhoClicked().getUniqueId());

        if (item != null) {
            SlimefunItem backpack = SlimefunItem.getByItem(item);

            if (e.getClick() == ClickType.NUMBER_KEY) {
                if (e.getClickedInventory().getType() != InventoryType.PLAYER) {
                    ItemStack hotbarItem = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());

                    if (!isItemAllowed(hotbarItem, backpack)) {
                        e.setCancelled(true);
                    }
                }
            }
            else if (!isItemAllowed(e.getCurrentItem(), backpack)) {
                e.setCancelled(true);
            }
        }
    }

    private boolean isItemAllowed(ItemStack item, SlimefunItem backpack) {
        if (item == null || item.getType() == Material.AIR) {
            return true;
        }

        if (item.getType() == Material.SHULKER_BOX || item.getType().toString().endsWith("_SHULKER_BOX")) {
            return false;
        }

        SlimefunItem slimefunItem = SlimefunItem.getByItem(item);

        if (slimefunItem instanceof SlimefunBackpack) {
            return false;
        }

        if (backpack instanceof Cooler) {
            return slimefunItem instanceof Juice;
        }

        return true;
    }

    public void openBackpack(Player p, ItemStack item, SlimefunBackpack backpack) {
        if (item.getAmount() == 1) {
            if (Slimefun.hasUnlocked(p, backpack, true) && !PlayerProfile.get(p, profile -> openBackpack(p, item, profile, backpack.getSize()))) {
                SlimefunPlugin.getLocal().sendMessage(p, "messages.opening-backpack");
            }
        }
        else {
            SlimefunPlugin.getLocal().sendMessage(p, "backpack.no-stack", true);
        }
    }

    private void openBackpack(Player p, ItemStack item, PlayerProfile profile, int size) {
        List<String> lore = item.getItemMeta().getLore();
        for (int line = 0; line < lore.size(); line++) {
            if (lore.get(line).equals(ChatColors.color("&7ID: <ID>"))) {
                setBackpackId(p, item, line, profile.createBackpack(size).getID());
                break;
            }
        }

        if (!backpacks.containsValue(item)) {
            p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1F, 1F);
            backpacks.put(p.getUniqueId(), item);

            PlayerProfile.getBackpack(item, backpack -> {
                if (backpack != null) {
                    backpack.open(p);
                }
            });
        }
        else {
            SlimefunPlugin.getLocal().sendMessage(p, "backpack.already-open", true);
        }
    }

    public static void setBackpackId(Player p, ItemStack item, int line, int id) {
        ItemMeta im = item.getItemMeta();
        List<String> lore = im.getLore();
        lore.set(line, lore.get(line).replace("<ID>", p.getUniqueId() + "#" + id));
        im.setLore(lore);
        item.setItemMeta(im);
    }
}
