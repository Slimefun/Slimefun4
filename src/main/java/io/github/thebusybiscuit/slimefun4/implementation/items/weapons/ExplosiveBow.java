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

    private final ItemSetting<Integer> range = new ItemSetting<Integer>("explosion-range", 3) {
        @Override
        public boolean validateInput(Integer input) {
            return input != null && input > 0;
        }
    };

    public ExplosiveBow(Category category, SlimefunItemStack item, ItemStack[] recipe) {
        super(category, item, recipe);
        addItemSetting(range);
    }

    @Override
    public BowShootHandler onShoot() {
        return (e, target) -> {
            target.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, target.getLocation(), 1);
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);

            Collection<Entity> entites = target.getWorld().getNearbyEntities(target.getLocation(), range.getValue(), range.getValue(), range.getValue(), entity -> entity instanceof LivingEntity && entity.isValid());
            for (Entity entity : entites) {
                LivingEntity entityL = (LivingEntity) entity;

                Vector distanceVector = entityL.getLocation().toVector().subtract(target.getLocation().toVector());
                distanceVector.setY(distanceVector.getY() + 0.6D);
                Vector entityVelocity = entityL.getVelocity();

                double distanceSquared = distanceVector.lengthSquared();
                double damage = calculateDamage(distanceSquared, e.getDamage());

                Vector knockback = entityVelocity.add(distanceVector.normalize().multiply((int) (e.getDamage() / damage)));
                entityL.setVelocity(knockback);

                if (!entityL.getUniqueId().equals(target.getUniqueId())) {
                    EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(e.getDamager(), entityL, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, damage);
                    Bukkit.getPluginManager().callEvent(damageEvent);
                    if (!damageEvent.isCancelled()) {
                        entityL.damage(damageEvent.getDamage());
                    }
                }
            }
        };
    }

    private double calculateDamage(double distanceSquared, double originalDamage) {
        if (distanceSquared <= 0.05D) return originalDamage;
        double damage = originalDamage * (1 - (distanceSquared / (range.getValue() * range.getValue())));
        return Math.min(Math.max(1, damage), originalDamage);
    }

}
