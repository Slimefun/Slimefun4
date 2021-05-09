package io.github.thebusybiscuit.slimefun4.api.events;

import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.enchanting.AutoEnchanter;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * An {@link Event} that is called whenever an {@link AutoEnchanter} is
 * enchanting an {@link ItemStack}.
 *
 * @author StarWishsama
 */
public class AutoEnchantProcessEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final ItemStack item;
    private final ItemStack enchantBook;
    private final BlockMenu menu;

    private boolean cancelled;

    public AutoEnchantProcessEvent(@Nonnull ItemStack item, @Nonnull ItemStack enchantBook, @Nonnull BlockMenu menu) {
        super(true);

        this.item = item;
        this.enchantBook = enchantBook;
        this.menu = menu;
    }

    /**
     * This returns the {@link ItemStack} that is being enchanted.
     *
     * @return The {@link ItemStack} that is being enchanted
     */
    @Nonnull
    public ItemStack getItem() {
        return item;
    }

    /**
     * This returns the {@link ItemStack} that is being used enchant book
     *
     * @return The {@link ItemStack} that is being used enchant book
     */
    @Nonnull
    public ItemStack getEnchantBook() {
        return enchantBook;
    }

    /**
     * This returns the {@link AutoEnchanter}'s ${@link BlockMenu}
     *
     * @return The ${@link BlockMenu} of {@link AutoEnchanter} that is enchanting item
     */
    @Nonnull
    public BlockMenu getMenu() {
        return menu;
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
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
