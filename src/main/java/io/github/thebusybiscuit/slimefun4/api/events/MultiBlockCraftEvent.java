package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;

/**
 * This {@link Event} is called when a {@link Player} crafts an item using a {@link MultiBlockMachine}.
 * Unlike the {@link MultiBlockInteractEvent}, this event only fires if an output to a craft is expected.
 * If this event is cancelled, ingredients will not be consumed and no output item results.
 *
 * @author char321
 *
 */
public class MultiBlockCraftEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final MultiBlockMachine machine;
    private ItemStack output;
    private boolean cancelled;

    /**
     * Creates a new {@link MultiBlockCraftEvent}.
     *
     * @param p The player that crafts using a multiblock
     * @param machine The multiblock machine used to craft
     * @param output The resulting item of the craft
     */
    @ParametersAreNonnullByDefault
    public MultiBlockCraftEvent(Player p, MultiBlockMachine machine, ItemStack output) {
        super(p);
        this.machine = machine;
        this.output = output;
    }

    /**
     * Gets the machine that was used to craft.
     *
     * @return The {@link MultiBlockMachine} used to craft
     */
    @Nonnull
    public MultiBlockMachine getMachine() {
        return machine;
    }

    /**
     * Gets the expected output of the craft.
     *
     * @return The {@link ItemStack} output that results from the craft.
     */
    @Nonnull
    public ItemStack getOutput() {
        return output;
    }

    /**
     * Sets the output of the craft. Keep in mind that this overwrites any existing output.
     *
     * @param output
     *            The new item for the event to produce
     *
     * @return The previous {@link ItemStack} output that was replaced
     */
    public ItemStack setOutput(@Nonnull ItemStack output) {
        ItemStack temp = this.output;
        this.output = output;
        return temp;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}
