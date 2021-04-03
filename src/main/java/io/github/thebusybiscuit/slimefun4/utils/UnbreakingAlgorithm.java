package io.github.thebusybiscuit.slimefun4.utils;


import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;

import javax.annotation.Nonnull;
import java.util.function.IntFunction;

/**
 * This a enum evaluating and indicating a {@link DamageableItem} 's chance to be damaged
 * depending if it is a tool or an armor
 *
 * @author RobotHanzo
 */
public enum UnbreakingAlgorithm {

    ARMOR(lvl -> !(Math.random() < 0.6 + (0.4 / (lvl + 1)))),
    TOOLS(lvl -> !(Math.random() < (1.0 / (lvl + 1))));

    private final IntFunction<Boolean> function;
    
    UnbreakingAlgorithm(@Nonnull IntFunction<Boolean> function) {
        this.function = function;
    }

    public boolean evaluate(int unbreakingLevel) {
        return function.apply(unbreakingLevel);
    }
}
