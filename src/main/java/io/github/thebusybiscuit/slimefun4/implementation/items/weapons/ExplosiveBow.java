package io.github.thebusybiscuit.slimefun4.implementation.items.weapons;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.handlers.BowShootHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

import java.util.Collection;

public class ExplosiveBow extends SlimefunBow {

    private final ItemSetting<Integer> range = new ItemSetting<>("explosion-range", 3);

    public ExplosiveBow(Category category, SlimefunItemStack item, ItemStack[] recipe) {
        super(category, item, recipe);
        addItemSetting(range);
    }

    @Override
    public BowShootHandler onShoot() {
        return (e, n) -> {
            Collection<Entity> entites = n.getWorld().getNearbyEntities(n.getLocation(), range.getValue(), range.getValue(), range.getValue(), entity -> entity instanceof LivingEntity);
            for (Entity entity : entites) {
                if (entity.isValid() && entity instanceof LivingEntity) {
                    LivingEntity entityL = (LivingEntity) entity;

                    double distance = entityL.getLocation().distance(n.getLocation());
                    double damage = calculateDamage(distance, e.getDamage());

                    Vector distanceVector = entityL.getLocation().toVector().subtract(n.getLocation().toVector());
                    distanceVector.setY(distanceVector.getY() + 0.6D);
                    Vector entityVelocity = entityL.getVelocity();
                    Vector knockback = entityVelocity.add(distanceVector.normalize().multiply((int) (e.getDamage() / Math.round(damage))));
                    entityL.setVelocity(knockback);

                    entityL.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, entityL.getLocation(), 1);
                    entityL.getWorld().playSound(entityL.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);

                    if (!entityL.getUniqueId().equals(n.getUniqueId())) {
                        entityL.damage(damage);
                        EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(e.getDamager(), entityL, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, damage);
                        Bukkit.getPluginManager().callEvent(damageEvent);
                    }
                }
            }
        };
    }

    private double calculateDamage(double distance, double ogDamage) {

        if (distance == 0D) return ogDamage;
        double damage = ogDamage - Math.pow((distance / range.getValue()), 2.5);
        if (Math.round(damage) == 0D) damage += 1D;
        return Math.min(damage, ogDamage);
    }

}
