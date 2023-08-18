package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.HashedArmorpiece;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.armor.RadiationTask;

/**
 * This {@link Listener} caches the armor of the player on join.
 * This is mainly for the {@link RadiationTask}.
 *
 * @author iTwins
 */
public class JoinListener implements Listener {

    public JoinListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(@Nonnull PlayerJoinEvent e) {
        PlayerProfile.get(e.getPlayer(), playerProfile -> {
            final ItemStack[] armorContents = e.getPlayer().getInventory().getArmorContents();
            final HashedArmorpiece[] hashedArmorpieces = playerProfile.getArmor();
            for (int i = 0; i < 4; i++) {
                if (armorContents[i] != null && armorContents[i].getType() != Material.AIR && SlimefunItem.getByItem(armorContents[i]) instanceof SlimefunArmorPiece sfArmorPiece) {
                    hashedArmorpieces[i].update(armorContents[i], sfArmorPiece);
                }
            }
        });
    }

}
