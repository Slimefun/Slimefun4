package io.github.thebusybiscuit.slimefun4.utils;
import org.bukkit.block.Block;
public final class BlockUtils {

    private BlockUtils() {}

    public static boolean hasInventory(Block block) {
        if (block == null) return false;

        switch (block.getType()) {
            case CHEST:
            case TRAPPED_CHEST:
            case FURNACE:
            case DISPENSER:
            case DROPPER:
            case HOPPER:
            case BREWING_STAND:
            case ENDER_CHEST:
            case BARREL:
            case BLAST_FURNACE:
            case LECTERN:
            case SMOKER:
                return true;
            default:
                return block.getType().name().endsWith("SHULKER_BOX");
        }
    }
}
