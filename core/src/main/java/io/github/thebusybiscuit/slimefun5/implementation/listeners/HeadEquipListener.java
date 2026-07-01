package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Prevents wearing {@link SlimefunItem SlimefunItems} that use a player-head texture (custom blocks)
 * as a helmet. Vanilla lets a player right-click, shift-click or hotbar-swap any player head onto
 * their head; Slimefun head items are decorative/functional blocks, not armor, so we cancel any
 * attempt to equip one. Real Slimefun armor never uses a player-head material, so this is safe.
 */
public class HeadEquipListener implements Listener {

    public HeadEquipListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private boolean isSlimefunHead(@Nullable ItemStack item) {
        if (item == null) {
            return false;
        }

        String type = item.getType().name();

        // PLAYER_HEAD (modern) / SKULL_ITEM (1.8-1.12) are the wearable player-head materials.
        if (!type.equals("PLAYER_HEAD") && !type.equals("SKULL_ITEM")) {
            return false;
        }

        return SlimefunItem.getByItem(item) != null;
    }

    /** Vanilla equips a player head to the helmet slot on right-click in the air. */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onRightClickEquip(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR && isSlimefunHead(e.getItem())) {
            e.setCancelled(true);
        }
    }

    /** Blocks placing, hotbar-swapping or shift-equipping a Slimefun head into the helmet slot. */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryEquip(InventoryClickEvent e) {
        // Click a head onto an armor slot.
        if (e.getSlotType() == InventoryType.SlotType.ARMOR && isSlimefunHead(e.getCursor())) {
            e.setCancelled(true);
            return;
        }

        // Number-key (hotbar) swap into an armor slot.
        if (e.getClick() == ClickType.NUMBER_KEY && e.getSlotType() == InventoryType.SlotType.ARMOR
                && e.getWhoClicked().getInventory() instanceof PlayerInventory) {
            ItemStack hotbar = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());

            if (isSlimefunHead(hotbar)) {
                e.setCancelled(true);
                return;
            }
        }

        // Shift-clicking a head in the player's own inventory auto-equips it to an empty helmet slot.
        // Only act when no other container is open (top inventory is the crafting grid), so shift-
        // moving a head into a chest is unaffected.
        if (e.getClick().isShiftClick() && isSlimefunHead(e.getCurrentItem())
                && e.getView().getType() == InventoryType.CRAFTING
                && e.getWhoClicked().getInventory() instanceof PlayerInventory) {
            ItemStack helmet = ((PlayerInventory) e.getWhoClicked().getInventory()).getHelmet();

            if (helmet == null || helmet.getType() == Material.AIR) {
                e.setCancelled(true);
            }
        }
    }
}
