package io.github.thebusybiscuit.slimefun4.core.attributes;

import org.bukkit.ChatColor;

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

    LOW(ChatColor.YELLOW), 
    MODERATE(ChatColor.YELLOW), 
    HIGH(ChatColor.DARK_GREEN), 
    VERY_HIGH(ChatColor.RED), 
    VERY_DEADLY(ChatColor.DARK_RED);

    private final ChatColor color;

    private Radioactivity(ChatColor color) {
        this.color = color;
    }

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
    public int getLevel() {
        return ordinal() + 1;
    }

}
