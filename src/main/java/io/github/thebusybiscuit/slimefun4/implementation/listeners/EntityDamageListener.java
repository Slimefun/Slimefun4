package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.EntityDamageHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class EntityDamageListener implements Listener {

    public EntityDamageListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity) {
            Player p = (Player) e.getDamager();

            ItemStack item = p.getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) return;

            SlimefunItem sfItem = SlimefunItem.getByItem(item);
            if (sfItem != null && Slimefun.hasUnlocked(p, sfItem, true)) {
                sfItem.callItemHandler(EntityDamageHandler.class, handler -> handler.onDamage(e, e.getEntity(), item));
            }
        }
    }
}
