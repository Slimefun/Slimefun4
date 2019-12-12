package me.mrCookieSlime.Slimefun.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.androids.AndroidEntity;

public class AndroidKillingListener implements Listener {
	
	private final Random random = new Random();
	
	public AndroidKillingListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(final EntityDeathEvent e) {
		if (e.getEntity().hasMetadata("android_killer")) {
			final AndroidEntity obj = (AndroidEntity) e.getEntity().getMetadata("android_killer").get(0).value();
					
			Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
				List<ItemStack> items = new ArrayList<>();
				
				for (Entity n : e.getEntity().getNearbyEntities(0.5D, 0.5D, 0.5D)) {
					if (n instanceof Item && !n.hasMetadata("no_pickup")) {
						items.add(((Item) n).getItemStack());
						n.remove();
					}
				}
				
				switch (e.getEntityType()) {
					case BLAZE:
						items.add(new ItemStack(Material.BLAZE_ROD, 1 + random.nextInt(2)));
						break;
					case PIG_ZOMBIE:
						items.add(new ItemStack(Material.GOLD_NUGGET, 1 + random.nextInt(3)));
						break;
					case WITHER_SKELETON:
						if (random.nextInt(250) < 2) {
							items.add(new ItemStack(Material.WITHER_SKELETON_SKULL));
						}
						break;
					default:
						break;
				}
				
				obj.getAndroid().addItems(obj.getBlock(), items.toArray(new ItemStack[items.size()]));
				ExperienceOrb exp = (ExperienceOrb) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.EXPERIENCE_ORB);
				exp.setExperience(1 + random.nextInt(6));
			}, 1L);
		}
	}
}
