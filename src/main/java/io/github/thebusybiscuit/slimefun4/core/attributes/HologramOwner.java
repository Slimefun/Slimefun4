package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.HologramProjector;

/**
 * This {@link ItemAttribute} manages holograms.
 * 
 * @author TheBusyBiscuit
 * 
 * @see HologramProjector
 *
 */
public interface HologramOwner extends ItemAttribute {

    default void updateHologram(@Nonnull Block b, @Nonnull String text) {
        Location loc = b.getLocation().add(getHologramOffset());
        SlimefunPlugin.getHologramsService().updateHologram(loc, hologram -> {
            hologram.setCustomName(ChatColors.color(text));
            hologram.setCustomNameVisible(true);
        });
    }

    @Nonnull
    default Vector getHologramOffset() {
        return new Vector(0.5, 0.75, 0.5);
    }

}
