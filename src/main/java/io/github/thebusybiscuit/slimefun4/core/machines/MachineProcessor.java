package io.github.thebusybiscuit.slimefun4.core.machines;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;

public class MachineProcessor<T extends MachineOperation> {

    private final Map<BlockPosition, T> machines = new HashMap<>();
    private ItemStack progressBar;

    @Nullable
    public ItemStack getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(@Nullable ItemStack progressBar) {
        this.progressBar = progressBar;
    }

    public boolean addOperation(@Nonnull Location loc, @Nonnull T operation) {
        Validate.notNull(loc, "The location must not be null");
        Validate.notNull(operation, "The machine operation cannot be null");

        return machines.putIfAbsent(new BlockPosition(loc), operation) == null;
    }

    public boolean addOperation(@Nonnull Block b, @Nonnull T operation) {
        Validate.notNull(b, "The Block must not be null");
        Validate.notNull(operation, "The machine operation cannot be null");

        return machines.putIfAbsent(new BlockPosition(b), operation) == null;
    }

    @Nonnull
    public Optional<T> getOperation(@Nonnull Location loc) {
        Validate.notNull(loc, "The location cannot be null");

        return Optional.ofNullable(machines.get(new BlockPosition(loc)));
    }

    @Nonnull
    public Optional<T> getOperation(@Nonnull Block b) {
        Validate.notNull(b, "The Block cannot be null");

        return Optional.ofNullable(machines.get(new BlockPosition(b)));
    }

    public boolean removeOperation(@Nonnull Location loc) {
        Validate.notNull(loc, "The location should not be null");

        return machines.remove(new BlockPosition(loc)) != null;
    }

    public boolean removeOperation(@Nonnull Block b) {
        Validate.notNull(b, "The Block should not be null");

        return machines.remove(new BlockPosition(b)) != null;
    }

}
