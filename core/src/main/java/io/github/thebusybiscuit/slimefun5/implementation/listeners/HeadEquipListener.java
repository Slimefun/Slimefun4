package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Prevents wearing {@link SlimefunItem SlimefunItems} that use a player-head texture (custom blocks)
 * as a helmet. Vanilla lets a player right-click, shift-click or hotbar-swap any player head onto
 * their head; Slimefun head items are decorative/functional blocks, not armor, so we cancel any
 * attempt to equip one. Real Slimefun armor never uses a player-head material, so this is safe.
 */
public class HeadEquipListener implements Listener {

    private final Slimefun plugin;

    public HeadEquipListener(@Nonnull Slimefun plugin) {
        this.plugin = plugin;
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

    /**
     * Cancelling an armor-slot click stops the server-side equip, but the client has already optimistically
     * moved the item into the helmet slot; without a resync it renders as equipped while the server still
     * holds it elsewhere, so the item appears to vanish (Slimefun #... - custom skulls deleted on equip).
     * Re-sending the inventory next tick restores the correct client view.
     */
    private void resync(@Nonnull HumanEntity who) {
        if (who instanceof Player) {
            Player p = (Player) who;
            plugin.getServer().getScheduler().runTask(plugin, () -> p.updateInventory());
        }
    }

    /** Vanilla equips a player head to the helmet slot on right-click in the air. */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onRightClickEquip(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR && isSlimefunHead(e.getItem())) {
            e.setCancelled(true);
        }
    }

    /** Blocks placing, hotbar-swapping or shift-equipping a Slimefun head into the helmet slot. */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryEquip(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getWhoClicked();

        // Click a head onto an armor slot (place or swap-with-cursor).
        if (e.getSlotType() == InventoryType.SlotType.ARMOR && isSlimefunHead(e.getCursor())) {
            e.setCancelled(true);
            resync(p);
            return;
        }

        // Number-key (hotbar) swap into an armor slot.
        if (e.getClick() == ClickType.NUMBER_KEY && e.getSlotType() == InventoryType.SlotType.ARMOR
                && isSlimefunHead(p.getInventory().getItem(e.getHotbarButton()))) {
            e.setCancelled(true);
            resync(p);
            return;
        }

        // Shift-clicking a head in the player's own inventory auto-equips it to an empty helmet slot.
        // Only act when no other container is open (top inventory is the crafting grid), so shift-
        // moving a head into a chest is unaffected.
        if (e.getClick().isShiftClick() && isSlimefunHead(e.getCurrentItem())
                && e.getView().getType() == InventoryType.CRAFTING) {
            ItemStack helmet = p.getInventory().getHelmet();

            if (helmet == null || helmet.getType() == Material.AIR) {
                e.setCancelled(true);
                resync(p);
                return;
            }
        }

        // Safety net: whatever gesture slips through the cancels above (version-specific actions, other
        // plugins, drag), if a Slimefun head still ends up in the helmet slot next tick, return it to the
        // inventory instead of letting it be worn — worn Slimefun heads are what get lost on the desync.
        if (isSlimefunHead(e.getCursor()) || isSlimefunHead(e.getCurrentItem())
                || (e.getClick() == ClickType.NUMBER_KEY && isSlimefunHead(p.getInventory().getItem(e.getHotbarButton())))) {
            plugin.getServer().getScheduler().runTask(plugin, () -> returnHeadFromHelmet(p));
        }
    }

    /** If the helmet slot holds a Slimefun head, move it back into the inventory (or drop it) and clear the slot. */
    private void returnHeadFromHelmet(@Nonnull Player p) {
        ItemStack helmet = p.getInventory().getHelmet();

        if (isSlimefunHead(helmet)) {
            p.getInventory().setHelmet(null);

            for (ItemStack leftover : p.getInventory().addItem(helmet).values()) {
                p.getWorld().dropItemNaturally(p.getLocation(), leftover);
            }

            p.updateInventory();
        }
    }
}
