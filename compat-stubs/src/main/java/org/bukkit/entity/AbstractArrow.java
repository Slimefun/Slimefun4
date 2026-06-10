package org.bukkit.entity;

/**
 * Compile-only stub for {@code org.bukkit.entity.AbstractArrow} (Minecraft 1.14+). Not shaded; on
 * modern servers the real interface is used at runtime.
 */
public interface AbstractArrow extends Projectile {

    int getPierceLevel();

    void setPierceLevel(int level);

    PickupStatus getPickupStatus();

    void setPickupStatus(PickupStatus status);

    double getDamage();

    void setDamage(double damage);

    enum PickupStatus {
        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY;
    }
}
