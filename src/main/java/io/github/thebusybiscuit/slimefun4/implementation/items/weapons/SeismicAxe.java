package io.github.thebusybiscuit.slimefun4.implementation.items.weapons;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link SeismicAxe} is an interesting weapon. It spawns ghostly block entities in a straight line
 * when right-clicked. These blocks launch up from the ground and damage any {@link LivingEntity} in its way.
 * It is quite similar to a shockwave.
 * 
 * @author TheBusyBiscuit
 *
 */
public class SeismicAxe extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable, DamageableItem {

    private static final float STRENGTH = 1.2F;
    private static final float DAMAGE = 6;
    private static final int RANGE = 10;

    public SeismicAxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            List<Block> blocks = p.getLineOfSight(null, RANGE);

            for (int i = 2; i < blocks.size(); i++) {
                Block ground = findGround(blocks.get(i));
                Location groundLocation = ground.getLocation();

                ground.getWorld().playEffect(groundLocation, Effect.STEP_SOUND, ground.getType());

                if (ground.getRelative(BlockFace.UP).getType() == Material.AIR) {
                    Location loc = ground.getRelative(BlockFace.UP).getLocation().add(0.5, 0.0, 0.5);
                    FallingBlock block = ground.getWorld().spawnFallingBlock(loc, ground.getBlockData());
                    block.setDropItem(false);
                    block.setVelocity(new Vector(0, 0.4 + i * 0.01, 0));
                    block.setMetadata("seismic_axe", new FixedMetadataValue(SlimefunPlugin.instance(), "fake_block"));
                }

                for (Entity n : ground.getChunk().getEntities()) {
                    if (n instanceof LivingEntity && n.getType() != EntityType.ARMOR_STAND && n.getLocation().distance(groundLocation) <= 2.0D && !n.getUniqueId().equals(p.getUniqueId())) {
                        pushEntity(p, n);
                    }
                }
            }

            for (int i = 0; i < 4; i++) {
                damageItem(p, e.getItem());
            }
        };
    }

    private void pushEntity(Player p, Entity entity) {
        if (entity.getType() != EntityType.PLAYER || p.getWorld().getPVP()) {
            EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, entity, DamageCause.ENTITY_ATTACK, DAMAGE);
            Bukkit.getPluginManager().callEvent(event);

            // Fixes #2207 - Only apply Vector if the Player is able to damage the entity
            if (!event.isCancelled()) {
                Vector vector = entity.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                vector.multiply(STRENGTH);
                vector.setY(0.9);
                entity.setVelocity(vector);

                ((LivingEntity) entity).damage(event.getDamage());
            }
        }
    }

    private Block findGround(Block b) {
        if (b.getType() == Material.AIR) {
            for (int y = 0; y < b.getY(); y++) {
                Block block = b.getRelative(0, -y, 0);

                if (block.getType() != Material.AIR) {
                    return block;
                }
            }
        }

        return b;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

}
