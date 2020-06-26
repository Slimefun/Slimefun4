package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

/**
 * The listener for Hazmat Suit's {@link Bee} sting protection.
 * Only applied if the whole set is worn.
 *
 * @author Linox
 *
 * @see Bee
 *
 */
public class HazmatSuitListener implements Listener {

    public HazmatSuitListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_15)) return;

        if (e.getDamager() instanceof Bee) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();

                int hazmatCount = 0;
                for (ItemStack armor : p.getInventory().getArmorContents()) {
                    SlimefunItem sfItem = SlimefunItem.getByItem(armor);
                    if (sfItem == null) return;

                    String id = sfItem.getID();
                    if (id.equals("SCUBA_HELMET")) hazmatCount++;
                    if (id.equals("HAZMAT_CHESTPLATE")) hazmatCount++;
                    if (id.equals("HAZMAT_LEGGINGS")) hazmatCount++;
                    if (id.equals("RUBBER_BOOTS")) hazmatCount++;
                }

                if (hazmatCount == 4) {
                    e.setDamage(0D);
                    for (ItemStack armor : p.getInventory().getArmorContents()) {
                        ItemUtils.damageItem(armor, 1, false);
                    }
                }
            }
        }
    }
}
