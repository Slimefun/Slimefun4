package io.github.thebusybiscuit.slimefun4.core.attributes;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;

import io.github.thebusybiscuit.slimefun4.implementation.items.misc.BasicCircuitBoard;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.MobDropListener;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This interface, when attached to a {@link SlimefunItem}, provides an easy method for adding
 * a % chance to drop for an {@link SlimefunItem} on {@link entityDeathEvent} or {@link EntityItemDropEvent}, this chance is 0-100
 * and used in conjunction with the MOB_DROP {@link RecipeType} or the BARTER_DROP {@link RecipeType}.
 * 
 * @author dNiym
 *
 * @see BasicCircuitBoard
 * @see MobDropListener
 * 
 */
@FunctionalInterface
public interface RandomMobDrop extends ItemAttribute {

    /**
     * Implement this method to make the object have a variable chance of being
     * added to the dropList when {@link EntityType} (specified in the recipe)
     * is killed by the {@link Player} for a MOB_DROP {@link RecipeType}.
     * 
     * Alternatively if the {@link RecipeType} is set to BARTER_DROP the item
     * will then have its % chance to be dropped only by bartering with the
     * Piglin creatures.
     * 
     * It is recommended that this chance is kept reasonably low to feel like
     * a vanilla drop as a 100% chance will completely override all piglin 
     * barter drops.  (NOTE: this feature only exists in 1.16+)
     * 
     * @return The integer chance (0-100%) {@link SlimefunItem} has to drop.
     */
    int getMobDropChance();

}
