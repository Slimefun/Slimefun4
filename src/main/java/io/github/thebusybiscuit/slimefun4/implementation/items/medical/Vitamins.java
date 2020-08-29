package io.github.thebusybiscuit.slimefun4.implementation.items.medical;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Vitamins extends SimpleSlimefunItem<ItemUseHandler> {

    private static final double HEALING_AMOUNT = 8.0;

    public Vitamins(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();

            if (p.getGameMode() != GameMode.CREATIVE) {
                ItemUtils.consumeItem(e.getItem(), false);
            }

            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);

            if (p.hasPotionEffect(PotionEffectType.POISON)) p.removePotionEffect(PotionEffectType.POISON);
            if (p.hasPotionEffect(PotionEffectType.WITHER)) p.removePotionEffect(PotionEffectType.WITHER);
            if (p.hasPotionEffect(PotionEffectType.SLOW)) p.removePotionEffect(PotionEffectType.SLOW);
            if (p.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
            if (p.hasPotionEffect(PotionEffectType.WEAKNESS)) p.removePotionEffect(PotionEffectType.WEAKNESS);
            if (p.hasPotionEffect(PotionEffectType.CONFUSION)) p.removePotionEffect(PotionEffectType.CONFUSION);
            if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) p.removePotionEffect(PotionEffectType.BLINDNESS);

            p.setFireTicks(0);

            double health = p.getHealth() + HEALING_AMOUNT;
            double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            p.setHealth(Math.min(health, maxHealth));

            e.cancel();
        };
    }

}
