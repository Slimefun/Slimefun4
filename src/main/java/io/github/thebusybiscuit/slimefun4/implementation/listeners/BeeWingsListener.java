package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.BeeWings;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.BeeWingsTask;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This {@link Listener} is responsible for the slow falling effect given to the {@link Player}
 * when nearing the ground while using the {@link BeeWings}.
 *
 * @author beSnow
 * @author Linox
 * @author TheBusyBiscuit
 * 
 * @see BeeWings
 * @see BeeWingsTask
 * 
 */
public class BeeWingsListener implements Listener {

    private final BeeWings wings;

    public BeeWingsListener(@Nonnull SlimefunPlugin plugin, @Nonnull BeeWings wings) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.wings = wings;
    }

    @EventHandler(ignoreCancelled = true)
    public void onApproachGround(EntityToggleGlideEvent e) {
        if (!e.isGliding() || wings.isDisabled() || !(e.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getEntity();
        ItemStack chestplate = player.getInventory().getChestplate();

        if (wings.isItem(chestplate) && Slimefun.hasUnlocked(player, chestplate, true)) {
            new BeeWingsTask(player).scheduleRepeating(3, 1);
        }
    }
}
