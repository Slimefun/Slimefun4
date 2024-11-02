package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.core.services.holograms.HologramsService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.HologramProjector;

/**
 * This {@link ItemAttribute} manages holograms.
 * 
 * @author TheBusyBiscuit, JustAHuman
 * 
 * @see HologramProjector
 * @see HologramsService
 */
public interface HologramOwner extends ItemAttribute {
    /**
     * This returns the offset of the hologram as a {@link Vector}.
     * This offset is applied to {@link Block#getLocation()} when spawning
     * the hologram.
     *
     * @param block
     *            The {@link Block} which serves as the origin point
     *
     * @return The hologram offset
     */
    @Nonnull
    default Vector getHologramOffset(@Nonnull Block block) {
        return Slimefun.getHologramsService().getDefaultOffset();
    }

    /**
     * This will update the hologram text for the given {@link Block}.
     * 
     * @param block
     *            The {@link Block} to which the hologram belongs
     * 
     * @param text
     *            The text for the hologram
     */
    default void updateHologram(@Nonnull Block block, @Nonnull String text) {
        Location location = block.getLocation();
        if (Slimefun.getTickerTask().isDeletedSoon(location)) {
            return;
        }

        Slimefun.getHologramsService().setHologramLabel(location.add(getHologramOffset(block)), ChatColors.color(text));
    }

    /**
     * This will update the hologram text for the given {@link Block}.
     *
     * @param block
     *            The {@link Block} to which the hologram belongs
     *
     * @param offset
     *            The new offset for the hologram
     */
    default void setOffset(@Nonnull Block block, @Nonnull Vector offset) {
        Location location = block.getLocation();
        if (Slimefun.getTickerTask().isDeletedSoon(location)) {
            return;
        }

        Location hologramLocation = location.clone().add(getHologramOffset(block));
        Location newHologramLocation = location.clone().add(offset);
        Slimefun.getHologramsService().teleportHologram(hologramLocation, newHologramLocation);
    }

    /**
     * This will remove the hologram for the given {@link Block}.
     * 
     * @param block
     *            The {@link Block} to which the hologram blocks
     */
    default void removeHologram(@Nonnull Block block) {
        Location location = block.getLocation().add(getHologramOffset(block));
        Slimefun.getHologramsService().removeHologram(location);
    }
}
