package io.github.thebusybiscuit.slimefun4.api.events;

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

    public PlayerLanguageChangeEvent(Player p, Language from, Language to) {
        player = p;
        this.from = from;
        this.to = to;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Language getPreviousLanguage() {
        return from;
    }

    public Language getNewLanguage() {
        return to;
    }

}
