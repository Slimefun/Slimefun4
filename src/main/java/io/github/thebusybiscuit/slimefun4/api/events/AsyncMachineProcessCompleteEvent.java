package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This {@link Event} is fired whenever an {@link AContainer} has completed its process.
 *
 * @author poma123
 *
 */
public class AsyncMachineProcessCompleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Location location;
    private final AContainer container;
    private final MachineRecipe machineRecipe;

    public AsyncMachineProcessCompleteEvent(@Nonnull Location l, @Nullable AContainer container, @Nullable MachineRecipe machineRecipe) {
        super(true);

        this.location = l;
        this.container = container;
        this.machineRecipe = machineRecipe;
    }

    /**
     * This returns the {@link Location} of the machine.
     *
     * @return The {@link Location} of the machine
     */
    @Nonnull
    public Location getLocation() {
        return location;
    }

    /**
     * The {@link SlimefunItem} instance of the machine.
     *
     * @return The {@link SlimefunItem} instance of the machine
     */
    @Nullable
    public AContainer getMachine() {
        return container;
    }

    /**
     * This returns the used {@link MachineRecipe} in the process.
     *
     * @return The {@link MachineRecipe} of the process
     */
    @Nullable
    public MachineRecipe getMachineRecipe() {
        return machineRecipe;
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
