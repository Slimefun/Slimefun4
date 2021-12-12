package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.services.holograms.HologramsService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.HologramProjector;

/**
 * This {@link ItemAttribute} manages holograms.
 * 
 * @author TheBusyBiscuit
 * 
 * @see HologramProjector
 * @see HologramsService
 *
 */
public interface HologramOwner extends ItemAttribute {

    /**
     * This will update the hologram text for the given {@link Block}.
     * 
     * @param b
     *            The {@link Block} to which the hologram belongs
     * 
     * @param text
     *            The nametag for the hologram
     */
    default void updateHologram(@Nonnull Block b, @Nonnull String text) {
        Location loc = b.getLocation().add(getHologramOffset(b));
        HologramsService service = Slimefun.getHologramsService();

        Runnable runnable = () -> {
            if (service.getHologram(loc, false) != null) {
                service.setHologramLabel(loc, ChatColors.color(text));
            }
        };

        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            Slimefun.runSync(runnable);
        }
    }

    /**
     * This will remove the hologram for the given {@link Block}.
     * 
     * @param b
     *            The {@link Block} to which the hologram blocks
     */
    default void removeHologram(@Nonnull Block b) {
        Location loc = b.getLocation().add(getHologramOffset(b));
        Slimefun.getHologramsService().removeHologram(loc);
    }

    /**
     * This returns a {@link BlockPlaceHandler} which creates a
     * hologram with an empty label which will be replaced
     * when the machine generating the hologram ticks
     *
     * @return The {@link BlockPlaceHandler}
     */
    default BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                Runnable runnable = () -> {
                    HologramsService service = Slimefun.getHologramsService();
                    Block block = e.getBlock();
                    Location loc = block.getLocation().add(getHologramOffset(block));

                    service.createHologram(loc, null);
                };

                if (Bukkit.isPrimaryThread()) {
                    runnable.run();
                } else {
                    Slimefun.runSync(runnable);
                }
            }

        };
    }

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

}
