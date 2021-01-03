package io.github.thebusybiscuit.slimefun4.implementation.listeners.entity;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

/**
 * The {@link Listener} for Hazmat Suit's {@link Bee} sting protection.
 * Only applied if the whole set is worn.
 *
 * @author Linox
 *
 */
public class BeeListener implements Listener {

    public BeeListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Bee && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            Optional<PlayerProfile> optional = PlayerProfile.find(p);

            if (!optional.isPresent()) {
                PlayerProfile.request(p);
                return;
            }

            PlayerProfile profile = optional.get();

            if (profile.hasFullProtectionAgainst(ProtectionType.BEES)) {
                for (ItemStack armor : p.getInventory().getArmorContents()) {
                    if (armor != null) {
                        ItemUtils.damageItem(armor, 1, false);
                    }
                }

                e.setDamage(0D);
            }
        }
    }

}
