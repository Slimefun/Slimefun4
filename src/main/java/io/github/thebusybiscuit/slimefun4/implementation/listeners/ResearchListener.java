package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class ResearchListener implements Listener {

	public ResearchListener(final SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onResearchFireworkDamage(final EntityDamageByEntityEvent e) {
		Firework fw = null;
		if (e.getDamager() instanceof Firework) {
			fw = (Firework) e.getDamager();
			if (!fw.getMetadata("slime").isEmpty() && fw.getMetadata("slime").get(0).asBoolean())
				e.setCancelled(true);
		}
	}
}
