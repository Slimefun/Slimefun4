package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.core.handlers.EntityInteractHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This {@link SlimefunItem} allows you to reset a {@link Villager} profession. 
 * Useful to reset a villager who does not have desirable trades.
 *
 * @author dNiym
 *
 * @see ItemHandler
 *
 */
public class RuneOfUnemployment extends SimpleSlimefunItem<EntityInteractHandler> {

    public RuneOfUnemployment(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public EntityInteractHandler getItemHandler() {
        return (p, entity, item, offhand) -> {
            if (entity instanceof Villager) {
                Villager v = (Villager) entity;
                
                if(v.getProfession() == Profession.NONE || v.getProfession() == Profession.NITWIT)
                    return;
                
                if (p.getGameMode() != GameMode.CREATIVE) 
                    ItemUtils.consumeItem(item, false);
                
                v.setVillagerExperience(0);
                v.setProfession(Profession.NONE);
                
                Double offset = ThreadLocalRandom.current().nextDouble(0, 0.5);
                
                double o2 = offset / 2;
                
                v.getWorld().spawnParticle(Particle.CRIMSON_SPORE, v.getLocation(), 10, 0, o2, 0,0.0d);
                v.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, v.getLocation(), 5, 0.04d, 1d, 0.04d);    

            }
        };
    }
}
