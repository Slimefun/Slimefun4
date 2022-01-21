package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;


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

    private final Map<BlockPosition, UUID> virtualItemCache;

    public AncientPedestalTask(@Nonnull AncientPedestal pedestalItem) {
        this.virtualItemCache = pedestalItem.getVirtualItemCache();
    }

    @Override
    public void run() {
        if (virtualItemCache.isEmpty()) {
            return;
        }

        for (Map.Entry<BlockPosition, UUID> entry : virtualItemCache.entrySet()) {
            BlockPosition blockPosition = entry.getKey();
            UUID itemUUID = entry.getValue();
            Location spawnLocation = blockPosition.toLocation().add(0.5, 1.2, 0.5);

            if (itemUUID != null) {
                // Avoid async catcher
                try {
                    Entity displayItem = Slimefun.instance().getServer().getScheduler().callSyncMethod(Slimefun.instance(), () -> Bukkit.getEntity(itemUUID)).get();
                    if (displayItem != null && displayItem.getLocation().distanceSquared(spawnLocation) > 1) {
                        if (displayItem.isValid()) {
                            displayItem.teleport(spawnLocation);
                        } else {
                            virtualItemCache.remove(blockPosition);
                        }
                    }
                } catch (InterruptedException | ExecutionException x) {
                    Slimefun.logger().log(Level.WARNING, x, () -> "An Error occurred while processing pedestal item!");
                }
            }
        }
    }
}
