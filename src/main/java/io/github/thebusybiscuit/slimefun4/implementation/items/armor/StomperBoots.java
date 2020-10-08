package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The Boots of the Stomper are boots which damage nearby enemies whenever the {@link Player}
 * takes fall damage.
 * 
 * @author TheBusyBiscuit
 *
 */
public class StomperBoots extends SlimefunItem {

    public StomperBoots(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    /**
     * This will apply the "stomp" effect to the given {@link EntityDamageEvent}.
     * 
     * @param fallDamageEvent
     *            The {@link EntityDamageEvent} in which the {@link Player} has taken fall damage
     */
    public void stomp(EntityDamageEvent fallDamageEvent) {
        Player p = (Player) fallDamageEvent.getEntity();
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1F, 2F);
        p.setVelocity(new Vector(0, 0.7, 0));

        for (Entity n : p.getNearbyEntities(4, 4, 4)) {
            if (n instanceof LivingEntity && n.isValid() && !n.getUniqueId().equals(p.getUniqueId())) {
                Vector velocity = getShockwave(p.getLocation(), n.getLocation());
                n.setVelocity(velocity);

                // Check if it's not a Player or if PvP is enabled
                if (!(n instanceof Player) || (p.getWorld().getPVP() && SlimefunPlugin.getProtectionManager().hasPermission(p, n.getLocation(), ProtectableAction.PVP))) {
                    EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, n, DamageCause.ENTITY_ATTACK, fallDamageEvent.getDamage() / 2);
                    Bukkit.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        ((LivingEntity) n).damage(event.getDamage());
                    }
                }
            }
        }

        for (BlockFace face : BlockFace.values()) {
            Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(face);
            p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
        }
    }

    /**
     * This gives us the "shockwave" {@link Vector} for a given target.
     * 
     * @param origin
     *            The {@link Location} of our {@link Player}
     * @param target
     *            The {@link Location} of the {@link Entity} we are pushing away
     * 
     * @return A {@link Vector} to determine the velocity for our {@link Entity}
     */
    private Vector getShockwave(Location origin, Location target) {
        // As the distance approaches zero we might slip into a "division by zero" when normalizing
        if (origin.distanceSquared(target) < 0.05) {
            return new Vector(0, 1, 0);
        } else {
            Vector direction = target.toVector().subtract(origin.toVector());
            return direction.normalize().multiply(1.4);
        }
    }

}
