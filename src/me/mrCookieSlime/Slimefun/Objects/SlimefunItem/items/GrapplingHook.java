package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Objects.tasks.GrapplingHookTask;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.utils.Utilities;

public class GrapplingHook extends SlimefunItem {

	public GrapplingHook(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
		super(category, item, id, recipeType, recipe, keys, values);
	}

	@Override
	public void register(boolean slimefun) {
		addItemHandler(new ItemInteractionHandler() {
			Utilities utilities = SlimefunPlugin.getUtilities();

			@Override
			public boolean onRightClick(ItemUseEvent e, Player p, ItemStack item) {
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
						Arrow arrow = p.getWorld().spawn(p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Arrow.class);
						arrow.setShooter(p);
						arrow.setVelocity(direction);

						Bat b = (Bat) p.getWorld().spawnEntity(p.getLocation(), EntityType.BAT);
						b.setCanPickupItems(false);
						b.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 100000));
						b.setLeashHolder(arrow);

						utilities.damage.add(p.getUniqueId());
						utilities.remove.put(p.getUniqueId(), new Entity[] {b, arrow});

						// To fix issue #253
						GrapplingHookTask task = new GrapplingHookTask(p, arrow);
						task.setID(Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, task, (int) Slimefun.getItemValue("GRAPPLING_HOOK", "despawn-seconds")));
					}
					return true;
				}
				else return false;
			}
		});

		super.register(slimefun);
	}
}
