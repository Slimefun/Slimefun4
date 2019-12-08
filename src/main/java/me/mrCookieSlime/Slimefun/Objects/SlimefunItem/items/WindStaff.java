package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class WindStaff extends SimpleSlimefunItem<ItemInteractionHandler> {
	
	public WindStaff(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}
	
	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, item) -> {
			if (isItem(item)) {
				if (p.getFoodLevel() >= 2) {
					if (p.getInventory().getItemInMainHand().getType() != Material.SHEARS && p.getGameMode() != GameMode.CREATIVE) {
						FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, p.getFoodLevel() - 2);
						Bukkit.getPluginManager().callEvent(event);
						p.setFoodLevel(event.getFoodLevel());
					}
					p.setVelocity(p.getEyeLocation().getDirection().multiply(4));
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_TNT_PRIMED, 1, 1);
					p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 1);
					p.setFallDistance(0F);
				}
				else {
					SlimefunPlugin.getLocal().sendMessage(p, "messages.hungry", true);
				}
				return true;
			}
			else return false;
		};
	}

}
