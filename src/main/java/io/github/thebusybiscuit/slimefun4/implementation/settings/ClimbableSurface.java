package io.github.thebusybiscuit.slimefun4.implementation.settings;

import javax.annotation.Nonnull;

import org.bukkit.Material;

import io.github.thebusybiscuit.slimefun4.api.events.ClimbingPickLaunchEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.settings.DoubleRangeSetting;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ClimbingPick;

/**
 * This is an {@link ItemSetting}
 * 
 * @author TheBusyBiscuit
 * 
 * @see ClimbingPick
 * @see ClimbingPickLaunchEvent
 *
 */
public class ClimbableSurface extends DoubleRangeSetting {

    private final Material type;

    /**
     * This creates a new {@link ClimbableSurface} for the given {@link Material}.
     * 
     * @param surface
     *            The {@link Material} of this surface
     * @param defaultValue
     *            The default launch amount
     */
    public ClimbableSurface(@Nonnull Material surface, double defaultValue) {
        super("launch-amounts." + surface.name(), 0, defaultValue, Double.MAX_VALUE);
        this.type = surface;
    }

    /**
     * This returns the {@link Material} of this surface.
     * 
     * @return The {@link Material} of this surface
     */
    @Nonnull
    public Material getType() {
        return type;
    }

}
