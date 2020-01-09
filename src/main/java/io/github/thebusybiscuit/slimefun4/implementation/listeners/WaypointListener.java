package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class WaypointListener implements Listener {

    private final SimpleDateFormat format = new SimpleDateFormat("(MMM d, yyyy @ hh:mm)");
	
	public WaypointListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
    public void onDamage(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getInventory().containsAtLeast(SlimefunItems.GPS_EMERGENCY_TRANSMITTER, 1)) {
                Slimefun.getGPSNetwork().addWaypoint(p, "&4Deathpoint &7" + format.format(new Date()), p.getLocation().getBlock().getLocation());
            }
        }
	}
}
