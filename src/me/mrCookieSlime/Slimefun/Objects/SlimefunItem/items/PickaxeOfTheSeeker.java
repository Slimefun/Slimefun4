package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.DamageableItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class PickaxeOfTheSeeker extends SimpleSlimefunItem<ItemInteractionHandler> implements DamageableItem {

	public PickaxeOfTheSeeker(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}

	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, item) -> {
			if (SlimefunManager.isItemSimiliar(item, getItem(), true)) {
				Block closest = null;

				for (int x = -4; x <= 4; x++) {
					for (int y = -4; y <= 4; y++) {
						for (int z = -4; z <= 4; z++) {
							if (p.getLocation().getBlock().getRelative(x, y, z).getType().toString().endsWith("_ORE") && closest == null || p.getLocation().distance(closest.getLocation()) < p.getLocation().distance(p.getLocation().getBlock().getRelative(x, y, z).getLocation())) {
								closest = p.getLocation().getBlock().getRelative(x, y, z);
							}
						}
					}
				}

				if (closest == null) {
					SlimefunPlugin.getLocal().sendMessage(p, "miner.no-ores", true);
				}
				else {
					double l = closest.getX() + 0.5 - p.getLocation().getX();
					double w = closest.getZ() + 0.5 - p.getLocation().getZ();
					float yaw;
					float pitch;
					double c = Math.sqrt(l * l + w * w);
					double alpha1 = -Math.asin(l / c) / Math.PI * 180;
					double alpha2 =  Math.acos(w / c) / Math.PI * 180;
					if (alpha2 > 90) yaw = (float) (180 - alpha1);
					else yaw = (float) alpha1;
					pitch = (float) ((-Math.atan((closest.getY() - 0.5 - p.getLocation().getY()) / Math.sqrt(l * l + w * w))) * 180F / Math.PI);

					p.teleport(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), yaw, pitch));
				}

				damageItem(p, item);
				return true;
			}
			else return false;
		};
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

}
