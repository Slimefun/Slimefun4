package io.github.thebusybiscuit.slimefun4.api.events;

import io.github.thebusybiscuit.slimefun4.implementation.items.androids.AndroidInstance;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class AndroidWoodcutEvent extends AndroidBlockBreakEvent {

    private static final HandlerList handlers = new HandlerList();

    /**
     * @param block
     *            The {@link Block} to be chopped
     * @param android
     *            The {@link AndroidInstance} that triggered this {@link Event}
     */
    @ParametersAreNonnullByDefault
    public AndroidWoodcutEvent(Block block, AndroidInstance android) {
        super(block, android);
    }

    @Override
    public @Nonnull HandlerList getHandlers() {
        return getHandlerList();
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
