package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.api.events.ClimbingPickLaunchEvent;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ClimbingPick;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

/**
 * The special event listener for {@link ClimbingPick}.
 * This is made like this to allow addons to add more materials to climbing pick.
 *
 * @author Linox
 *
 * @see ClimbingPick
 *
 */
public class ClimbingPickListener implements Listener {

    public ClimbingPickListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onLaunch(ClimbingPickLaunchEvent e) {
        Player p = e.getPlayer();

        p.setVelocity(e.getVelocity());
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
        
        if (p.getGameMode() != GameMode.CREATIVE) {
            ((ClimbingPick) SlimefunItem.getByItem(e.getItem())).damageItem(p, e.getItem());
        }
    }
}
