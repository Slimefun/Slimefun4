package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The {@link RadiationDamageEvent} is called when a player takes radiation damage.
 * 
 * @author HoosierTransfer
 */
public class RadiationDamageEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final int exposure;
    private boolean cancelled;

    /**
     * This constructs a new {@link RadiationDamageEvent}.
     * 
     * @param player The {@link Player} who took radiation damage
     * @param exposure The amount of radiation exposure
     */
    public RadiationDamageEvent(@Nonnull Player player, int exposure) {
        this.player = player;
        this.exposure = exposure;
    }

    /**
     * This returns the {@link Player} who took radiation damage.
     * 
     * @return The {@link Player} who took radiation damage
     */
    public @Nonnull Player getPlayer() {
        return player;
    }

    /**
     * This returns the amount of radiation exposure.
     * 
     * @return The amount of radiation exposure
     */
    public int getExposure() {
        return exposure;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static @Nonnull HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @Nonnull HandlerList getHandlers() {
        return getHandlerList();
    }
}
