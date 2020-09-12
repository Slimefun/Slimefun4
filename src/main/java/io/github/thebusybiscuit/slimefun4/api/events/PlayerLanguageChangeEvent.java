package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.github.thebusybiscuit.slimefun4.core.services.LocalizationService;
import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;

/**
 * This {@link Event} gets called when a {@link Player} has switched their {@link Language}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Language
 * @see LocalizationService
 *
 */
public class PlayerLanguageChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Language from;
    private final Language to;

    public PlayerLanguageChangeEvent(@Nonnull Player p, @Nonnull Language from, @Nonnull Language to) {
        player = p;
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the {@link Player} who triggered this {@link Event},
     * the {@link Player} who switched his {@link Language} to be precise.
     * 
     * @return The {@link Player} who switched his {@link Language}
     */
    @Nonnull
    public Player getPlayer() {
        return player;
    }

    /**
     * This returns the {@link Language} that this {@link Player} was using before.
     * 
     * @return The previous {@link Language} of our {@link Player}
     */
    @Nonnull
    public Language getPreviousLanguage() {
        return from;
    }

    /**
     * This returns the {@link Language} that this {@link Player} wants to switch to.
     * 
     * @return The new {@link Language}
     */
    @Nonnull
    public Language getNewLanguage() {
        return to;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

}
