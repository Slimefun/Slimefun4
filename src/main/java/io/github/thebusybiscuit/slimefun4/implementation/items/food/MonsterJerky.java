package io.github.thebusybiscuit.slimefun4.implementation.items.food;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun4.core.handlers.ItemConsumptionHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * {@link MonsterJerky} is basically just Rotten Flesh but without the Hunger Effect.
 * 
 * @author TheBusyBiscuit
 * 
 * @see MeatJerky
 *
 */
public class MonsterJerky extends SimpleSlimefunItem<ItemConsumptionHandler> {

    public MonsterJerky(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemConsumptionHandler getItemHandler() {
        return (e, p, item) -> SlimefunPlugin.runSync(() -> {
            if (p.hasPotionEffect(PotionEffectType.HUNGER)) {
                p.removePotionEffect(PotionEffectType.HUNGER);
            }

            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 5, 0));
        }, 1L);
    }

}
