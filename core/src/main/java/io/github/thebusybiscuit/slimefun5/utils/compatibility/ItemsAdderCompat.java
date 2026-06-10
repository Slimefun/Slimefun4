package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * Java-8 universal port: ItemsAdder ({@code dev.lone.itemsadder.api.CustomBlock}) is an optional
 * third-party plugin, not a compile dependency, so its API is reached reflectively. Calls are only made
 * after the integration layer has confirmed the block is an ItemsAdder custom block; when the plugin is
 * absent these degrade to an empty loot list / no-op.
 */
public final class ItemsAdderCompat {

    private static final String CUSTOM_BLOCK = "dev.lone.itemsadder.api.CustomBlock";

    private ItemsAdderCompat() {}

    @SuppressWarnings("unchecked")
    public static List<ItemStack> getLoot(Block block) {
        try {
            Class<?> customBlock = Class.forName(CUSTOM_BLOCK);
            Object instance = customBlock.getMethod("byAlreadyPlaced", Block.class).invoke(null, block);

            if (instance != null) {
                Object loot = customBlock.getMethod("getLoot").invoke(instance);

                if (loot instanceof List) {
                    return (List<ItemStack>) loot;
                }
            }
        } catch (Throwable ignored) {
            // ItemsAdder absent or API changed — no loot.
        }

        return Collections.emptyList();
    }

    public static void remove(Location location) {
        try {
            Class.forName(CUSTOM_BLOCK).getMethod("remove", Location.class).invoke(null, location);
        } catch (Throwable ignored) {
            // ItemsAdder absent — nothing to remove.
        }
    }
}
