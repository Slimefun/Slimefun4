package io.github.thebusybiscuit.slimefun4.core.attributes;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

/**
 * This {@link Enum} holds all different variants of a {@link TickingBlock} item.
 * 
 * @author TheBusyBiscuit
 *
 */
public enum TickingMethod {

    /**
     * {@link TickingBlock} that ticks using this method will run on the
     * main {@link Thread}.
     * Use this {@link TickingMethod} if you need to interact with the
     * {@link World}, {@link Entity Entities} or a {@link Block}.
     */
    MAIN_THREAD,

    /**
     * A {@link TickingBlock} using this method will run on a seperate
     * {@link Thread}, this will be better performance-wise but make sure to deal
     * with any concurrency-related issues!
     */
    SEPERATE_THREAD;

}
