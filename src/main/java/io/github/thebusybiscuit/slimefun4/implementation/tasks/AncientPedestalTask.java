package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Item;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientPedestal;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.AncientAltarListener;

import javax.annotation.Nonnull;

/**
 * The {@link AncientPedestalTask} is responsible for checking item
 * not being moved upon a {@link AncientPedestal}.
 *
 * @author StarWishsama
 *
 * @see AncientPedestal
 * @see AncientAltarListener
 * @see AncientAltarTask
 *
 */
public class AncientPedestalTask implements Runnable {

    private final Map<BlockPosition, Item> pedestalItemCache;

    public AncientPedestalTask(@Nonnull AncientPedestal pedestalItem) {
        this.pedestalItemCache = pedestalItem.getVirtualItemCache();
    }

    @Override
    public void run() {
        if (pedestalItemCache.isEmpty()) {
            return;
        }

        for (Map.Entry<BlockPosition, Item> entry : pedestalItemCache.entrySet()) {
            BlockPosition blockPosition = entry.getKey();
            Item displayItem = entry.getValue();
            Location spawnLocation = blockPosition.toLocation().add(0.5, 1.2, 0.5);

            if (displayItem != null && displayItem.getLocation().distanceSquared(spawnLocation) > 1) {
                if (displayItem.isValid()) {
                    displayItem.teleport(spawnLocation);
                } else {
                    pedestalItemCache.remove(blockPosition);
                }
            }
        }
    }
}
