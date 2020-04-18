package io.github.thebusybiscuit.slimefun4.api.events;

import io.github.thebusybiscuit.slimefun4.core.MultiBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MultiBlockInteractEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private MultiBlock multiBlock;
    private Block clickedBlock;
    private boolean cancelled;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public MultiBlockInteractEvent(Player p, MultiBlock mb, Block clicked) {
        this.player = p;
        this.multiBlock = mb;
        this.clickedBlock = clicked;
    }

    public Player getPlayer() {
        return player;
    }

    public MultiBlock getMultiBlock() {
        return multiBlock;
    }

    public Block getClickedBlock() {
        return clickedBlock;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}
