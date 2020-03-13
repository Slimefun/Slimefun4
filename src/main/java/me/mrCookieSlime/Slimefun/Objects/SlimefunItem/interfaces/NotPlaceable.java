package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Implement this interface for any {@link SlimefunItem} to prevent
 * that {@link SlimefunItem} from being placed.
 * <p>
 * Important: This will not cancel any {@link BlockPlaceEvent}.
 * It will simply prevent Slimefun from ever registering this {@link SlimefunItem}
 * as a placed {@link Block}.
 *
 * @author TheBusyBiscuit
 */
public interface NotPlaceable {

}
