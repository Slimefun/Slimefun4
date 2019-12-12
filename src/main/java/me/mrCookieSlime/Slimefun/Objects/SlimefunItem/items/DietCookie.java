package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemConsumptionHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class DietCookie extends SimpleSlimefunItem<ItemConsumptionHandler> {

	public DietCookie(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}

	@Override
	public ItemConsumptionHandler getItemHandler() {
		return (e, p, item) -> {
			if (isItem(item)) {
				p.sendMessage(ChatColor.YELLOW + "You feel so light...");
				p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);

				if (p.hasPotionEffect(PotionEffectType.LEVITATION)) p.removePotionEffect(PotionEffectType.LEVITATION);
				p.addPotionEffect(PotionEffectType.LEVITATION.createEffect(60, 1));
				
				return true;
			}
			return false;
		};
	}

}
