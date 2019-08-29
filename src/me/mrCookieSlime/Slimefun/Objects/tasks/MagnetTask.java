package me.mrCookieSlime.Slimefun.Objects.tasks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class MagnetTask extends SlimefunTask {

	public MagnetTask(Player p) {
		super(p);
	}

	@Override
	void executeTask() {
		for (Entity item: Bukkit.getPlayer(uuid).getNearbyEntities(6D, 6D, 6D)) {
			if (item instanceof Item && !item.hasMetadata("no_pickup") && ((Item) item).getPickupDelay() <= 0) {
				item.teleport(Bukkit.getPlayer(uuid).getEyeLocation());
				Bukkit.getPlayer(uuid).getWorld().playSound(Bukkit.getPlayer(uuid).getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5L, 2L);
			}
		}
	}
	
	@Override
	protected boolean cancelTask() {
		return super.cancelTask() || p.getGameMode() == GameMode.SPECTATOR;
	}

}
