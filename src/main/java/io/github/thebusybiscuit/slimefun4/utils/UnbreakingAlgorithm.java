package io.github.thebusybiscuit.slimefun4.utils;


import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;

/**
 * This a enum evaluating and indicating a {@link DamageableItem} 's chance to be damaged
 * depending if it is a tool or an armor
 *
 * @author RobotHanzo
 */
public enum UnbreakingAlgorithm {
    TOOLS,
    ARMOR;
    public boolean evaluate(int unbreakingLevel){
        if (this == TOOLS) {
            return !(Math.random() < (1.0 / (unbreakingLevel + 1)));
        }

        if (this == ARMOR) {
            return !(Math.random() < 0.6 + (0.4 / (unbreakingLevel + 1)));
        }

        return false;
    }
}
