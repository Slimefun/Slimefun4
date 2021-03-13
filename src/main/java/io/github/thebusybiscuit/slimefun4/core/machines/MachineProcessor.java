package io.github.thebusybiscuit.slimefun4.core.machines;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class MachineProcessor<T extends MachineOperation> {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<BlockPosition, T> machines = new HashMap<>();
    private ItemStack progressBar;

    /**
     * This returns the progress bar icon for this {@link MachineProcessor}
     * or null if no progress bar was set.
     * 
     * @return The progress bar icon or null
     */
    @Nullable
    public ItemStack getProgressBar() {
        return progressBar;
    }

    /**
     * This sets the progress bar icon for this {@link MachineProcessor}.
     * You can also set it to null to clear the progress bar.
     * 
     * @param progressBar
     *            An {@link ItemStack} or null
     */
    public void setProgressBar(@Nullable ItemStack progressBar) {
        this.progressBar = progressBar;
    }

    public boolean addOperation(@Nonnull Location loc, @Nonnull T operation) {
        Validate.notNull(loc, "The location must not be null");
        Validate.notNull(operation, "The machine operation cannot be null");

        return addOperation(new BlockPosition(loc), operation);
    }

    public boolean addOperation(@Nonnull Block b, @Nonnull T operation) {
        Validate.notNull(b, "The Block must not be null");
        Validate.notNull(operation, "The machine operation cannot be null");

        return addOperation(new BlockPosition(b), operation);
    }

    private boolean addOperation(@Nonnull BlockPosition pos, @Nonnull T operation) {
        lock.writeLock().lock();

        try {
            return machines.putIfAbsent(pos, operation) == null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Nullable
    public T getOperation(@Nonnull Location loc) {
        Validate.notNull(loc, "The location cannot be null");

        return getOperation(new BlockPosition(loc));
    }

    @Nullable
    public T getOperation(@Nonnull Block b) {
        Validate.notNull(b, "The Block cannot be null");

        return getOperation(new BlockPosition(b));
    }

    private T getOperation(@Nonnull BlockPosition pos) {
        lock.readLock().lock();

        try {
            return machines.get(pos);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean removeOperation(@Nonnull Location loc) {
        Validate.notNull(loc, "The location should not be null");

        return removeOperation(new BlockPosition(loc));
    }

    public boolean removeOperation(@Nonnull Block b) {
        Validate.notNull(b, "The Block should not be null");

        return removeOperation(new BlockPosition(b));
    }

    private boolean removeOperation(@Nonnull BlockPosition pos) {
        lock.writeLock().lock();

        try {
            return machines.remove(pos) != null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void updateProgressBar(@Nonnull BlockMenu inv, int slot, @Nonnull T operation) {
        if (getProgressBar() == null) {
            // No progress bar
            return;
        }

        int remainingTicks = operation.getRemainingTicks();
        int totalTicks = operation.getTotalTicks();
        ChestMenuUtils.updateProgressbar(inv, slot, remainingTicks, totalTicks, getProgressBar());
    }

}
