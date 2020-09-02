package io.github.thebusybiscuit.slimefun4.implementation.items.medical;

import javax.annotation.Nonnull;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
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
     * This method clears any negative {@link PotionEffect} from the given {@link Player}.
     * 
     * @param p
     *            The {@link Player}
     */
    public void clearNegativeEffects(@Nonnull Player p) {
        if (p.hasPotionEffect(PotionEffectType.POISON)) p.removePotionEffect(PotionEffectType.POISON);
        if (p.hasPotionEffect(PotionEffectType.WITHER)) p.removePotionEffect(PotionEffectType.WITHER);
        if (p.hasPotionEffect(PotionEffectType.SLOW)) p.removePotionEffect(PotionEffectType.SLOW);
        if (p.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        if (p.hasPotionEffect(PotionEffectType.WEAKNESS)) p.removePotionEffect(PotionEffectType.WEAKNESS);
        if (p.hasPotionEffect(PotionEffectType.CONFUSION)) p.removePotionEffect(PotionEffectType.CONFUSION);
        if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) p.removePotionEffect(PotionEffectType.BLINDNESS);
        if (p.hasPotionEffect(PotionEffectType.BAD_OMEN)) p.removePotionEffect(PotionEffectType.BLINDNESS);
    }

    public void heal(@Nonnull Player p) {
        double health = p.getHealth() + healAmount;
        double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        p.setHealth(Math.min(health, maxHealth));
    }

}
