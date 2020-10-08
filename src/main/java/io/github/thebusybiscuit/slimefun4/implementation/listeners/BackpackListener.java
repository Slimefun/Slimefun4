package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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

import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.Cooler;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SlimefunBackpack;
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

    private final Map<UUID, ItemStack> backpacks = new HashMap<>();

    public void register(@Nonnull SlimefunPlugin plugin) {
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

            if (backpack instanceof SlimefunBackpack) {
                if (e.getClick() == ClickType.NUMBER_KEY) {
                    if (e.getClickedInventory().getType() != InventoryType.PLAYER) {
                        ItemStack hotbarItem = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());

                        if (!isAllowed((SlimefunBackpack) backpack, hotbarItem)) {
                            e.setCancelled(true);
                        }
                    }
                } else if (!isAllowed((SlimefunBackpack) backpack, e.getCurrentItem())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    private boolean isAllowed(@Nonnull SlimefunBackpack backpack, @Nullable ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return true;
        }

        return backpack.isItemAllowed(item, SlimefunItem.getByItem(item));
    }

    public void openBackpack(Player p, ItemStack item, SlimefunBackpack backpack) {
        if (item.getAmount() == 1) {
            if (Slimefun.hasUnlocked(p, backpack, true) && !PlayerProfile.get(p, profile -> openBackpack(p, item, profile, backpack.getSize()))) {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.opening-backpack");
            }
        } else {
            SlimefunPlugin.getLocalization().sendMessage(p, "backpack.no-stack", true);
        }
    }

    private void openBackpack(Player p, ItemStack item, PlayerProfile profile, int size) {
        List<String> lore = item.getItemMeta().getLore();
        for (int line = 0; line < lore.size(); line++) {
            if (lore.get(line).equals(ChatColor.GRAY + "ID: <ID>")) {
                setBackpackId(p, item, line, profile.createBackpack(size).getId());
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
        } else {
            SlimefunPlugin.getLocalization().sendMessage(p, "backpack.already-open", true);
        }
    }

    /**
     * This method sets the id for a backpack onto the given {@link ItemStack}.
     * 
     * @param backpackOwner
     *            The owner of this backpack
     * @param item
     *            The {@link ItemStack} to modify
     * @param line
     *            The line at which the ID should be replaced
     * @param id
     *            The id of this backpack
     */
    public void setBackpackId(@Nonnull OfflinePlayer backpackOwner, @Nonnull ItemStack item, int line, int id) {
        Validate.notNull(backpackOwner, "Backpacks must have an owner!");
        Validate.notNull(item, "Cannot set the id onto null!");

        ItemMeta im = item.getItemMeta();

        if (!im.hasLore()) {
            throw new IllegalArgumentException("This backpack does not have any lore!");
        }

        List<String> lore = im.getLore();

        if (line >= lore.size() || !lore.get(line).contains("<ID>")) {
            throw new IllegalArgumentException("Specified a line that is out of bounds or invalid!");
        }

        lore.set(line, lore.get(line).replace("<ID>", backpackOwner.getUniqueId() + "#" + id));
        im.setLore(lore);
        item.setItemMeta(im);
    }
}
