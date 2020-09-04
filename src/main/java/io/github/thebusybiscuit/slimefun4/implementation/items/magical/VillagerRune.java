package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.core.handlers.EntityInteractHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This {@link SlimefunItem} allows you to reset a {@link Villager} profession.
 * Useful to reset a villager who does not have desirable trades.
 *
 * @author dNiym
 *
 */
public class VillagerRune extends SimpleSlimefunItem<EntityInteractHandler> {

    public VillagerRune(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);
    }

    @Override
    public EntityInteractHandler getItemHandler() {
        return (e, item, offhand) -> {
            if (e.getRightClicked() instanceof Villager) {
                Villager v = (Villager) e.getRightClicked();

                if (v.getProfession() == Profession.NONE || v.getProfession() == Profession.NITWIT) {
                    return;
                }

                if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    ItemUtils.consumeItem(item, false);
                }

                // Reset Villager
                v.setVillagerExperience(0);
                v.setVillagerLevel(1);
                v.setProfession(Profession.NONE);
                e.setCancelled(true);

                double offset = ThreadLocalRandom.current().nextDouble(0.5);

                v.getWorld().playSound(v.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1.4F);
                v.getWorld().spawnParticle(Particle.CRIMSON_SPORE, v.getLocation(), 10, 0, offset / 2, 0, 0);
                v.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, v.getLocation(), 5, 0.04, 1, 0.04);
            }
        };
    }
}
