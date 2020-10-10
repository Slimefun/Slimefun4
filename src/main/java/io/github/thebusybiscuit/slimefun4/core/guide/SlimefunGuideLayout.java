package io.github.thebusybiscuit.slimefun4.core.guide;

import io.github.thebusybiscuit.cscorelib2.inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

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
public enum SlimefunGuideLayout {

    /**
     * This design is a book representation of the {@link SlimefunGuide}
     */
    BOOK,

    /**
     * This design is the standard layout, it uses a {@link ChestMenu}
     */
    CHEST,

    /**
     * This is an admin-only design which creates a {@link SlimefunGuide} that allows
     * you to spawn in any {@link SlimefunItem}
     */
    CHEAT_SHEET;

    public static final SlimefunGuideLayout[] valuesCache = values();

}
