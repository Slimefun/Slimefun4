package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.Map;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Item;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientPedestal;

public class AncientPedestalTask implements Runnable {

    private final AncientPedestal pedestalItem = (AncientPedestal) SlimefunItems.ANCIENT_PEDESTAL.getItem();
    private final Map<BlockPosition, Optional<Item>> cache = pedestalItem.getCachedDisplayItems();

    @Override
    public void run() {
        if (cache.isEmpty()) {
            return;
        }

        cache.forEach((blockPosition, display) -> {
            Location spawnLocation = blockPosition.toLocation().add(0.5, 1.2, 0.5);

            if (display.isPresent() && display.get().getLocation().distanceSquared(spawnLocation) > 1) {
                if (display.get().isValid()) {
                    display.get().teleport(spawnLocation);
                } else {
                    cache.remove(blockPosition);
                }
            }
        });
    }
}
