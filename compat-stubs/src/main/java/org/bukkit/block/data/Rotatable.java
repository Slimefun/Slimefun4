package org.bukkit.block.data;

import org.bukkit.block.BlockFace;

/**
 * Compile-only stub for {@code org.bukkit.block.data.Rotatable} (Minecraft 1.13+).
 */
public interface Rotatable extends BlockData {

    BlockFace getRotation();

    void setRotation(BlockFace rotation);
}
