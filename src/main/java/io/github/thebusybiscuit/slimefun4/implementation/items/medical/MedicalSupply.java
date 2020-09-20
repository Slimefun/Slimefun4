package io.github.thebusybiscuit.slimefun4.implementation.items.medical;

import javax.annotation.Nonnull;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public abstract class MedicalSupply<T extends ItemHandler> extends SimpleSlimefunItem<T> {

    private final int healAmount;

    public MedicalSupply(Category category, int healAmount, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        this.healAmount = healAmount;
    }

    /**
     * This method clears any negative {@link PotionEffect} from the given {@link LivingEntity}.
     * 
     * @param n
     *            The {@link LivingEntity} to clear the effects from.
     */
    public void clearNegativeEffects(@Nonnull LivingEntity n) {
        if (n.hasPotionEffect(PotionEffectType.POISON)) n.removePotionEffect(PotionEffectType.POISON);
        if (n.hasPotionEffect(PotionEffectType.WITHER)) n.removePotionEffect(PotionEffectType.WITHER);
        if (n.hasPotionEffect(PotionEffectType.SLOW)) n.removePotionEffect(PotionEffectType.SLOW);
        if (n.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) n.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        if (n.hasPotionEffect(PotionEffectType.WEAKNESS)) n.removePotionEffect(PotionEffectType.WEAKNESS);
        if (n.hasPotionEffect(PotionEffectType.CONFUSION)) n.removePotionEffect(PotionEffectType.CONFUSION);
        if (n.hasPotionEffect(PotionEffectType.BLINDNESS)) n.removePotionEffect(PotionEffectType.BLINDNESS);
        if (n.hasPotionEffect(PotionEffectType.BAD_OMEN)) n.removePotionEffect(PotionEffectType.BAD_OMEN);
    }

    /**
     * This method heals the given {@link LivingEntity} by the amount provided via the constructor.
     * 
     * @param n
     *            The {@link LivingEntity} to heal
     */
    public void heal(@Nonnull LivingEntity n) {
        double health = n.getHealth() + healAmount;
        double maxHealth = n.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        n.setHealth(Math.min(health, maxHealth));
    }

}
