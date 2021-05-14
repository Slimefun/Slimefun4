package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * This is a base class for any ArmorTask, it checks every online player
 * and handles any armor functionality.
 *
 * @author martinbrom
 * @see ArmorTask
 * @see RainbowArmorTask
 */
public abstract class AbstractArmorTask implements Runnable {

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.isValid() || p.isDead()) {
                continue;
            }

            PlayerProfile.get(p, profile -> {
                ItemStack[] armor = p.getInventory().getArmorContents();

                handle(p, profile, armor);
            });
        }
    }

    protected abstract void handle(Player p, PlayerProfile profile, ItemStack[] armor);

}
