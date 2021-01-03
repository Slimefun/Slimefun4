package io.github.thebusybiscuit.slimefun4.core.attributes;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.HologramProjector;

/**
 * 
 * 
 * @author TheBusyBiscuit
 * 
 * @see HologramProjector
 *
 */
public interface HologramOwner extends ItemAttribute {

    default void updateHologram(@Nonnull Block b, @Nonnull String text) {
        ArmorStand hologram = getHologram(b, true);
        hologram.setCustomName(ChatColors.color(text));
        hologram.setCustomNameVisible(true);
    }

    @Nonnull
    default Vector getHologramOffset() {
        return new Vector(0.5, 0.75, 0.5);
    }

    @Nullable
    default ArmorStand getHologram(@Nonnull Block b, boolean createIfNoneExists) {
        Location loc = b.getLocation().add(getHologramOffset());
        Collection<Entity> holograms = b.getWorld().getNearbyEntities(loc, 0.4, 0.4, 0.4, n -> n instanceof ArmorStand && isHologram(n));

        for (Entity n : holograms) {
            if (n instanceof ArmorStand) {
                PersistentDataContainer container = n.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(SlimefunPlugin.instance(), "hologram_id");
                String value = b.getX() + ";" + b.getY() + ";" + b.getZ();

                if (container.has(key, PersistentDataType.STRING)) {
                    if (container.get(key, PersistentDataType.STRING).equals(value)) {
                        return (ArmorStand) n;
                    }
                } else {
                    container.set(key, PersistentDataType.STRING, value);
                    return (ArmorStand) n;
                }
            }
        }

        if (!createIfNoneExists) {
            return null;
        } else {
            ArmorStand hologram = (ArmorStand) b.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);

            hologram.setSilent(true);
            hologram.setMarker(true);
            hologram.setAI(false);
            hologram.setGravity(false);

            return hologram;
        }
    }

    default boolean isHologram(@Nonnull Entity n) {
        if (n instanceof ArmorStand) {
            ArmorStand armorstand = (ArmorStand) n;
            return armorstand.isValid() && armorstand.isSilent() && armorstand.isMarker() && !armorstand.hasGravity();
        } else {
            return false;
        }
    }

    default void removeHologram(@Nonnull Block b) {
        ArmorStand hologram = getHologram(b, false);

        if (hologram != null) {
            hologram.remove();
        }
    }

}
