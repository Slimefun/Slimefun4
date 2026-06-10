package org.bukkit.block.data.type;

import java.util.Set;

import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;

/**
 * Compile-only stub for {@code org.bukkit.block.data.type.GlassPane} (Minecraft 1.13+). Mirrors the
 * subset of {@code MultipleFacing} and {@code Waterlogged} that Slimefun uses.
 */
public interface GlassPane extends Waterlogged {

    boolean hasFace(BlockFace face);

    void setFace(BlockFace face, boolean has);

    Set<BlockFace> getFaces();

    Set<BlockFace> getAllowedFaces();
}
