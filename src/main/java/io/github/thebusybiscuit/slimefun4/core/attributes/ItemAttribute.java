package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

/**
 * An empty interface that only serves the purpose of bundling together all
 * interfaces of that kind.
 * 
 * An {@link ItemAttribute} must be attached to a {@link SlimefunItem}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunItem
 * @see ItemHandler
 *
 */
public interface ItemAttribute {

    /**
     * Returns the identifier of the associated {@link SlimefunItem}.
     *
     * @return the identifier of the {@link SlimefunItem}
     */
    @Nonnull
    String getId();

}
