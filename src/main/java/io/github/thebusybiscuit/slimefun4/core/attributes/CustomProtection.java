package io.github.thebusybiscuit.slimefun4.core.attributes;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import io.github.thebusybiscuit.slimefun4.implementation.items.armor.HazmatArmorPiece;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;

/**
 * Implement this interface for any {@link SlimefunArmorPiece} to prevent
 * the {@link Player} wearing that {@link SlimefunArmorPiece}
 *
 * <b>Important</b>: This will not cancel any {@link EntityDamageEvent}.
 * It will simply prevent Slimefun from ever applying {@link ProtectionType}
 * to this {@link SlimefunArmorPiece}'s wearer.
 *
 * @author Linox
 *
 * @see SlimefunArmorPiece
 * @see HazmatArmorPiece
 * @see ItemAttribute
 *
 */
public interface CustomProtection extends ItemAttribute {

    /**
     * This returns the {@link ProtectionType}s this {@link ItemAttribute}
     * prevents the assigned {@link SlimefunArmorPiece} to be damaged by.
     *
     * @return The {@link ProtectionType}s
     */
    ProtectionType[] getProtectionTypes();

    /**
     * This returns the {@link ProtectionType}s this {@link ItemAttribute}
     * prevents the assigned {@link SlimefunArmorPiece} to be damaged by.
     *
     * @return The {@link ProtectionType}s
     */
    boolean isFullSetRequired();
}
