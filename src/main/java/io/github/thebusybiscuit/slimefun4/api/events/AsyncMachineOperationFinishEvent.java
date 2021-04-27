package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.github.thebusybiscuit.slimefun4.core.machines.MachineOperation;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;

/**
 * This {@link Event} is fired whenever an {@link MachineProcessor} has completed a {@link MachineOperation}.
 *
 * @author poma123
 * @author TheBusyBiscuit
 *
 */
public class AsyncMachineOperationFinishEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Location location;
    private final MachineProcessor<?> machineProcessor;
    private final MachineOperation machineOperation;

    public <T extends MachineOperation> AsyncMachineOperationFinishEvent(Location l, MachineProcessor<T> processor, T operation) {
        super(!Bukkit.isPrimaryThread());

        this.location = l;
        this.machineProcessor = processor;
        this.machineOperation = operation;
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
     * The {@link MachineProcessor} instance of the machine.
     *
     * @return The {@link MachineProcessor} instance of the machine
     */
    @Nullable
    public MachineProcessor<?> getProcessor() {
        return machineProcessor;
    }

    /**
     * This returns the used {@link MachineOperation} in the process.
     *
     * @return The {@link MachineOperation} of the process
     */
    @Nullable
    public MachineOperation getOperation() {
        return machineOperation;
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
