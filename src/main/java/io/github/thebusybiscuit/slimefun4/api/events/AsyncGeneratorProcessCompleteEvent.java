package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This {@link Event} is fired whenever an {@link AGenerator} has completed its process.
 *
 * @author poma123
 *
 */
public class AsyncGeneratorProcessCompleteEvent extends AsyncMachineProcessCompleteEvent {

    private static final HandlerList handlers = new HandlerList();

    private final AGenerator generator;
    private final MachineFuel machineFuel;

    @ParametersAreNonnullByDefault
    public AsyncGeneratorProcessCompleteEvent(Location l, AGenerator generator, MachineFuel machineFuel) {
        super(l, null, null);

        this.generator = generator;
        this.machineFuel = machineFuel;
    }

    /**
     * The {@link SlimefunItem} instance of the generator.
     *
     * @return The {@link SlimefunItem} instance of the generator
     */
    @Nonnull
    public AGenerator getGenerator() {
        return generator;
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
