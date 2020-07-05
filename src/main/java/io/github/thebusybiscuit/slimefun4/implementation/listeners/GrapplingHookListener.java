package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GrapplingHook;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This {@link Listener} is responsible for the mechanics behind the {@link GrapplingHook}.
 * 
 * @author TheBusyBiscuit
 * @author Linox
 * @author BlackBeltPanda
 * 
 * @see GrapplingHook
 *
 */
public class GrapplingHookListener implements Listener {

    private GrapplingHook grapplingHook;

    private final Map<UUID, GrapplingHookEntity> activeHooks = new HashMap<>();
    private final Set<UUID> invulnerability = new HashSet<>();

    public void register(SlimefunPlugin plugin, GrapplingHook grapplingHook) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.grapplingHook = grapplingHook;
    }

    private boolean isEnabled() {
        return grapplingHook != null && !grapplingHook.isDisabled();
    }

    @EventHandler
    public void onArrowHitEntity(EntityDamageByEntityEvent e) {
        if (!isEnabled()) {
            return;
        }

        if (e.getDamager() instanceof Arrow) {
            handleGrapplingHook((Arrow) e.getDamager());
        }
    }

    @EventHandler
    public void onArrowHitSurface(ProjectileHitEvent e) {
        if (!isEnabled()) {
            return;
        }

        Slimefun.runSync(() -> {
            if (e.getEntity() instanceof Arrow) {
                handleGrapplingHook((Arrow) e.getEntity());
            }
        }, 2L);
    }

    @EventHandler
    public void onArrowHitHanging(HangingBreakByEntityEvent e) {
        if (!isEnabled()) {
            return;
        }

        // This is called when the arrow shoots off a painting or an item frame
        if (e.getRemover() instanceof Arrow) {
            handleGrapplingHook((Arrow) e.getRemover());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (!isEnabled()) {
            return;
        }

        UUID uuid = e.getPlayer().getUniqueId();
        activeHooks.remove(uuid);
        invulnerability.remove(uuid);
    }

    @EventHandler
    public void onLeave(PlayerKickEvent e) {
        if (!isEnabled()) {
            return;
        }

        UUID uuid = e.getPlayer().getUniqueId();
        activeHooks.remove(uuid);
        invulnerability.remove(uuid);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (!isEnabled()) {
            return;
        }

        if (e.getEntity() instanceof Player && e.getCause() == DamageCause.FALL && invulnerability.remove(e.getEntity().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPortalEnter(EntityPortalEnterEvent e) {
        if (!isEnabled()) {
            return;
        }

        if (e.getEntity() instanceof Arrow) {
            handleGrapplingHook((Arrow) e.getEntity());
        }
    }

    private void handleGrapplingHook(Arrow arrow) {
        if (arrow != null && arrow.isValid() && arrow.getShooter() instanceof Player) {
            Player p = (Player) arrow.getShooter();
            GrapplingHookEntity hook = activeHooks.get(p.getUniqueId());

            if (hook != null) {
                Location target = arrow.getLocation();
                hook.drop(target);

                Vector velocity = new Vector(0.0, 0.2, 0.0);

                if (p.getLocation().distance(target) < 3.0) {
                    if (target.getY() <= p.getLocation().getY()) {
                        velocity = target.toVector().subtract(p.getLocation().toVector());
                    }
                }
                else {
                    Location l = p.getLocation();
                    l.setY(l.getY() + 0.5);
                    p.teleport(l);

                    double g = -0.08;
                    double d = target.distance(l);
                    double t = d;
                    double vX = (1.0 + 0.08 * t) * (target.getX() - l.getX()) / t;
                    double vY = (1.0 + 0.04 * t) * (target.getY() - l.getY()) / t - 0.5D * g * t;
                    double vZ = (1.0 + 0.08 * t) * (target.getZ() - l.getZ()) / t;

                    velocity = p.getVelocity();
                    velocity.setX(vX);
                    velocity.setY(vY);
                    velocity.setZ(vZ);
                }

                p.setVelocity(velocity);

                hook.remove();
                Slimefun.runSync(() -> activeHooks.remove(p.getUniqueId()), 20L);
            }
        }
    }

    public boolean isGrappling(UUID uuid) {
        return activeHooks.containsKey(uuid);
    }

    public void addGrapplingHook(Player p, Arrow arrow, Bat bat, boolean dropItem, long despawnTicks) {
        GrapplingHookEntity hook = new GrapplingHookEntity(p, arrow, bat, dropItem);
        UUID uuid = p.getUniqueId();

        activeHooks.put(uuid, hook);

        // To fix issue #253
        Slimefun.runSync(() -> {
            GrapplingHookEntity entity = activeHooks.get(uuid);

            if (entity != null) {
                SlimefunPlugin.getBowListener().getProjectileData().remove(uuid);
                entity.remove();

                Slimefun.runSync(() -> {
                    activeHooks.remove(uuid);
                    invulnerability.remove(uuid);
                }, 20L);
            }
        }, despawnTicks);
    }
}
