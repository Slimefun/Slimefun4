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

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class GrapplingHookListener implements Listener {

    private final Map<UUID, Boolean> jumpState = new HashMap<>();
    private final Set<UUID> invulnerable = new HashSet<>();
    private final Map<UUID, Entity[]> temporaryEntities = new HashMap<>();

    public void load(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onArrowHitEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow) {
            handleGrapplingHook((Arrow) e.getDamager());
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent e) {
        Slimefun.runSync(() -> {
            if (e.getEntity().isValid() && e.getEntity() instanceof Arrow) {
                handleGrapplingHook((Arrow) e.getEntity());
            }
        }, 4L);
    }

    @EventHandler
    public void onArrowHit(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() == DamageCause.FALL && invulnerable.contains(e.getEntity().getUniqueId())) {
            e.setCancelled(true);
            invulnerable.remove(e.getEntity().getUniqueId());
        }
    }

    private void handleGrapplingHook(Arrow arrow) {
        if (arrow != null && arrow.getShooter() instanceof Player && jumpState.containsKey(((Player) arrow.getShooter()).getUniqueId())) {
            Player p = (Player) arrow.getShooter();

            if (p.getGameMode() != GameMode.CREATIVE && (boolean) jumpState.get(p.getUniqueId())) {
                arrow.getWorld().dropItem(arrow.getLocation(), SlimefunItems.GRAPPLING_HOOK);
            }

            if (p.getLocation().distance(arrow.getLocation()) < 3.0D) {
                if (arrow.getLocation().getY() > p.getLocation().getY()) {
                    p.setVelocity(new Vector(0.0D, 0.25D, 0.0D));
                }
                else {
                    p.setVelocity(arrow.getLocation().toVector().subtract(p.getLocation().toVector()));
                }

                for (Entity n : temporaryEntities.get(p.getUniqueId())) {
                    if (n.isValid()) {
                        n.remove();
                    }
                }

                Slimefun.runSync(() -> {
                    jumpState.remove(p.getUniqueId());
                    temporaryEntities.remove(p.getUniqueId());
                }, 20L);
            }
            else {
                Location l = p.getLocation();
                l.setY(l.getY() + 0.5D);
                p.teleport(l);

                double g = -0.08D;
                double d = arrow.getLocation().distance(l);
                double t = d;
                double vX = (1.0D + 0.08000000000000001D * t) * (arrow.getLocation().getX() - l.getX()) / t;
                double vY = (1.0D + 0.04D * t) * (arrow.getLocation().getY() - l.getY()) / t - 0.5D * g * t;
                double vZ = (1.0D + 0.08000000000000001D * t) * (arrow.getLocation().getZ() - l.getZ()) / t;

                Vector v = p.getVelocity();

                v.setX(vX);
                v.setY(vY);
                v.setZ(vZ);

                p.setVelocity(v);

                for (Entity n : temporaryEntities.get(p.getUniqueId())) {
                    if (n.isValid()) n.remove();
                }

                Slimefun.runSync(() -> {
                    jumpState.remove(p.getUniqueId());
                    temporaryEntities.remove(p.getUniqueId());
                }, 20L);
            }
        }
    }

    public boolean isJumping(UUID uuid) {
        return jumpState.containsKey(uuid);
    }

    public void addGrapplingHook(UUID uuid, Arrow arrow, Bat b, boolean state, long despawnTicks) {
        jumpState.put(uuid, state);
        invulnerable.add(uuid);
        temporaryEntities.put(uuid, new Entity[] { b, arrow });

        // To fix issue #253
        Slimefun.runSync(() -> {
            if (jumpState.containsKey(uuid)) {
                SlimefunPlugin.getBowListener().getBows().remove(uuid);

                for (Entity n : temporaryEntities.get(uuid)) {
                    if (n.isValid()) n.remove();
                }

                Slimefun.runSync(() -> {
                    invulnerable.remove(uuid);
                    jumpState.remove(uuid);
                    temporaryEntities.remove(uuid);
                }, 20L);
            }
        }, despawnTicks);
    }
}
