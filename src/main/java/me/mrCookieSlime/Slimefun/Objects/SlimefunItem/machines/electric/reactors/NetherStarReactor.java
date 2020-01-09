package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.reactors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AReactor;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.holograms.ReactorHologram;

public abstract class NetherStarReactor extends AReactor {
	
	public NetherStarReactor(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}

	@Override
	public String getInventoryTitle() {
		return "&fNether Star Reactor";
	}

	@Override
	public void registerDefaultRecipes() {
		registerFuel(new MachineFuel(1800, new ItemStack(Material.NETHER_STAR)));
	}

	@Override
	public void extraTick(final Location l) {
		Slimefun.runSync(() -> {
			for (Entity entity : ReactorHologram.getArmorStand(l, true).getNearbyEntities(5, 5, 5)) {
				if (entity instanceof LivingEntity) {
					((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1));
				}
			}
		}, 0L);
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
