package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/**
 * Compile-only stub for {@code org.bukkit.block.data.type.Farmland} (Minecraft 1.13+).
 */
public interface Farmland extends BlockData {

    int getMoisture();

    void setMoisture(int moisture);

    int getMaximumMoisture();
}
