package io.github.thebusybiscuit.slimefun4.utils;

import java.util.function.IntFunction;

import javax.annotation.Nonnull;

import org.bukkit.enchantments.Enchantment;

import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;

/**
 * This a enum evaluating and indicating a {@link DamageableItem} 's chance to be damaged
 * depending if it is a tool or an armor
 *
 * @author RobotHanzo
 * 
 * @see DamageableItem
 */
public enum UnbreakingAlgorithm {

    /**
     * For armor sets, unbreaking is capped at max. 40% effectiveness.
     */
    ARMOR(lvl -> Math.random() >= 0.6 + (0.4 / (lvl + 1))),

    /**
     * For tools, unbreaking is calculated like this.
     * The effect increases indefinitely.
     */
    TOOLS(lvl -> Math.random() >= 1.0 / (lvl + 1));

    private final IntFunction<Boolean> function;

    UnbreakingAlgorithm(@Nonnull IntFunction<Boolean> function) {
        this.function = function;
    }

    /**
     * This method will randomly decide if the item should be damaged or not
     * based on the internal formula of this {@link UnbreakingAlgorithm}.
     * If this method returns true, the item should not take damage.
     * 
     * @param unbreakingLevel
     *            The {@link Integer} level of the unbreaking {@link Enchantment}
     * 
     * @return Whether to save the item from taking damage
     *
     */
    public boolean evaluate(int unbreakingLevel) {
        if (unbreakingLevel > 0) {
            return function.apply(unbreakingLevel);
        } else {
            return false;
        }
    }
}
