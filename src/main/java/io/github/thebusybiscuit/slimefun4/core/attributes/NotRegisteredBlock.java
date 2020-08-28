package io.github.thebusybiscuit.slimefun4.core.attributes;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlacable;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * Implement this interface for any {@link SlimefunItem} to prevent
 * that {@link SlimefunItem} from being registered to {@link BlockStorage}.
 * 
 * <b>Important</b>: This is similar to {@link NotPlacable} but this one allows for {@link ItemHandler}s to work.
 * 
 * @author Linox
 *
 */
public interface NotRegisteredBlock {

}
