package me.mrCookieSlime.Slimefun.Objects.handlers;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * An {@link ItemHandler} represents a certain action that a {@link SlimefunItem}
 * can perform.
 * 
 * @author TheBusyBiscuit
 *
 * @see ItemUseHandler
 * @see ItemConsumptionHandler
 * @see BlockUseHandler
 * @see EntityKillHandler
 */
@FunctionalInterface
public interface ItemHandler {

    /**
     * This method is used to determine whether this {@link ItemHandler} can be
     * safely associated with one particular {@link SlimefunItem}.
     * 
     * Should this {@link ItemHandler} not be private, then it will never be
     * permanently linked to a {@link SlimefunItem}.
     * 
     * @return Whether this {@link ItemHandler} is considered private.
     */
    default boolean isPrivate() {
        return true;
    }

    /**
     * This method returns the identifier for this {@link ItemHandler}.
     * We use a {@link Class} identifier to group Item Handlers together.
     * 
     * @return The {@link Class} identifier for this {@link ItemHandler}
     */
    Class<? extends ItemHandler> getIdentifier();
}
