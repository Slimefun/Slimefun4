package io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.holograms.ReactorHologram;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link NetherStarReactor} is an implementation of {@link Reactor} that consumes
 * Nether Stars and adds Withering to any nearby {@link LivingEntity}
 * 
 * @author John000708
 * 
 * @see NuclearReactor
 *
 */
public abstract class NetherStarReactor extends Reactor {

    @ParametersAreNonnullByDefault
    public NetherStarReactor(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultFuelTypes() {
        registerFuel(new MachineFuel(1800, new ItemStack(Material.NETHER_STAR)));
    }

    @Override
    public void extraTick(@Nonnull Location l) {
        SlimefunPlugin.runSync(() -> {
            ArmorStand hologram = ReactorHologram.getArmorStand(l, true);
            for (Entity entity : hologram.getNearbyEntities(5, 5, 5)) {
                if (entity instanceof LivingEntity && entity.isValid()) {
                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1));
                }
            }
        });
    }

    @Override
    public ItemStack getCoolant() {
        return SlimefunItems.NETHER_ICE_COOLANT_CELL;
    }

    @Override
    public ItemStack getFuelIcon() {
        return new ItemStack(Material.NETHER_STAR);
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.NETHER_STAR);
    }

}
