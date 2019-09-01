package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class TelepositionScroll extends SimpleSlimefunItem<ItemInteractionHandler> {

	public TelepositionScroll(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}

	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, item) -> {
			if (SlimefunManager.isItemSimiliar(item, SlimefunItems.SCROLL_OF_DIMENSIONAL_TELEPOSITION, true)) {
				for (Entity n: p.getNearbyEntities(10.0, 10.0, 10.0)) {
					if (n instanceof LivingEntity && !(n instanceof ArmorStand) && !n.getUniqueId().equals(p.getUniqueId())) {
						float yaw = n.getLocation().getYaw() + 180.0F;
						if (yaw > 360.0F) yaw = yaw - 360.0F;
						
						n.teleport(new Location(n.getWorld(), n.getLocation().getX(), n.getLocation().getY(), n.getLocation().getZ(), yaw, n.getLocation().getPitch()));
					}
				}
				return true;
			}
			else return false;
		};
	}

}
