package org.bukkit.entity;

import org.bukkit.inventory.Inventory;

/**
 * Compile-only stub for {@code org.bukkit.entity.ChestedHorse} (Minecraft 1.11+). Not shaded; on
 * modern servers the real interface is used at runtime. Declared as a marker extending
 * {@link LivingEntity} (a conservative supertype that exists on the 1.8 floor), plus the
 * {@code getInventory()} accessor Slimefun reads (inherited from {@code AbstractHorse} on the real type).
 */
public interface ChestedHorse extends LivingEntity {

    Inventory getInventory();

    boolean isCarryingChest();
}
