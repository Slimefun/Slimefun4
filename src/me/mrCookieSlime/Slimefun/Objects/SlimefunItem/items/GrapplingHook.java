package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.utils.Utilities;

public class GrapplingHook extends SimpleSlimefunItem<ItemInteractionHandler> {

	public GrapplingHook(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}

	@Override
	public ItemInteractionHandler getItemHandler() {
		Utilities utilities = SlimefunPlugin.getUtilities();
		
		return (e, p, item) -> {
			if (SlimefunManager.isItemSimiliar(item, SlimefunItems.GRAPPLING_HOOK, true)) {
				if (e.getClickedBlock() == null && !utilities.jumpState.containsKey(p.getUniqueId())) {
					e.setCancelled(true);
					if (p.getInventory().getItemInOffHand().getType() == Material.BOW) {
						// Cancel, to fix dupe #740
						return false;
					}
					utilities.jumpState.put(p.getUniqueId(), p.getInventory().getItemInMainHand().getType() != Material.SHEARS);
					if (p.getInventory().getItemInMainHand().getType() == Material.LEAD) PlayerInventory.consumeItemInHand(p);

					Vector direction = p.getEyeLocation().getDirection().multiply(2.0);
			    	Projectile projectile = p.getWorld().spawn(p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
			    	projectile.setShooter(p);
			    	projectile.setVelocity(direction);
			    	Arrow arrow = (Arrow) projectile;
			    	Bat b = (Bat) p.getWorld().spawnEntity(p.getLocation(), EntityType.BAT);
			    	b.setCanPickupItems(false);
			    	b.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 100000));
			    	b.setLeashHolder(arrow);

			    	utilities.damage.add(p.getUniqueId());
			    	utilities.remove.put(p.getUniqueId(), new Entity[] {b, arrow});
				}
				return true;
			}
			else return false;
		};
	}

}
