package io.github.thebusybiscuit.slimefun4.core.attributes;

/**
 * This interface, when attached to a {@link SlimefunItem}, provides an easy method for adding
 * a % chance to drop for an {@link SlimefunItem} on {@link entityDeathEvent} or {@link EntityItemDropEvent}, this chance is 0-100
 * and used in conjunction with the MOB_DROP {@link RecipeType} or the BARTER_DROP {@link RecipeType}.
 * 
 * @author dNiym
 *
 * @see BasicCircuitBoard
 * @see MobDropListener
 * @see PiglinBarterListener
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
    public int getMobDropChance();

}
