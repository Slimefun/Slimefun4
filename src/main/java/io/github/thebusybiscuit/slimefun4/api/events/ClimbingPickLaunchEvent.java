package io.github.thebusybiscuit.slimefun4.api.events;

import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ClimbingPick;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * An {@link Event} that is called whenever a {@link Player} has
 * used a {@link ClimbingPick} on a climbable surface.
 *
 * @author Linox
 *
 * @see ClimbingPick
 *
 */
public class ClimbingPickLaunchEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Vector velocity;
    private final ItemStack pick;
    private boolean cancelled;

    public ClimbingPickLaunchEvent(Player player, Vector velocity, ItemStack pick) {
        super(false);

        this.player = player;
        this.velocity = velocity;
        this.pick = pick;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * This returns the {@link Player} that used the {@link ClimbingPick}.
     *
     * @return The {@link Player} that used
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * This returns the {@link Vector} velocity that was applied to the {@link Player}
     * who used the {@link ClimbingPick}.
     *
     * @return The {@link Vector} of the applied velocity
     */
    public Vector getVelocity() {
        return velocity;
    }

    /**
     * This returns the {@link ClimbingPick} {@link ItemStack} that was used.
     *
     * @return The {@link ItemStack} that was used
     */
    public ItemStack getItem() {
        return this.pick;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
