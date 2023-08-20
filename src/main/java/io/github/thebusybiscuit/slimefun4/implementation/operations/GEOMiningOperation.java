package io.github.thebusybiscuit.slimefun4.implementation.operations;

import java.util.OptionalInt;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.api.geo.ResourceManager;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineOperation;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOMiner;

/**
 * This {@link MachineOperation} represents a {@link GEOMiner}
 * mining a {@link GEOResource}.
 *
 * @author iTwins
 *
 * @see GEOMiner
 *
 */
public class GEOMiningOperation extends MiningOperation {

    private final GEOResource resource;
    private final Block block;

    public GEOMiningOperation(@Nonnull GEOResource resource, @Nonnull Block block, int totalTicks) {
        super(resource.getItem().clone(), totalTicks);
        this.resource = resource;
        this.block = block;
    }

    /**
     * This returns the {@link GEOResource} back to the chunk
     * when the {@link GEOMiningOperation} gets cancelled
     */
    @Override
    public void onCancel() {
        ResourceManager resourceManager = Slimefun.getGPSNetwork().getResourceManager();
        OptionalInt supplies = resourceManager.getSupplies(resource, block.getWorld(), block.getX() >> 4, block.getZ() >> 4);
        supplies.ifPresent(s -> resourceManager.setSupplies(resource, block.getWorld(), block.getX() >> 4, block.getZ() >> 4, s + 1));
    }

}
