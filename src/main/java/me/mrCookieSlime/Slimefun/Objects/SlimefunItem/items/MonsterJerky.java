package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemConsumptionHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class MonsterJerky extends SimpleSlimefunItem<ItemConsumptionHandler> {

	public MonsterJerky(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}

	@Override
	public ItemConsumptionHandler getItemHandler() {
		return (e, p, item) -> {
			if (isItem(item)) {
				Slimefun.runSync(() -> {
					if (p.hasPotionEffect(PotionEffectType.HUNGER)) {
						p.removePotionEffect(PotionEffectType.HUNGER);
					}
					
					p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 5, 0));
				}, 1L);
				return true;
			}
			else {
				return false;
			}
		};
	}

}
