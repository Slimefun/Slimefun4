package me.mrCookieSlime.Slimefun.listeners;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Android.AndroidObject;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;

public class AndroidKillingListener implements Listener {
	
	public AndroidKillingListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDeath(final EntityDeathEvent e) {
		if (e.getEntity().hasMetadata("android_killer")) {
			for (MetadataValue value: e.getEntity().getMetadata("android_killer")) {
				final AndroidObject obj = (AndroidObject) value.value();
				Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {
					
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						List<ItemStack> items = new ArrayList<ItemStack>();
						for (Entity n: e.getEntity().getNearbyEntities(0.5D, 0.5D, 0.5D)) {
							if (n instanceof Item && !n.hasMetadata("no_pickup")) {
								items.add(((Item) n).getItemStack());
								n.remove();
							}
						}
						
						switch (e.getEntityType()) {
						case BLAZE: {
							items.add(new ItemStack(Material.BLAZE_ROD, 1 + CSCoreLib.randomizer().nextInt(2)));
							break;
						}
						case PIG_ZOMBIE: {
							items.add(new ItemStack(Material.GOLD_NUGGET, 1 + CSCoreLib.randomizer().nextInt(3)));
							break;
						}
						case SKELETON: {
							if (((Skeleton) e.getEntity()).getSkeletonType().equals(SkeletonType.WITHER)) {
								if (CSCoreLib.randomizer().nextInt(250) < 2) items.add(new MaterialData(Material.SKULL_ITEM, (byte) 1).toItemStack(1));
							}
							break;
						}
						default:
							break;
						}
						
						obj.getAndroid().addItems(obj.getBlock(), items.toArray(new ItemStack[items.size()]));
						ExperienceOrb exp = (ExperienceOrb) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.EXPERIENCE_ORB);
						exp.setExperience(1 + CSCoreLib.randomizer().nextInt(6));
					}
				}, 1L);
				return;
			}
		}
	}
}
