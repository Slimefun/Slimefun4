package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * This enum holds all available levels of {@link Radioactivity}.
 * The higher the level the more severe the effect of radiation will be.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Radioactive
 *
 */
public enum Radioactivity {

    /**
     * This represents a low level of radiation.
     * It will still cause damage but will take a while before it becomes deadly.
     */
    LOW(ChatColor.YELLOW),

    /**
     * This represents a medium level of radiation.
     * This can be considered the default.
     */
    MODERATE(ChatColor.YELLOW),

    /**
     * This is a high level of radiation.
     * It will cause death if the {@link Player} does not act quickly.
     */
    HIGH(ChatColor.DARK_GREEN),

    /**
     * A very high level of radiation will be deadly.
     * The {@link Player} should not take this too lightly...
     */
    VERY_HIGH(ChatColor.RED),

    /**
     * This is the deadlies level of radiation.
     * The {@link Player} has basically no chance to protect themselves in time.
     * It will cause certain death.
     */
    VERY_DEADLY(ChatColor.DARK_RED);

    private final ChatColor color;

    Radioactivity(@Nonnull ChatColor color) {
        this.color = color;
    }

    @Nonnull
    public String getLore() {
        return ChatColor.GREEN + "\u2622" + ChatColor.GRAY + " Radiation level: " + color + toString().replace('_', ' ');
    }

    /**
     * This method returns the level for the radiation effect to use in conjunction
     * with this level of {@link Radioactive}.
     * 
     * It is basically the index of this enum constant.
     * 
     * @return The level of radiation associated with this constant.
     */
    public int getRadiationLevel() {
        return ordinal() + 1;
    }

}
