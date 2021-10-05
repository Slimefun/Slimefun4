package io.github.thebusybiscuit.slimefun4.core.guide;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

/**
 * This enum holds the different designs a {@link SlimefunGuide} can have.
 * Each constant corresponds to a {@link SlimefunGuideImplementation}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunGuide
 * @see SlimefunGuideImplementation
 *
 */
public enum SlimefunGuideMode {

    /**
     * This design is the standard layout used in survival mode.
     */
    SURVIVAL_MODE,

    /**
     * This is an admin-only design which creates a {@link SlimefunGuide} that allows
     * you to spawn in any {@link SlimefunItem}
     */
    CHEAT_MODE;

}
