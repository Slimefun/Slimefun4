package io.github.thebusybiscuit.slimefun4.implementation.items.weapons;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun4.core.handlers.BowShootHandler;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedParticle;

/**
 * The {@link ExplosiveBow} is a {@link SlimefunBow} which creates a fake explosion when it hits
 * a {@link LivingEntity}. Any nearby {@link LivingEntity LivingEntities} get pushed away and
 * take a little bit of damage, similar to an "Area of Effect" damage.
 * 
 * @author TheBusyBiscuit
 * @author Linox
 * 
 * @see SlimefunBow
 *
 */
public class ExplosiveBow extends SlimefunBow {

    private final ItemSetting<Integer> range = new IntRangeSetting(this, "explosion-range", 1, 3, Integer.MAX_VALUE);

    @ParametersAreNonnullByDefault
    public ExplosiveBow(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe) {
        super(itemGroup, item, recipe);

        addItemSetting(range);
    }

    @Nonnull
    @Override
    public BowShootHandler onShoot() {
        return (e, target) -> {
            target.getWorld().spawnParticle(VersionedParticle.EXPLOSION, target.getLocation(), 1);
            SoundEffect.EXPLOSIVE_BOW_HIT_SOUND.playAt(target.getLocation(), SoundCategory.PLAYERS);
            int radius = range.getValue();

            Collection<Entity> entities = target.getWorld().getNearbyEntities(target.getLocation(), radius, radius, radius, this::canDamage);
            for (Entity nearby : entities) {
                LivingEntity entity = (LivingEntity) nearby;

                Vector distanceVector = entity.getLocation().toVector().subtract(target.getLocation().toVector()).add(new Vector(0, 0.75, 0));

                double distanceSquared = distanceVector.lengthSquared();
                double damage = e.getDamage() * (1 - (distanceSquared / (2 * range.getValue() * range.getValue())));

                if (!entity.getUniqueId().equals(target.getUniqueId())) {
                    EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(e.getDamager(), entity, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, damage);
                    Bukkit.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        distanceVector.setY(0.75);
                        Vector knockback = distanceVector.normalize().multiply(2);
                        entity.setVelocity(entity.getVelocity().add(knockback));
                        entity.damage(event.getDamage());
                    }
                }
            }
        };
    }

    private boolean canDamage(@Nonnull Entity entity) {
        return entity instanceof LivingEntity && !(entity instanceof ArmorStand) && entity.isValid();
    }
}
