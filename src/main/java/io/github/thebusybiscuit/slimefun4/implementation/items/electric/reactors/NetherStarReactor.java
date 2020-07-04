package io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.holograms.ReactorHologram;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * The {@link NetherStarReactor} is an implementation of {@link Reactor} that consumes
 * Nether Stars and adds Withering to any nearby {@link LivingEntity}
 *
 * @author John000708
 * @see NuclearReactor
 */
public abstract class NetherStarReactor extends Reactor {

    public NetherStarReactor(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public String getInventoryTitle() {
        return SlimefunItems.NETHER_STAR_REACTOR.getItemMeta().getDisplayName();
    }

    @Override
    protected void registerDefaultFuelTypes() {
        registerFuel(new MachineFuel(1800, new ItemStack(Material.NETHER_STAR)));
    }

    @Override
    public void extraTick(Location l) {
        Slimefun.runSync(() -> {
            for (Entity entity : ReactorHologram.getArmorStand(l, true).getNearbyEntities(5, 5, 5)) {
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1));
                }
            }
        }, 0L);
    }

    @Override
    public ItemStack getFuelIcon() {
        return new ItemStack(Material.NETHER_STAR);
    }

    @Override
    public ItemStack getCoolant() {
        return SlimefunItems.NETHER_ICE_COOLANT_CELL;
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.NETHER_STAR);
    }

}
