package org.bukkit.inventory.meta;

/**
 * Compile-only stub for {@code org.bukkit.inventory.meta.Damageable} (Minecraft 1.13+). Not shaded;
 * on modern servers the real interface is used at runtime. Signatures match the real type.
 */
public interface Damageable extends ItemMeta {

    boolean hasDamage();

    int getDamage();

    void setDamage(int damage);
}
