package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientPedestal;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.AncientAltarListener;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

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
    private final Slimefun plugin;
    private final BukkitScheduler scheduler;

    public AncientPedestalTask(@Nonnull Slimefun plugin, @Nonnull AncientPedestal pedestalItem) {
        this.plugin = plugin;
        scheduler = plugin.getServer().getScheduler();
        this.virtualItemCache = pedestalItem.getVirtualItemCache();

        scheduler.runTaskTimerAsynchronously(plugin, this, 5 * 20L, 5 * 20L);
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
                    Entity displayItem = scheduler.callSyncMethod(plugin, () -> Bukkit.getEntity(itemUUID)).get(3, TimeUnit.SECONDS);
                    if (displayItem != null && displayItem.getLocation().distanceSquared(spawnLocation) > 1) {
                        if (displayItem.isValid()) {
                            displayItem.teleport(spawnLocation);
                        } else {
                            virtualItemCache.remove(blockPosition);
                        }
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException x) {
                    Slimefun.logger().log(Level.WARNING, x, () -> "An Error occurred while processing pedestal item!");
                }
            }
        }
    }
}
