package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class BundlingListener implements Listener {
    public BundlingListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void onBundling(InventoryDragEvent event) {

        ItemStack cursor = event.getCursor();
        if (cursor == null) {
            return;
        }

        if (!SlimefunTag.BUNDLES.isTagged(cursor.getType())) {
            return;
        }
    }
}
