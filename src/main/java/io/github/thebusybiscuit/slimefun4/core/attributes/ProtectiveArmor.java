package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.implementation.items.armor.HazmatArmorPiece;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;

/**
 * Implement this interface to a {@link SlimefunArmorPiece} to protect
 * the {@link Player} who wears that {@link SlimefunArmorPiece} from
 * {@link ProtectionType} damage.
 *
 * <b>Important:</b> You need to specify which {@link ProtectionType} damages
 * to protect the {@link Player} from.
 *
 * @author Linox
 *
 * @see SlimefunArmorPiece
 * @see HazmatArmorPiece
 * @see ItemAttribute
 *
 */
public interface ProtectiveArmor extends ItemAttribute {

    /**
     * This returns which {@link ProtectionType} damages this {@link ItemAttribute}
     * will protect the {@link Player} from.
     *
     * @return The {@link ProtectionType}s.
     */
    @Nonnull
    ProtectionType[] getProtectionTypes();

    /**
     * This returns whether the full set is required for {@link Player}'s protection on
     * assigned {@link ProtectionType} damages.
     *
     * @return Whether or not he full set is required.
     */
    boolean isFullSetRequired();

    /**
     * This returns the armor set {@link NamespacedKey} of this {@link SlimefunArmorPiece}.
     *
     * @return The set {@link NamespacedKey}, <code>null</code> if none is found.
     */
    @Nullable
    NamespacedKey getArmorSetId();
}
