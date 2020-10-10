package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.handlers.EntityInteractHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * The Listener class responsible for a {@link Player} interacting with an {@link Entity}.
 *
 * @author Linox
 *
 * @see EntityInteractHandler
 *
 */
public class EntityInteractionListener implements Listener {

    public EntityInteractionListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        if (!e.getRightClicked().isValid()) {
            return;
        }

        ItemStack itemStack;

        if (e.getHand() == EquipmentSlot.OFF_HAND) {
            itemStack = e.getPlayer().getInventory().getItemInOffHand();
        } else {
            itemStack = e.getPlayer().getInventory().getItemInMainHand();
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(itemStack);

        if (sfItem != null && Slimefun.hasUnlocked(e.getPlayer(), sfItem, true)) {
            sfItem.callItemHandler(EntityInteractHandler.class, handler -> handler.onInteract(e, itemStack, e.getHand() == EquipmentSlot.OFF_HAND));
        }
    }
}