package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.Reactor;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This {@link Event} is fired whenever a {@link Reactor} has completed its process.
 *
 * @author poma123
 *
 */
public class AsyncReactorProcessCompleteEvent extends AsyncMachineProcessCompleteEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Reactor reactor;
    private final MachineFuel machineFuel;

    @ParametersAreNonnullByDefault
    public AsyncReactorProcessCompleteEvent(Location l, Reactor reactor, MachineFuel machineFuel) {
        super(l, null, null);

        this.reactor = reactor;
        this.machineFuel = machineFuel;
    }

    /**
     * The {@link SlimefunItem} instance of the reactor.
     *
     * @return The {@link SlimefunItem} instance of the reactor
     */
    @Nonnull
    public Reactor getReactor() {
        return reactor;
    }

    /**
     * This returns the used {@link MachineFuel} in the process.
     *
     * @return The {@link MachineFuel} of the process
     */
    @Nonnull
    public MachineFuel getMachineFuel() {
        return machineFuel;
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
