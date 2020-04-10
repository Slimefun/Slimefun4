package io.github.thebusybiscuit.slimefun4.core.networks.cargo;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
public final class BlockUtils {

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
                return true;
            default:
                if (type.name().endsWith("SHULKER_BOX")) return true;

                return (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14) &&
                        (type == Material.BARREL || type == Material.BLAST_FURNACE || type == Material.LECTERN || type == Material.SMOKER));
        }
    }
}
