package io.github.thebusybiscuit.slimefun4.core.handlers;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

/**
 * A {@link GlobalItemHandler} is a special type of {@link ItemHandler}
 * which is not associated with one particular {@link SlimefunItem} but rather
 * exists seperate from them, similar to an {@link Event} {@link Listener}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see ItemDropHandler
 *
 */
public interface GlobalItemHandler extends ItemHandler {

}
