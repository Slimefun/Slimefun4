package io.github.thebusybiscuit.slimefun4.implementation.operations;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineOperation;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOMiner;

/**
 * This {@link MachineOperation} represents a {@link GEOMiner}
 * mining a {@link GEOResource}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see GEOMiner
 *
 */
public class MiningOperation implements MachineOperation {

    private final ItemStack result;

    private final int totalTicks;
    private int currentTicks = 0;

    public MiningOperation(@Nonnull ItemStack result, int totalTicks) {
        Preconditions.checkArgument(result != null, "The result cannot be null");
        Preconditions.checkArgument(totalTicks >= 0, "The amount of total ticks must be a positive integer or zero, received: " + totalTicks);

        this.result = result;
        this.totalTicks = totalTicks;
    }

    @Override
    public void addProgress(int num) {
        Preconditions.checkArgument(num > 0, "Progress must be positive.");
        currentTicks += num;
    }

    /**
     * This returns the result of this operation, the {@link ItemStack}
     * that will be returned in the end.
     * 
     * @return The result of this operation
     */
    public @Nonnull ItemStack getResult() {
        return result;
    }

    @Override
    public int getProgress() {
        return currentTicks;
    }

    @Override
    public int getTotalTicks() {
        return totalTicks;
    }

}
