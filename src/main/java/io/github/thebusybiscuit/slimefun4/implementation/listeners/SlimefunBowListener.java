package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import io.github.thebusybiscuit.slimefun4.implementation.items.weapons.SlimefunBow;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BowShootHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class SlimefunBowListener implements Listener {

    private final Map<UUID, SlimefunBow> bows = new HashMap<>();

    public SlimefunBowListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public Map<UUID, SlimefunBow> getBows() {
        return bows;
    }

    @EventHandler
    public void onBowUse(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player && e.getProjectile() instanceof Arrow) {
            SlimefunItem bow = SlimefunItem.getByItem(e.getBow());

            if (bow instanceof SlimefunBow) {
                bows.put(e.getProjectile().getUniqueId(), (SlimefunBow) bow);
            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent e) {
        Slimefun.runSync(() -> {
            if (e.getEntity().isValid() && e.getEntity() instanceof Arrow) {
                bows.remove(e.getEntity().getUniqueId());
            }
        }, 4L);
    }

    @EventHandler
    public void onArrowSuccessfulHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow && e.getEntity() instanceof LivingEntity) {
            SlimefunBow bow = bows.get(e.getDamager().getUniqueId());

            if (bow != null) {
                bow.callItemHandler(BowShootHandler.class, handler -> handler.onHit(e, (LivingEntity) e.getEntity()));
            }

            bows.remove(e.getDamager().getUniqueId());
        }
    }

}
