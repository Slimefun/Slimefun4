package io.github.thebusybiscuit.slimefun4.api.events;


import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class WhitelistCreateEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Player target;
    private String name;

    private boolean cancelled;

    public WhitelistCreateEvent(@Nonnull Player player, @Nonnull Player target) {
        super(player);

        Validate.notNull(target, "Target must not be null!");
        Validate.notNull(player, "Player must not be null!");

        this.target = target;
        this.name = player.toString();
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        Validate.notEmpty(name, "The name of a player must not be empty!");
        this.name = name;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
}
