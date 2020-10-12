package io.github.thebusybiscuit.slimefun4.api.events;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.implementation.guide.BookSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.implementation.guide.ChestSlimefunGuide;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This {@link Event} is called whenever a {@link Player} click to unlock a research.
 * This is called called before {@link Research#canUnlock(Player)}
 * The event is not called for the cheat sheet
 *
 * @author uiytt
 *
 * @see ChestSlimefunGuide
 * @see BookSlimefunGuide
 *
 */
public class PreCanUnlockResearchEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Research research;
    private final SlimefunItem slimefunItem;

    private boolean cancelled;


    @ParametersAreNonnullByDefault
    public PreCanUnlockResearchEvent(Player p, Research research, SlimefunItem slimefunItem) {
        Validate.notNull(p, "The Player cannot be null");
        Validate.notNull(research, "Research cannot be null");
        Validate.notNull(slimefunItem, "SlimefunItem cannot be null");

        this.player = p;
        this.research = research;
        this.slimefunItem = slimefunItem;
    }

    @Nonnull
    public Player getPlayer() {
        return player;
    }

    @Nonnull
    public Research getResearch() {
        return research;
    }

    @Nonnull
    public SlimefunItem getSlimefunItem() {
        return slimefunItem;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
