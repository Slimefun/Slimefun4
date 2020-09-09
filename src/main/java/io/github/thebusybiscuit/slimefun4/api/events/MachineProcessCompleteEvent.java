package io.github.thebusybiscuit.slimefun4.api.events;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This {@link Event} is fired whenever an {@link AContainer} has completed its process.
 *
 * @author poma123
 *
 */
public class MachineProcessCompleteEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final Block block;
    private final MachineRecipe machineRecipe;

    @ParametersAreNonnullByDefault
    public MachineProcessCompleteEvent(Block block, MachineRecipe machineRecipe) {
        this.block = block;
        this.machineRecipe = machineRecipe;
    }

    /** This method returns the {@link Block} of the machine.
     *
     * @return the {@link Block} of the machine
     */
    public Block getMachine() {
        return block;
    }

    /** This returns the used {@link MachineRecipe} in the process.
     *
     * @return the {@link MachineRecipe} of the process.
     */
    public MachineRecipe getMachineRecipe() {
        return machineRecipe;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}