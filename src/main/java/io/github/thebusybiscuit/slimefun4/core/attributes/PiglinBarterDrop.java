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
 * a % chance to drop for an {@link SlimefunItem} on {@link EntityItemDropEvent}, this chance is 0-100
 * and used in conjunction with the BARTER_DROP {@link RecipeType}.
 * 
 * @author dNiym
 *
 * @see PiglinListener
 * 
 */
@FunctionalInterface
public interface PiglinBarterDrop extends ItemAttribute {

    /**
     * Implement this method to make the object have a variable chance of being
     * dropped by Piglins when bartering with them. This interface should be used
     * with a {@link SlimefunItem} item that has the BARTER_DROP {@link RecipeType}.
     * 
     * It is recommended that this chance is kept reasonably low to feel like
     * a vanilla drop as a 100% chance will completely override all Piglin 
     * barter drops.  (NOTE: this feature only exists in 1.16+)
     * 
     * @return The integer chance (0-100%) {@link SlimefunItem} has to drop.
     */
    int getBarteringLootChance() ;

}
