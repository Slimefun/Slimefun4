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
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SeismicAxe extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable, DamageableItem {

    public SeismicAxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            List<Block> blocks = p.getLineOfSight(null, 10);

            for (int i = 0; i < blocks.size(); i++) {
                Block b = blocks.get(i);
                Location ground = b.getLocation();

                if (b.getType() == null || b.getType() == Material.AIR) {
                    for (int y = ground.getBlockY(); y > 0; y--) {
                        if (b.getWorld().getBlockAt(b.getX(), y, b.getZ()) != null && b.getWorld().getBlockAt(b.getX(), y, b.getZ()).getType() != null && b.getWorld().getBlockAt(b.getX(), y, b.getZ()).getType() != Material.AIR) {
                            ground = new Location(b.getWorld(), b.getX(), y, b.getZ());
                            break;
                        }
                    }
                }

                b.getWorld().playEffect(ground, Effect.STEP_SOUND, ground.getBlock().getType());

                if (ground.getBlock().getRelative(BlockFace.UP).getType() == null || ground.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
                    Location loc = ground.getBlock().getRelative(BlockFace.UP).getLocation().add(0.5, 0.0, 0.5);
                    FallingBlock block = ground.getWorld().spawnFallingBlock(loc, ground.getBlock().getBlockData());
                    block.setDropItem(false);
                    block.setVelocity(new Vector(0, 0.4 + i * 0.01, 0));
                    block.setMetadata("seismic_axe", new FixedMetadataValue(SlimefunPlugin.instance, "fake_block"));
                }

                for (Entity n : ground.getChunk().getEntities()) {
                    if (n instanceof LivingEntity && n.getType() != EntityType.ARMOR_STAND && n.getLocation().distance(ground) <= 2.0D && !n.getUniqueId().equals(p.getUniqueId())) {
                        Vector vector = n.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(1.4);
                        vector.setY(0.9);
                        n.setVelocity(vector);

                        if (n.getType() != EntityType.PLAYER || p.getWorld().getPVP()) {
                            EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, n, DamageCause.ENTITY_ATTACK, 6D);
                            Bukkit.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                ((LivingEntity) n).damage(6D);
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < 4; i++) {
                damageItem(p, e.getItem());
            }
        };
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

}
