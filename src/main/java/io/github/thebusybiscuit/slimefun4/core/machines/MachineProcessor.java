package io.github.thebusybiscuit.slimefun4.core.machines;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.api.events.AsyncMachineOperationFinishEvent;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineProcessHolder;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link MachineProcessor} manages different {@link MachineOperation}s and handles
 * their progress.
 *
 * @param <T> The type of {@link MachineOperation} this processor can hold.
 * @author TheBusyBiscuit
 * @see MachineOperation
 * @see MachineProcessHolder
 */
public class MachineProcessor<T extends MachineOperation> {

    private final Map<BlockPosition, T> machines = new ConcurrentHashMap<>();
    private final MachineProcessHolder<T> owner;

    private ItemStack progressBar;

    /**
     * This creates a new {@link MachineProcessor}.
     *
     * @param owner The owner of this {@link MachineProcessor}.
     */
    public MachineProcessor(@Nonnull MachineProcessHolder<T> owner) {
        Validate.notNull(owner, "The MachineProcessHolder cannot be null.");

        this.owner = owner;
    }

    /**
     * This returns the owner of this {@link MachineProcessor}.
     *
     * @return The owner / holder
     */
    public @Nonnull
    MachineProcessHolder<T> getOwner() {
        return owner;
    }

    /**
     * This returns the progress bar icon for this {@link MachineProcessor}
     * or null if no progress bar was set.
     *
     * @return The progress bar icon or null
     */
    public @Nullable
    ItemStack getProgressBar() {
        return progressBar;
    }

    /**
     * This sets the progress bar icon for this {@link MachineProcessor}.
     * You can also set it to null to clear the progress bar.
     *
     * @param progressBar An {@link ItemStack} or null
     */
    public void setProgressBar(@Nullable ItemStack progressBar) {
        this.progressBar = progressBar;
    }

    /**
     * This method will start a {@link MachineOperation} at the given {@link Location}.
     *
     * @param loc       The {@link Location} at which our machine is located.
     * @param operation The {@link MachineOperation} to start
     */
    public void startOperation(@Nonnull Location loc, @Nonnull T operation) {
        Validate.notNull(loc, "The location must not be null");
        Validate.notNull(operation, "The operation cannot be null");

        startOperation(new BlockPosition(loc), operation);
    }

    /**
     * This method will start a {@link MachineOperation} at the given {@link Block}.
     *
     * @param b         The {@link Block} at which our machine is located.
     * @param operation The {@link MachineOperation} to start
     */
    public void startOperation(@Nonnull Block b, @Nonnull T operation) {
        Validate.notNull(b, "The Block must not be null");
        Validate.notNull(operation, "The machine operation cannot be null");

        startOperation(new BlockPosition(b), operation);
    }

    /**
     * This method will actually start the {@link MachineOperation}.
     *
     * @param pos       The {@link BlockPosition} of our machine
     * @param operation The {@link MachineOperation} to start
     */
    public void startOperation(@Nonnull BlockPosition pos, @Nonnull T operation) {
        Validate.notNull(pos, "The BlockPosition must not be null");
        Validate.notNull(operation, "The machine operation cannot be null");

        machines.putIfAbsent(pos, operation);
    }

    /**
     * This returns the current {@link MachineOperation} at that given {@link Location}.
     *
     * @param loc The {@link Location} at which our machine is located.
     * @return The current {@link MachineOperation} or null.
     */
    public @Nullable
    T getOperation(@Nonnull Location loc) {
        Validate.notNull(loc, "The location cannot be null");

        return getOperation(new BlockPosition(loc));
    }

    /**
     * This returns the current {@link MachineOperation} at that given {@link Block}.
     *
     * @param b The {@link Block} at which our machine is located.
     * @return The current {@link MachineOperation} or null.
     */
    public @Nullable
    T getOperation(@Nonnull Block b) {
        Validate.notNull(b, "The Block cannot be null");

        return getOperation(new BlockPosition(b));
    }

    /**
     * This returns the current {@link MachineOperation} at that given {@link BlockPosition}.
     * We don't need to validate our input here as that is already
     * covered in our public methods.
     *
     * @param pos The {@link BlockPosition} at which our machine is located.
     * @return The current {@link MachineOperation} or null.
     */
    public @Nullable
    T getOperation(@Nonnull BlockPosition pos) {
        Validate.notNull(pos, "The BlockPosition must not be null");

        return machines.get(pos);
    }

    /**
     * This will end the {@link MachineOperation} at the given {@link Location}.
     *
     * @param loc The {@link Location} at which our machine is located.
     */
    public void endOperation(@Nonnull Location loc) {
        Validate.notNull(loc, "The location should not be null");

        endOperation(new BlockPosition(loc));
    }

    /**
     * This will end the {@link MachineOperation} at the given {@link Block}.
     *
     * @param b The {@link Block} at which our machine is located.
     */
    public void endOperation(@Nonnull Block b) {
        Validate.notNull(b, "The Block should not be null");

        endOperation(new BlockPosition(b));
    }

    /**
     * This will end the {@link MachineOperation} at the given {@link BlockPosition}.
     *
     * @param pos The {@link BlockPosition} at which our machine is located.
     */
    public void endOperation(@Nonnull BlockPosition pos) {
        Validate.notNull(pos, "The BlockPosition cannot be null");

        T operation = machines.remove(pos);

        if (operation != null) {
            /*
             * Only call an event if the operation actually finished.
             * If it was ended prematurely (aka aborted), then we don't call any event.
             */
            if (operation.isFinished()) {
                Event event = new AsyncMachineOperationFinishEvent(pos, this, operation);
                Bukkit.getPluginManager().callEvent(event);
            }

        }
    }

    public void updateProgressBar(@Nonnull BlockMenu inv, int slot, @Nonnull T operation) {
        Validate.notNull(inv, "The inventory must not be null.");
        Validate.notNull(operation, "The MachineOperation must not be null.");

        if (getProgressBar() == null) {
            // No progress bar, no need to update anything.
            return;
        }

        // Update the progress bar in our inventory (if anyone is watching)
        int remainingTicks = operation.getRemainingTicks();
        int totalTicks = operation.getTotalTicks();

        if (remainingTicks <= 0 && totalTicks <= 0) {
            return;
        }

        ChestMenuUtils.updateProgressbar(inv, slot, remainingTicks, totalTicks, getProgressBar());
    }

}
