package io.github.thebusybiscuit.slimefun4.utils;
import org.bukkit.Material;
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
                return isShulkerBox(block);
        }
    }

    public static boolean isShulkerBox(Block block) {
        Material type = block.getType();
        return type == Material.SHULKER_BOX || type == Material.WHITE_SHULKER_BOX || type == Material.ORANGE_SHULKER_BOX
                || type == Material.MAGENTA_SHULKER_BOX || type == Material.LIGHT_BLUE_SHULKER_BOX
                || type == Material.YELLOW_SHULKER_BOX || type == Material.LIME_SHULKER_BOX
                || type == Material.PINK_SHULKER_BOX || type == Material.GRAY_SHULKER_BOX
                || type == Material.LIGHT_GRAY_SHULKER_BOX || type == Material.CYAN_SHULKER_BOX
                || type == Material.PURPLE_SHULKER_BOX || type == Material.BLUE_SHULKER_BOX || type == Material.BROWN_SHULKER_BOX
                || type == Material.GREEN_SHULKER_BOX || type == Material.RED_SHULKER_BOX || type == Material.BLACK_SHULKER_BOX;
    }

}
