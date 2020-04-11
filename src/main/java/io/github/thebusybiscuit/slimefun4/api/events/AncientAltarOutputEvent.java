package io.github.thebusybiscuit.slimefun4.api.events;


import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientAltar;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * This {@link Event} is fired before an item is dropped by an {@link AncientAltar}.
 * Cancelling this event will make the {@link AncientAltar} drop no item after the recipe is finished.
 *
 * @author Tweep
 *
 */

public class AncientAltarOutputEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Block block;
    private ItemStack output;
    private boolean cancelled;

    /**
     * @param block
     *            The mined {@link Block}
     * @param output
     *            The {@link ItemStack} that would be dropped by the ritual
     *
     */

    public AncientAltarOutputEvent(ItemStack output, Block block) {
        this.block = block;
        this.output = output;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() { return handlers; }
    /**
     * This method returns the main altar's block {@link Block}
     *
     * @return the main altar's block {@link Block}
     */
    public Block getBlock() { return block; }

    /**
     * This method returns the item that would be dropped by the altar. {@link ItemStack}
     *
     * @return the item that would be dropped by the altar. {@link ItemStack}
     */
    public ItemStack getItem() {
        return output;
    }

    public void setItem(ItemStack output) {
        this.output = output;
    }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean b) { cancelled = b; }

}
