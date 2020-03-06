package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import io.github.thebusybiscuit.slimefun4.implementation.items.armor.Parachute;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.JetBoots;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.Jetpack;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.JetBootsTask;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.JetpackTask;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.MagnetTask;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.ParachuteTask;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class GearListener implements Listener {

    public GearListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent e) {
        if (e.isSneaking()) {
            Player p = e.getPlayer();

            if (p.getInventory().getChestplate() != null) {
                SlimefunItem chestplate = SlimefunItem.getByItem(p.getInventory().getChestplate());
                handleChestplate(p, chestplate);
            }

            if (p.getInventory().getBoots() != null) {
                SlimefunItem boots = SlimefunItem.getByItem(p.getInventory().getBoots());
                handleBoots(p, boots);
            }

            if (SlimefunManager.containsSimilarItem(p.getInventory(), SlimefunItems.INFUSED_MAGNET, true)) {
                new MagnetTask(p).scheduleRepeating(0, 8);
            }
        }
    }

    private void handleChestplate(Player p, SlimefunItem chestplate) {
        if (chestplate == null || !Slimefun.hasUnlocked(p, chestplate, true)) {
            return;
        }

        if (chestplate instanceof Jetpack) {
            double thrust = ((Jetpack) chestplate).getThrust();

            if (thrust > 0.2) {
                new JetpackTask(p, thrust).scheduleRepeating(0, 3);
            }
        }
        else if (chestplate instanceof Parachute) {
            new ParachuteTask(p).scheduleRepeating(0, 3);
        }
    }

    private void handleBoots(Player p, SlimefunItem boots) {
        if (boots instanceof JetBoots && Slimefun.hasUnlocked(p, boots, true)) {
            double speed = ((JetBoots) boots).getSpeed();

            if (speed > 0.2) {
                new JetBootsTask(p, speed).scheduleRepeating(0, 2);
            }
        }
    }
}
