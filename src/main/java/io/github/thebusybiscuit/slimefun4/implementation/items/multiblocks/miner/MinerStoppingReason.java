package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner;

import javax.annotation.Nonnull;

import org.bukkit.block.Chest;
import org.bukkit.block.data.type.Piston;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlock;

/**
 * This enum holds various different reasons as to why an {@link IndustrialMiner}
 * may stop mining.
 * 
 * @author TheBusyBiscuit
 *
 */
enum MinerStoppingReason {

    /**
     * The {@link IndustrialMiner} has run out of fuel.
     */
    NO_FUEL("machines.INDUSTRIAL_MINER.no-fuel"),

    /**
     * The {@link IndustrialMiner} has entered a region where
     * the {@link Player} has no permission to build.
     */
    NO_PERMISSION("machines.INDUSTRIAL_MINER.no-permission"),

    /**
     * The {@link Chest} of our {@link IndustrialMiner} is full.
     */
    CHEST_FULL("machines.INDUSTRIAL_MINER.chest-full"),

    /**
     * The {@link MultiBlock} structure of the {@link IndustrialMiner}
     * has been destroyed.
     */
    STRUCTURE_DESTROYED("machines.INDUSTRIAL_MINER.destroyed"),

    /**
     * The {@link Piston}s inside the structure faces the wrong way.
     */
    PISTON_WRONG_DIRECTION("machines.INDUSTRIAL_MINER.piston-facing"),

    /**
     * The {@link Piston}s have no space to move.
     */
    PISTON_NO_SPACE("machines.INDUSTRIAL_MINER.piston-space");

    private final String messageKey;

    MinerStoppingReason(@Nonnull String messageKey) {
        this.messageKey = messageKey;
    }

    @Nonnull
    String getErrorMessage() {
        return messageKey;
    }

}
