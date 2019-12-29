package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.JetBoots;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Jetpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.tasks.JetBootsTask;
import me.mrCookieSlime.Slimefun.Objects.tasks.JetpackTask;
import me.mrCookieSlime.Slimefun.Objects.tasks.MagnetTask;
import me.mrCookieSlime.Slimefun.Objects.tasks.ParachuteTask;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class GearListener implements Listener {

	public GearListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onToggleSneak(PlayerToggleSneakEvent e) {
		if (e.isSneaking()) {
			Player p = e.getPlayer();
			SlimefunItem chestplate = SlimefunItem.getByItem(p.getInventory().getChestplate());
			SlimefunItem boots = SlimefunItem.getByItem(p.getInventory().getBoots());
			
			if (chestplate != null) {
				if (chestplate instanceof Jetpack) {
					if (Slimefun.hasUnlocked(p, chestplate, true)) {
						double thrust = ((Jetpack) chestplate).getThrust();

						if (thrust > 0.2) {
							JetpackTask task = new JetpackTask(p, thrust);
							task.setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(SlimefunPlugin.instance, task, 0L, 3L));
						}
					}
				}
				else if (chestplate.getID().equals("PARACHUTE") && Slimefun.hasUnlocked(p, chestplate, true)) {
					ParachuteTask task = new ParachuteTask(p);
					task.setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(SlimefunPlugin.instance, task, 0L, 3L));
				}
			}
			
			if (boots instanceof JetBoots && Slimefun.hasUnlocked(p, boots, true)) {
				double speed = ((JetBoots) boots).getSpeed();
				
				if (speed > 0.2) {
					JetBootsTask task = new JetBootsTask(p, speed);
					task.setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(SlimefunPlugin.instance, task, 0L, 2L));
				}
			}
			
			if (SlimefunManager.containsSimilarItem(p.getInventory(), SlimefunItems.INFUSED_MAGNET, true)) {
				MagnetTask task = new MagnetTask(p);
				task.setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(SlimefunPlugin.instance, task, 0L, 8L));
			}
		}
	}

}
