package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.Parachute;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.JetBoots;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.Jetpack;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.InfusedMagnet;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.JetBootsTask;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.JetpackTask;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.MagnetTask;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.ParachuteTask;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This {@link Listener} is responsible for listening to the {@link PlayerToggleSneakEvent}
 * to start tasks for various gadgets that are activated by pressing shift,
 * like the {@link Jetpack} or {@link JetBoots}
 * 
 * @author TheBusyBiscuit
 * 
 * @see JetpackTask
 * @see JetBootsTask
 * @see ParachuteTask
 * @see MagnetTask
 *
 */
public class GadgetsListener implements Listener {

    public GadgetsListener(@Nonnull SlimefunPlugin plugin) {
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

            if (SlimefunUtils.containsSimilarItem(p.getInventory(), SlimefunItems.INFUSED_MAGNET, true)) {
                InfusedMagnet magnet = (InfusedMagnet) SlimefunItems.INFUSED_MAGNET.getItem();

                if (Slimefun.hasUnlocked(p, magnet, true)) {
                    new MagnetTask(p, magnet.getRadius()).scheduleRepeating(0, 8);
                }
            }
        }
    }

    private void handleChestplate(@Nonnull Player p, @Nullable SlimefunItem chestplate) {
        if (chestplate == null || !Slimefun.hasUnlocked(p, chestplate, true)) {
            return;
        }

        if (chestplate instanceof Jetpack) {
            double thrust = ((Jetpack) chestplate).getThrust();

            if (thrust > 0.2) {
                new JetpackTask(p, (Jetpack) chestplate).scheduleRepeating(0, 3);
            }
        } else if (chestplate instanceof Parachute) {
            new ParachuteTask(p).scheduleRepeating(0, 3);
        }
    }

    private void handleBoots(@Nonnull Player p, @Nullable SlimefunItem boots) {
        if (boots instanceof JetBoots && Slimefun.hasUnlocked(p, boots, true)) {
            double speed = ((JetBoots) boots).getSpeed();

            if (speed > 0.2) {
                new JetBootsTask(p, (JetBoots) boots).scheduleRepeating(0, 2);
            }
        }
    }
}
