package io.github.thebusybiscuit.slimefun4.utils;
import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
public final class BlockUtils {

    private static final boolean is_1_14 = ReflectionUtils.isVersion("v1_14_");

    private BlockUtils() {}

    public static boolean hasInventory(Block block) {
        if (block == null) return false;

        Material type = block.getType();
        switch (type) {
            case CHEST:
            case TRAPPED_CHEST:
            case FURNACE:
            case DISPENSER:
            case DROPPER:
            case HOPPER:
            case BREWING_STAND:
            case ENDER_CHEST:
                return true;
            default:
                if (type.name().endsWith("SHULKER_BOX")) return true;

                return (is_1_14 &&
                        (type == Material.BARREL || type == Material.BLAST_FURNACE || type == Material.LECTERN || type == Material.SMOKER));
        }
    }
}
