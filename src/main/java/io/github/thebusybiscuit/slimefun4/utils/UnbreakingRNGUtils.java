package io.github.thebusybiscuit.slimefun4.utils;


import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;

/**
 * This class is created for a RNG generator to determine if the tool / armor should be damaged when unbreaking enchant is applied.<br>
 * Mainly used in {@link DamageableItem} and their implementations
 *
 * @author RobotHanzo
 *
 */
public class UnbreakingRNGUtils {

    public UnbreakingRNGUtils(){}

    public boolean getToolsRNG(int unbreakingLevel){
        return !(Math.random() < (1.0 / (unbreakingLevel + 1)));
    }

    public boolean getArmorRNG(int unbreakingLevel){
        return !(Math.random() < 0.6 + (0.4 / (unbreakingLevel + 1)));
    }

}
