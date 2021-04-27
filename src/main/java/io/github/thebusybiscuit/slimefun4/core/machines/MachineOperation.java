package io.github.thebusybiscuit.slimefun4.core.machines;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineProcessHolder;

/**
 * This represents a {@link MachineOperation} which is handled
 * by a {@link MachineProcessor}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see MachineProcessor
 * @see MachineProcessHolder
 *
 */
public interface MachineOperation {

    /**
     * This method adds the given amount of ticks to the progress.
     * 
     * @param ticks
     *            The amount of ticks to add to the progress
     */
    void addProgress(int ticks);

    /**
     * This returns the amount of progress that has been made.
     * It's basically the amount of elapsed ticks since the {@link MachineOperation}
     * has started.
     * 
     * @return The amount of elapsed ticks
     */
    int getProgress();

    /**
     * This returns the amount of total ticks this {@link MachineOperation} takes to complete.
     * 
     * @return The amount of total ticks required.
     */
    int getTotalTicks();

    /**
     * This returns the amount of remaining ticks until the {@link MachineOperation}
     * finishes.
     * 
     * @return The amount of remaining ticks.
     */
    default int getRemainingTicks() {
        return getTotalTicks() - getProgress();
    }

    /**
     * This returns whether this {@link MachineOperation} has finished.
     * 
     * @return Whether this has finished or not.
     */
    default boolean isFinished() {
        return getRemainingTicks() <= 0;
    }

}
