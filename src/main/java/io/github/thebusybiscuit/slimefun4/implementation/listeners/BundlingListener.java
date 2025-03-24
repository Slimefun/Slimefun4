package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class BundlingListener implements Listener {
    public BundlingListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBundling(InventoryClickEvent event) {
        ItemStack cursor = event.getCursor();
        ItemStack slot = event.getCurrentItem();

        if (slot == null) {
            return;
        }

        if (isBundle(cursor) && isBackpack(slot) || isBundle(slot) && isBackpack(cursor)) {
            event.setCancelled(true);
        }
    }

    private boolean isBundle(ItemStack stack) {
        return SlimefunTag.BUNDLES.isTagged(stack.getType());
    }

    private boolean isBackpack(ItemStack stack) {
        if (stack.getType() != Material.PLAYER_HEAD) {
            return false;
        }

        SlimefunItem backpack = SlimefunItem.getByItem(stack);
        if (backpack == null) {
            return false;
        }

        return backpack instanceof SlimefunBackpack;
    }
}
