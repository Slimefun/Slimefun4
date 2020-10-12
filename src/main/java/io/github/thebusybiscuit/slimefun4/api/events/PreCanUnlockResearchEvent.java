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

public class PreCanUnlockResearchEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private final Player player;
    private final Research research;
    private final PlayerProfile profile;
    private final SlimefunItem slimefunItem;
    private final Category category;

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
    public PreCanUnlockResearchEvent(@Nonnull Player p, @Nonnull Research research, @Nonnull PlayerProfile profile, @Nonnull SlimefunItem slimefunItem, @Nonnull Category category) {
        Validate.notNull(p, "The Player cannot be null");
        Validate.notNull(research, "Research cannot be null");
        Validate.notNull(profile, "PlayerProfile cannot be null");
        Validate.notNull(slimefunItem, "SlimefunItem cannot be null");
        Validate.notNull(category, "Category cannot be null");

        this.player = p;
        this.research = research;
        this.profile = profile;
        this.slimefunItem = slimefunItem;
        this.category = category;
    }

    @Nonnull
    public Player getPlayer() {
        return this.player;
    }
    @Nonnull
    public Research getResearch() {
        return this.research;
    }
    @Nonnull
    public PlayerProfile getProfile() {
        return this.profile;
    }
    @Nonnull
    public SlimefunItem getSlimefunItem() {
        return this.slimefunItem;
    }
    @Nonnull
    public Category getCategory() {
        return this.category;
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
