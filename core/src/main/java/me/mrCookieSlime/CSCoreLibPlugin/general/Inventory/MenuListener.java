package me.mrCookieSlime.CSCoreLibPlugin.general.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;

/**
 * An old {@link Listener} for CS-CoreLib
 * This is an old remnant of CS-CoreLib, the last bits of the past. They will be removed once everything is
 *             updated.
 */
public class MenuListener implements Listener {

    static final Map<UUID, ChestMenu> menus = new HashMap<>();

    public MenuListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        ChestMenu menu = menus.remove(e.getPlayer().getUniqueId());

        if (menu != null) {
            menu.getMenuCloseHandler().onClose((Player) e.getPlayer());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ChestMenu menu = menus.get(e.getWhoClicked().getUniqueId());

        if (menu != null) {
            // A double-click (COLLECT_TO_CURSOR) gathers matching items from the WHOLE view, bypassing the
            // per-slot handlers below - so it can vacuum protected display/output slots (which regenerate)
            // into the cursor = a duplication. Cancel it only when it would actually pull from such a slot,
            // leaving free input slots and the player's own inventory gatherable.
            if (e.getAction() == InventoryAction.COLLECT_TO_CURSOR && collectWouldTouchProtectedSlot(e.getCursor(), e.getInventory(), menu)) {
                e.setCancelled(true);
                return;
            }

            if (e.getRawSlot() < e.getInventory().getSize()) {
                MenuClickHandler handler = menu.getMenuClickHandler(e.getSlot());

                if (handler == null) {
                    e.setCancelled(!menu.isEmptySlotsClickable() && (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR));
                } else if (handler instanceof AdvancedMenuClickHandler) {
                    e.setCancelled(!((AdvancedMenuClickHandler) handler).onClick(e, (Player) e.getWhoClicked(), e.getSlot(), e.getCursor(), new ClickAction(e.isRightClick(), e.isShiftClick())));
                } else {
                    e.setCancelled(!handler.onClick((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), new ClickAction(e.isRightClick(), e.isShiftClick())));
                }
            } else {
                e.setCancelled(!menu.getPlayerInventoryClickHandler().onClick((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), new ClickAction(e.isRightClick(), e.isShiftClick())));
            }
        }
    }

    /**
     * A drag isn't routed through the per-slot click handlers at all, so without this a player could
     * drag-place items into (or spread across) protected menu slots - e.g. bypass the output-slot
     * guard. Cancel any drag that touches a menu slot which carries a click handler; free slots (plain
     * input areas) and the player's own inventory are left draggable.
     */
    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        ChestMenu menu = menus.get(e.getWhoClicked().getUniqueId());

        if (menu != null && dragTouchesProtectedSlot(e.getRawSlots(), e.getInventory().getSize(), menu)) {
            e.setCancelled(true);
        }
    }

    /**
     * Whether a {@link InventoryAction#COLLECT_TO_CURSOR} would gather from a protected menu slot: a slot
     * carrying a click handler (display / output / button) that holds an item matching the cursor. Free
     * input slots (no handler) and the player's own inventory are not protected.
     */
    static boolean collectWouldTouchProtectedSlot(ItemStack cursor, Inventory top, ChestMenu menu) {
        if (cursor == null || cursor.getType() == Material.AIR) {
            return false;
        }

        for (int slot = 0; slot < top.getSize(); slot++) {
            if (menu.getMenuClickHandler(slot) != null) {
                ItemStack item = top.getItem(slot);

                if (item != null && item.getType() != Material.AIR && cursor.isSimilar(item)) {
                    return true;
                }
            }
        }

        return false;
    }

    /** Whether a drag touches a protected (handler-bearing) slot of the open menu's top inventory. */
    static boolean dragTouchesProtectedSlot(Iterable<Integer> rawSlots, int topSize, ChestMenu menu) {
        for (int rawSlot : rawSlots) {
            if (rawSlot < topSize && menu.getMenuClickHandler(rawSlot) != null) {
                return true;
            }
        }

        return false;
    }

}
