package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces;

import io.github.thebusybiscuit.slimefun4.core.attributes.ItemAttribute;

/**
 * This interface, when attached to a {@link SlimefunItem}, provides an easy method for adding 
 * a % chance to drop for an {@link SlimefunItem} on {@link entityDeathEvent}, this chance is 0-100
 * and used in conjunction with the MOB_DROP {@link RecipeType}. 
 * @see BasicCircuitBoard and @see MobDropListener.
 * 
 * @author dNiym
 *
 */
@FunctionalInterface
public interface RandomMobDrop extends ItemAttribute {
	

    /**
     * Implement this method to make the object have a variable chance of being
     * added to the dropList when {@link EntityType} (specified in the recipe) 
     * is killed by the {@link Player}
     * 
     * @return The integer chance (0-100%) {@link SlimefunItem} has to drop.
     */
    public int getDropChance();
    
}
