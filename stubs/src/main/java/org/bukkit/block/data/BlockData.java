package org.bukkit.block.data;

/**
 * Compile-only stub for {@code org.bukkit.block.data.BlockData} (added in Minecraft 1.13). Not
 * shaded; on modern servers the real interface is used at runtime. Declared as a marker — concrete
 * methods live on the sub-interfaces that Slimefun actually calls.
 */
public interface BlockData {

    org.bukkit.Material getMaterial();

    boolean isSupported(org.bukkit.block.Block block);
}
