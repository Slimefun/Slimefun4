package io.github.thebusybiscuit.slimefun4.implementation.settings;

import javax.annotation.Nonnull;

import org.bukkit.Material;

import io.github.thebusybiscuit.slimefun4.api.events.ClimbingPickLaunchEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
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
public class ClimbableSurface extends ItemSetting<Double> {

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
        super("launch-amounts." + surface.name(), defaultValue);
        this.type = surface;
    }

    @Override
    public boolean validateInput(Double input) {
        return super.validateInput(input) && input >= 0;
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
