package me.mrCookieSlime.Slimefun.Objects.tasks;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class MagnetTask implements Runnable {
	
	UUID uuid;
	int id;
	
	public MagnetTask(Player p) {
		this.uuid = p.getUniqueId();
	}
	
	public void setID(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		if (Bukkit.getPlayer(uuid) == null) Bukkit.getScheduler().cancelTask(id);
		else if (Bukkit.getPlayer(uuid).isDead()) Bukkit.getScheduler().cancelTask(id);
		else if (!Bukkit.getPlayer(uuid).isSneaking()) Bukkit.getScheduler().cancelTask(id);
		else {
			for (Entity item: Bukkit.getPlayer(uuid).getNearbyEntities(6D, 6D, 6D)) {
				if (item instanceof Item) {
					if (!item.hasMetadata("no_pickup") && ((Item) item).getPickupDelay() <= 0) {
						item.teleport(Bukkit.getPlayer(uuid).getEyeLocation());
						Bukkit.getPlayer(uuid).getWorld().playSound(Bukkit.getPlayer(uuid).getEyeLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5L, 2L);
					}
				}
			}
		}
	}

}
