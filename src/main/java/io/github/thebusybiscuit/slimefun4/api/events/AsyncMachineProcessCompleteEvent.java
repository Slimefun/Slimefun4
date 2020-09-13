package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;

/**
 * This {@link Event} is fired whenever an {@link AContainer} has completed its process.
 *
 * @author poma123
 *
 */
public class AsyncMachineProcessCompleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Block block;
    private final MachineRecipe machineRecipe;

    @ParametersAreNonnullByDefault
    public AsyncMachineProcessCompleteEvent(Block block, MachineRecipe machineRecipe) {
        super(true);

        this.block = block;
        this.machineRecipe = machineRecipe;
    }

    /**
     * This method returns the {@link Block} of the machine.
     *
     * @return the {@link Block} of the machine
     */
    @Nonnull
    public Block getMachine() {
        return block;
    }

    /**
     * This returns the used {@link MachineRecipe} in the process.
     *
     * @return the {@link MachineRecipe} of the process.
     */
    @Nonnull
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
