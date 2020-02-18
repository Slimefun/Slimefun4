package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class ResearchListener implements Listener {
	public ResearchListener(final SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onResearchFireworkDamage(final EntityDamageByEntityEvent e) {
		if (e.getEntityType() == EntityType.PLAYER && e.getDamager() instanceof Firework)
			if (((Firework) e.getDamager()).getFireworkMeta().getPersistentDataContainer()
					.get(new NamespacedKey(SlimefunPlugin.instance, "slime"), PersistentDataType.INTEGER) != null
					&& ((Firework) e.getDamager()).getFireworkMeta().getPersistentDataContainer()
							.get(new NamespacedKey(SlimefunPlugin.instance, "slime"), PersistentDataType.INTEGER) == 1)
				e.setCancelled(true);
	}
}
