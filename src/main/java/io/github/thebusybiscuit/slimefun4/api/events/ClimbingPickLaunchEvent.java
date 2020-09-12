package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ClimbingPick;

/**
 * An {@link Event} that is called whenever a {@link Player} has
 * used a {@link ClimbingPick} on a climbable surface.
 *
 * @author Linox
 *
 * @see ClimbingPick
 *
 */
public class ClimbingPickLaunchEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Vector velocity;
    private final ClimbingPick pick;
    private final ItemStack itemStack;
    private final Block block;

    private boolean cancelled;

    @ParametersAreNonnullByDefault
    public ClimbingPickLaunchEvent(Player player, Vector velocity, ClimbingPick pick, ItemStack itemStack, Block block) {
        super(player);

        this.velocity = velocity;
        this.pick = pick;
        this.itemStack = itemStack;
        this.block = block;
    }

    /**
     * This returns the velocity {@link Vector} that was applied to the {@link Player}
     * who used the {@link ClimbingPick}.
     *
     * @return The {@link Vector} of the applied velocity
     */
    @Nonnull
    public Vector getVelocity() {
        return velocity;
    }

    /**
     * Use this to change the velocity {@link Vector} applied to the {@link Player}.
     *
     * @param velocity
     *            The {@link Vector} velocity to apply
     */
    public void setVelocity(@Nonnull Vector velocity) {
        Validate.notNull(velocity);
        this.velocity = velocity;
    }

    /**
     * This returns the {@link ClimbingPick} that was used.
     *
     * @return The {@link ClimbingPick} that was used
     */
    @Nonnull
    public ClimbingPick getPick() {
        return pick;
    }

    /**
     * This returns the {@link ItemStack} that was used.
     *
     * @return The {@link ItemStack} that was used
     */
    @Nonnull
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * This returns the {@link Block} that was climbed.
     *
     * @return The {@link Block} that was climbed
     */
    @Nonnull
    public Block getBlock() {
        return block;
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
