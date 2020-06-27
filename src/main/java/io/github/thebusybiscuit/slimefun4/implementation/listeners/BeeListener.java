package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Optional;

import io.github.thebusybiscuit.slimefun4.core.attributes.CustomProtection;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.items.HashedArmorpiece;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.HazmatArmorPiece;
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
        if (e.getDamager() instanceof Bee && e.getEntity() instanceof Player) {

            Player p = (Player) e.getEntity();
            Optional<PlayerProfile> optional = PlayerProfile.find(p);
            if (!optional.isPresent()) {
                PlayerProfile.request(p);
                return;
            }
            PlayerProfile profile = optional.get();

            HashedArmorpiece[] armors = profile.getArmor();
            if (shouldProtect(armors)) {
                for (ItemStack armor : p.getInventory().getArmorContents()) {
                    ItemUtils.damageItem(armor, 1, false);
                }
                e.setDamage(0D);
            }
        }
    }

    private boolean shouldProtect(HashedArmorpiece[] armors) {
        int armorCount = 0;
        boolean first = true;

        String setID = null;
        for (HashedArmorpiece armor : armors) {
            Optional<SlimefunArmorPiece> armorPiece = armor.getItem();
            if (!armorPiece.isPresent()) return false;

            if (armorPiece.get() instanceof CustomProtection) {
                CustomProtection protectedArmor = (CustomProtection) armorPiece.get();

                if (first) {
                    if (protectedArmor.requireFullSet()) setID = armorPiece.get().getSetID();
                    first = false;
                }

                for (CustomProtection.ProtectionType protectionType : protectedArmor.getProtectionTypes()) {
                    if (protectionType == CustomProtection.ProtectionType.BEES) {
                        if (setID == null) {
                            return true;
                        }
                        armorCount++;
                    }
                }

            }
        }

        return armorCount == 4;
    }
}
