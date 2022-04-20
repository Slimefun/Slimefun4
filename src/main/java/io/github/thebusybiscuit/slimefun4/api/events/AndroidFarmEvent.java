package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.items.androids.AndroidInstance;
import io.github.thebusybiscuit.slimefun4.implementation.items.androids.FarmerAndroid;

/**
 * This {@link Event} is fired before a {@link FarmerAndroid} harvests a {@link Block}.
 * If this {@link Event} is cancelled, the {@link Block} will not be harvested.
 * <p>
 * The {@link Event} will still be fired for non-harvestable blocks.
 * 
 * @author TheBusyBiscuit
 * 
 */
public class AndroidFarmEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block block;
    private final AndroidInstance android;
    private final boolean isAdvanced;
    private ItemStack drop;
    private boolean cancelled;

    /**
     * @param block
     *            The harvested {@link Block}
     * @param android
     *            The {@link AndroidInstance} that triggered this {@link Event}
     * @param isAdvanced
     *            Whether this is an advanced farming action
     * @param drop
     *            The item to be dropped or null
     */
    public AndroidFarmEvent(@Nonnull Block block, @Nonnull AndroidInstance android, boolean isAdvanced, @Nullable ItemStack drop) {
        this.block = block;
        this.android = android;
        this.isAdvanced = isAdvanced;
        this.drop = drop;
    }

    /**
     * This method returns the mined {@link Block}
     *
     * @return the mined {@link Block}
     */
    @Nonnull
    public Block getBlock() {
        return block;
    }

    /**
     * This returns the harvested item or null.
     * 
     * @return The harvested item or null
     */
    @Nullable
    public ItemStack getDrop() {
        return drop;
    }

    /**
     * Whether this was invoked via an advanced farming action
     * 
     * @return Whether it is advanced
     */
    public boolean isAdvanced() {
        return isAdvanced;
    }

    /**
     * This will set the {@link ItemStack} result.
     * 
     * @param drop
     *            The result or null
     */
    public void setDrop(@Nullable ItemStack drop) {
        this.drop = drop;
    }

    /**
     * This method returns the {@link AndroidInstance} who
     * triggered this {@link Event}
     *
     * @return the involved {@link AndroidInstance}
     */
    @Nonnull
    public AndroidInstance getAndroid() {
        return android;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
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