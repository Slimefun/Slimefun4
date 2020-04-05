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
            case SHULKER_BOX:
            case WHITE_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case LIGHT_GRAY_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case BLACK_SHULKER_BOX:
            case ENDER_CHEST:
            case BARREL:
            case BLAST_FURNACE:
            case LECTERN:
            case SMOKER:
                return true;
            default:
                return false;
        }
    }

}
