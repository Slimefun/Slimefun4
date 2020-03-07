package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.Cooler;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.Juice;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;

/**
 * This {@link Listener} listens for a {@link FoodLevelChangeEvent} and consumes a {@link Juice}
 * from any {@link Cooler} that can be found in the {@link Inventory} of the given {@link Player}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Cooler
 * @see Juice
 *
 */
public class CoolerListener implements Listener {

    public CoolerListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onStarve(FoodLevelChangeEvent e) {
        if (e.getFoodLevel() < ((Player) e.getEntity()).getFoodLevel()) {
            Player p = (Player) e.getEntity();

            for (ItemStack item : p.getInventory().getContents()) {
                if (SlimefunManager.isItemSimilar(item, SlimefunItems.COOLER, false)) {
                    PlayerBackpack backpack = PlayerProfile.getBackpack(item);

                    if (backpack != null && tryConsumeJuice(p, backpack)) {
                        break;
                    }
                }
            }
        }
    }

    private boolean tryConsumeJuice(Player p, PlayerBackpack backpack) {
        Inventory inv = backpack.getInventory();
        int slot = -1;

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack stack = inv.getItem(i);

            if (stack != null && stack.getType() == Material.POTION && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()) {
                slot = i;
                break;
            }
        }

        if (slot >= 0) {
            PotionMeta im = (PotionMeta) inv.getItem(slot).getItemMeta();

            for (PotionEffect effect : im.getCustomEffects()) {
                p.addPotionEffect(effect);
            }

            p.setSaturation(6F);
            p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1F, 1F);
            inv.setItem(slot, null);
            backpack.markDirty();
            return true;
        }

        return false;
    }

}
