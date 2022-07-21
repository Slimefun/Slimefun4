package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * The {@link MiddleClickListener} is responsible for listening to
 * the {@link InventoryCreativeEvent}.
 *
 * @author svr333
 *
 */
public class MiddleClickListener implements Listener {

    public MiddleClickListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /*
     * General Discloser: this event has really really really weird behavior on middle click.
     * Has been tested thoroughly to make sure it doesnt break anything else.
     */
    @EventHandler
    public void onInventoryCreativeEvent(InventoryCreativeEvent e) {
        /*
         * When clicking outside of an inventory with middle click,
         * ClickType is not MIDDLE but CREATIVE (because this ClickType covers
         * multiple cases, we have to filter out more later on)
         */
        if (e.getClick() == ClickType.CREATIVE && e.getSlotType() == SlotType.QUICKBAR) {
            HumanEntity player = e.getWhoClicked();
            // get the block the player is looking at for later
            Block b = player.getTargetBlockExact(5);

            // Fixes: #3483
            if (b == null || !isActualMiddleClick(e, b)) {
                return;
            }

            // find the actual slimefun item the user is looking at
            SlimefunItem sfItem = BlockStorage.check(b);

            // vanilla block -> ignore
            if (sfItem == null) {
                return;
            }

            /*
             * Before giving the item to the user, check if you can swap
             * to the item instead (user already has item in hotbar).
             * This is sometimes bypassed by the client itself (not fixable though).
             */
            for (int i = 0; i < 9; i++) {
                if (sfItem.isItem(player.getInventory().getItem(i))) {
                    player.getInventory().setHeldItemSlot(i);
                    // Has to be cancelled in order for it to work properly.
                    e.setCancelled(true);
                    return;
                }
            }

            // Give the item, doing it like this will not alter any other cases.
            e.setCursor(sfItem.getItem().clone());
        }
    }

    @ParametersAreNonnullByDefault
    private boolean isActualMiddleClick(InventoryCreativeEvent e, Block b) {
        /*
         * On a middle click outside the user inventory, cursor will be set
         * to the actual block that is middle clicked, while currentItem will be AIR.
         *
         * This check is really weird due to the weird nature of this event's behaviour.
         * It checks if the block the player is looking at is of the same type as the cursor,
         * after this we can make sure that it is a middle click outside of the inventory
         * currentItem should also be air, otherwise it is not outside of the inventory
         */
        boolean isOutsideInventoryClick = e.getCursor().getType() == b.getType() && e.getCurrentItem().getType() == Material.AIR;

        /*
         * This is an edge case where the player is looking at a WALL_HEAD (eg. cargo)
         * and then the boolean above wont match because WALL_HEAD != PLAYER_HEAD.
         * This check makes up for that lack.
         */
        boolean isPlayerWallhead = b.getType() == Material.PLAYER_WALL_HEAD && e.getCursor().getType() == Material.PLAYER_HEAD;

        return isOutsideInventoryClick || isPlayerWallhead;
    }
}
