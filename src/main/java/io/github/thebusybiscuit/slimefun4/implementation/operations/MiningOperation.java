package io.github.thebusybiscuit.slimefun4.implementation.operations;

import java.util.OptionalInt;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.api.geo.ResourceManager;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineOperation;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
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

    private final GEOResource resource;
    private final Block block;
    private final int totalTicks;
    private int currentTicks = 0;

    public MiningOperation(@Nonnull GEOResource resource, Block block, int totalTicks) {
        Preconditions.checkArgument(resource != null, "The resource cannot be null");
        Preconditions.checkArgument(totalTicks >= 0, "The amount of total ticks must be a positive integer or zero, received: " + totalTicks);

        this.resource = resource;
        this.result = resource.getItem().clone();
        this.block = block;
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

    @Override
    public void cancel() {
        ResourceManager resourceManager = Slimefun.getGPSNetwork().getResourceManager();
        OptionalInt supplies = resourceManager.getSupplies(resource, block.getWorld(), block.getX() >> 4, block.getZ() >> 4);
        supplies.ifPresent(s -> resourceManager.setSupplies(resource, block.getWorld(), block.getX() >> 4, block.getZ() >> 4, s + 1));
    }

}
