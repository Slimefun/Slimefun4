package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.enchanting.AutoDisenchanter;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * An {@link Event} that is fired just after {@link AutoDisenchantEvent} if and only if it was not cancelled.
 * This event will start out as being cancelled unless it contains a bukkit enchantment.
 * The enchantment transfer logic is already performed by slimefun before this event is fired, though this
 * logic does not affect input items.
 * The main usecase of this event is to control the output items or to control the time the disenchanting process takes.
 *
 * @author Geolykt
 */
public class AsyncAutoDisenchanterProcessEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Result result = Result.DEFAULT;
    private final BlockMenu menu;
    private final ItemStack inputBook;
    private final ItemStack inputItem;
    private ItemStack outputBook;
    private ItemStack outputStack;
    private int transferredEnchantmentsAmount;

    @ParametersAreNonnullByDefault
    public AsyncAutoDisenchanterProcessEvent(BlockMenu menu, ItemStack inBook, ItemStack inItem, ItemStack outBook, ItemStack outStack, int transferredEnchantmentsAmount) {
        super(true);
        this.menu = menu;
        this.inputBook = inBook;
        this.inputItem = inItem;
        setOutputBook(outBook);
        setOutputStack(outStack);
        setTransferredEnchantmentsAmount(transferredEnchantmentsAmount);
    }

    public static @Nonnull HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @Nonnull HandlerList getHandlers() {
        return getHandlerList();
    }

    /**
     * This returns the input {@link ItemStack} to disenchant. The returned instance should not be modified.
     *
     * @return The {@link ItemStack} that is being disenchanted
     */
    public @Nonnull ItemStack getInputBook() {
        return inputBook;
    }

    /**
     * This returns the input {@link ItemStack} which represents the book where the enchantments are applied onto.
     * The returned instance should not be modified.
     *
     * @return The input {@link ItemStack} book
     */
    public @Nonnull ItemStack getInputItem() {
        return inputItem;
    }

    /**
     * This returns the {@link AutoDisenchanter}'s {@link BlockMenu}.
     *
     * @return The {@link BlockMenu} of {@link AutoDisenchanter} that is enchanting item
     */
    public BlockMenu getMenu() {
        return menu;
    }

    /**
     * This returns the output {@link ItemStack} that represents the book after the disenchanting process.
     * This instance can be directly modified, however usage of {@link #setOutputBook(ItemStack)} is equally valid.
     *
     * @return The output {@link ItemStack} book
     */
    public @Nonnull ItemStack getOutputBook() {
        return outputBook;
    }

    /**
     * This returns the output {@link ItemStack} to disenchant with the enchantments being stripped.
     * This instance can be directly modified, however usage of {@link #setOutputStack(ItemStack)} is equally valid.
     *
     * @return The output {@link ItemStack} that is being disenchanted
     */
    public @Nonnull ItemStack getOutputStack() {
        // Lazily clone the output stack instance if it is the same instance as the input stack instance
        // Therefore the instance comparison is intended
        // The alternative would be to forbid mutating the itemstack through the documentation
        // but this will likely result in excess amounts of cloning or unstable (and potentially exploitable) code
        if (outputStack == inputItem) {
            outputStack = outputStack.clone();
        }
        return outputStack;
    }

    /**
     * Obtains the result of the event. If the result is {@link Result#DENY} then the disenchanting process 
     * will be denied and will also be denied if it is set to {@link Result#DEFAULT} and slimefun did not transfer
     * any enchantments. The default value is {@link Result#DEFAULT}, where as plugins should set it to other values
     * via {@link #setResult(Result)} if applicable.
     *
     * @return The current {@link Result} of the event
     */
    public @Nonnull Result getResult() {
        return result;
    }

    /**
     * Obtains the amount of transferred enchantments. This includes the enchantments transferred by slimefun.
     * This value is used to compute the duration for which the AutoDisenchanter stays active.
     *
     * @return The amount of transferred enchantments
     */
    public int getTransferredEnchantmentsAmount() {
        return transferredEnchantmentsAmount;
    }

    /**
     * This sets the output {@link ItemStack} that represents the book after the disenchanting process.
     * If you wish to increase the time the disenchanting process is taking you must use
     * {@link #setTransferredEnchantmentsAmount(int)}.
     *
     * @param outputBook The output {@link ItemStack} book
     */
    public void setOutputBook(@Nonnull ItemStack outputBook) {
        Validate.notNull(outputBook, "The outputBook parameter may not be null");
        this.outputBook = outputBook;
    }

    /**
     * This sets the output {@link ItemStack} to disenchant with the enchantments being stripped.
     *
     * @param outputStack The output {@link ItemStack} that is being disenchanted
     */
    public void setOutputStack(@Nonnull ItemStack outputStack) {
        Validate.notNull(outputStack, "The outputStack parameter may not be null");
        this.outputStack = outputStack;
    }

    /**
     * Sets the result of the event.
     * Listeners should only set it to {@link Result#ACCEPT} or {@link Result#DENY}.
     *
     * @param result The new {@link Result} of the event
     */
    public void setResult(@Nonnull Result result) {
        Validate.notNull(result, "The new result must not be null");
        this.result = result;
    }

    /**
     * Sets the amount of transferred enchantments. This includes the enchantments transferred by slimefun.
     * The value is used to compute the duration for which the AutoDisenchanter stays active.
     *
     * @param transferredEnchantmentsAmount The amount of transferred enchantments
     */
    public void setTransferredEnchantmentsAmount(int transferredEnchantmentsAmount) {
        if (transferredEnchantmentsAmount < 0) {
            throw new IllegalArgumentException("transferredEnchantmentsAmount must be a non-negative integer but was " + transferredEnchantmentsAmount);
        }
        this.transferredEnchantmentsAmount = transferredEnchantmentsAmount;
    }
}
