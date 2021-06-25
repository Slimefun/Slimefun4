package io.github.thebusybiscuit.slimefun4.utils.itemstack;


import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;

public enum RadiationSymptom {
    /**
     * An enum of potential radiation symptoms.
     */
    SLOW(10, PotionEffectType.SLOW, 3),
    WITHER_LOW(25, PotionEffectType.WITHER, 0),
    BLINDNESS(50, PotionEffectType.BLINDNESS, 4),
    WITHER_HIGH(75, PotionEffectType.WITHER, 3),
    IMMINENT_DEATH(100, PotionEffectType.HARM, 49);

    private final int minExposure;
    private final PotionEffect potionEffect;

    RadiationSymptom(int minExposure, @Nonnull PotionEffectType type, int level) {
        this.minExposure = minExposure;
        this.potionEffect = new PotionEffect(type, SlimefunPlugin.getCfg().getOrSetDefault("options.radiation-update-interval", 1) * 20 + 20, level);
    }

    /**
     * This method applies the symptom to a player.
     *
     * @param p The player
     */

    public void apply(@Nonnull Player p){
        SlimefunPlugin.runSync(() -> p.addPotionEffect(potionEffect));
    }

    /**
     * This method returns if this symptom
     * should be applied.
     *
     * @param exposure Exposure level
     *
     * @return If the symptom should be applied
     */
    public boolean shouldApply(int exposure){
        return exposure >= minExposure;
    }
}