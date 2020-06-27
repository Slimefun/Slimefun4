package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GrapplingHook;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class GrapplingHookListener implements Listener {

    private GrapplingHook grapplingHook;

    private final Map<UUID, Boolean> grappleState = new HashMap<>();
    private final Set<UUID> invulnerable = new HashSet<>();
    private final Map<UUID, Entity[]> temporaryEntities = new HashMap<>();

    public void register(SlimefunPlugin plugin, GrapplingHook grapplingHook) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.grapplingHook = grapplingHook;
    }

    @EventHandler
    public void onArrowHitEntity(EntityDamageByEntityEvent e) {
        if (grapplingHook == null || grapplingHook.isDisabled()) {
            return;
        }

        if (e.getDamager() instanceof Arrow) {
            handleGrapplingHook((Arrow) e.getDamager());
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent e) {
        if (grapplingHook == null || grapplingHook.isDisabled()) {
            return;
        }

        Slimefun.runSync(() -> {
            if (e.getEntity().isValid() && e.getEntity() instanceof Arrow) {
                handleGrapplingHook((Arrow) e.getEntity());
            }
        }, 4L);
    }

    @EventHandler
    public void onArrowHit(EntityDamageEvent e) {
        if (grapplingHook == null || grapplingHook.isDisabled()) {
            return;
        }

        if (e.getEntity() instanceof Player && e.getCause() == DamageCause.FALL && invulnerable.contains(e.getEntity().getUniqueId())) {
            e.setCancelled(true);
            invulnerable.remove(e.getEntity().getUniqueId());
        }
    }

    private void handleGrapplingHook(Arrow arrow) {
        if (arrow != null && arrow.getShooter() instanceof Player && grappleState.containsKey(((Player) arrow.getShooter()).getUniqueId())) {
            Player p = (Player) arrow.getShooter();

            if (p.getGameMode() != GameMode.CREATIVE && (boolean) grappleState.get(p.getUniqueId())) {
                arrow.getWorld().dropItem(arrow.getLocation(), SlimefunItems.GRAPPLING_HOOK.clone());
            }

            Vector velocity = new Vector(0.0, 0.2, 0.0);

            if (p.getLocation().distance(arrow.getLocation()) < 3.0) {
                if (arrow.getLocation().getY() <= p.getLocation().getY()) {
                    velocity = arrow.getLocation().toVector().subtract(p.getLocation().toVector());
                }
            }
            else {
                Location l = p.getLocation();
                l.setY(l.getY() + 0.5);
                p.teleport(l);

                double g = -0.08;
                double d = arrow.getLocation().distance(l);
                double t = d;
                double vX = (1.0 + 0.08 * t) * (arrow.getLocation().getX() - l.getX()) / t;
                double vY = (1.0 + 0.04 * t) * (arrow.getLocation().getY() - l.getY()) / t - 0.5D * g * t;
                double vZ = (1.0 + 0.08 * t) * (arrow.getLocation().getZ() - l.getZ()) / t;

                velocity = p.getVelocity();
                velocity.setX(vX);
                velocity.setY(vY);
                velocity.setZ(vZ);
            }

            p.setVelocity(velocity);

            for (Entity n : temporaryEntities.get(p.getUniqueId())) {
                if (n.isValid()) {
                    n.remove();
                }
            }

            Slimefun.runSync(() -> {
                grappleState.remove(p.getUniqueId());
                temporaryEntities.remove(p.getUniqueId());
            }, 20L);
        }
    }

    public boolean isGrappling(UUID uuid) {
        return grappleState.containsKey(uuid);
    }

    public void addGrapplingHook(UUID uuid, Arrow arrow, Bat b, boolean state, long despawnTicks) {
        grappleState.put(uuid, state);
        invulnerable.add(uuid);
        temporaryEntities.put(uuid, new Entity[] { b, arrow });

        // To fix issue #253
        Slimefun.runSync(() -> {
            if (grappleState.containsKey(uuid)) {
                SlimefunPlugin.getBowListener().getProjectileData().remove(uuid);

                for (Entity n : temporaryEntities.get(uuid)) {
                    if (n.isValid()) {
                        n.remove();
                    }
                }

                Slimefun.runSync(() -> {
                    invulnerable.remove(uuid);
                    grappleState.remove(uuid);
                    temporaryEntities.remove(uuid);
                }, 20L);
            }
        }, despawnTicks);
    }
}
