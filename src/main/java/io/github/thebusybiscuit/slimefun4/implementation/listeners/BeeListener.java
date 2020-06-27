package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Optional;

import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.items.HashedArmorpiece;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

/**
 * The listener for Hazmat Suit's {@link Bee} sting protection.
 * Only applied if the whole set is worn.
 *
 * @author Linox
 *
 */
public class BeeListener implements Listener {

    public BeeListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Bee) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                PlayerProfile.get(p, profile -> {

                    HashedArmorpiece[] armors = profile.getArmor();
                    if (hasFullHazmat(armors)) {
                        for (ItemStack armor : p.getInventory().getArmorContents()) {
                            ItemUtils.damageItem(armor, 1, false);
                        }
                        e.setDamage(0D);
                    }
                });
            }
        }
    }

    private boolean hasFullHazmat(HashedArmorpiece[] armors) {
        int hazmatCount = 0;

        // Check for a Hazmat Suit
        for (HashedArmorpiece armor : armors) {
            Optional<SlimefunArmorPiece> armorPiece = armor.getItem();
            if (!armorPiece.isPresent()) return false;

            if (armorPiece.get().getID().equals("SCUBA_HELMET") ||
                    armorPiece.get().getID().equals("HAZMAT_CHESTPLATE") ||
                    armorPiece.get().getID().equals("HAZMAT_LEGGINGS") ||
                    armorPiece.get().getID().equals("RUBBER_BOOTS")) {
                hazmatCount++;
            }
        }

        return hazmatCount == 4;
    }
}
