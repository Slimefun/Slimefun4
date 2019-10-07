package me.mrCookieSlime.Slimefun.listeners;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.JetBoots;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Jetpack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.tasks.JetBootsTask;
import me.mrCookieSlime.Slimefun.Objects.tasks.JetpackTask;
import me.mrCookieSlime.Slimefun.Objects.tasks.MagnetTask;
import me.mrCookieSlime.Slimefun.Objects.tasks.ParachuteTask;
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
			final Player p = e.getPlayer();

			if (SlimefunItem.getByItem(p.getInventory().getChestplate()) != null) {
				SlimefunItem item = SlimefunItem.getByItem(p.getInventory().getChestplate());

				if (item instanceof Jetpack) {
					if (Slimefun.hasUnlocked(p, item.getItem(), true)) {
						double thrust = ((Jetpack) item).getThrust();

						if (thrust > 0.2) {
							JetpackTask task = new JetpackTask(p, thrust);
							task.setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(SlimefunPlugin.instance, task, 0L, 3L));
						}
					}
				}
				else if (item.isItem(SlimefunItems.PARACHUTE) && Slimefun.hasUnlocked(p, SlimefunItems.PARACHUTE, true)) {
					ParachuteTask task = new ParachuteTask(p);
					task.setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(SlimefunPlugin.instance, task, 0L, 3L));
				}
			}
			if (SlimefunItem.getByItem(p.getInventory().getBoots()) != null) {
				SlimefunItem item = SlimefunItem.getByItem(p.getInventory().getBoots());

				if (item instanceof JetBoots && Slimefun.hasUnlocked(p, item.getItem(), true)) {
					double speed = ((JetBoots) item).getSpeed();
					if (speed > 0.2) {
						JetBootsTask task = new JetBootsTask(p, speed);
						task.setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(SlimefunPlugin.instance, task, 0L, 2L));
					}
				}
			}
			if (p.getInventory().containsAtLeast(SlimefunItems.INFUSED_MAGNET, 1)) {
				MagnetTask task = new MagnetTask(p);
				task.setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(SlimefunPlugin.instance, task, 0L, 8L));
			}
		}
	}

}
