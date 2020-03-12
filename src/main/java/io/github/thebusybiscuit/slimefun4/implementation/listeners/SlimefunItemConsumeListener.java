package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun4.implementation.items.food.Juice;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemConsumptionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class SlimefunItemConsumeListener implements Listener {

    public SlimefunItemConsumeListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        SlimefunItem sfItem = SlimefunItem.getByItem(item);

        if (sfItem != null) {
            if (Slimefun.hasUnlocked(p, sfItem, true)) {
                if (sfItem instanceof Juice) {
                    // Fix for Saturation on potions is no longer working

                    for (PotionEffect effect : ((PotionMeta) item.getItemMeta()).getCustomEffects()) {
                        if (effect.getType().equals(PotionEffectType.SATURATION)) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, effect.getDuration(), effect.getAmplifier()));
                            break;
                        }
                    }

                    removeGlassBottle(p, item);
                }
                else {
                    sfItem.callItemHandler(ItemConsumptionHandler.class, handler -> handler.onConsume(e, p, item));
                }
            }
            else {
                e.setCancelled(true);
            }
        }
    }

    /**
     * Determines from which hand the juice is being drunk, and its amount
     * 
     * @param p
     *            The {@link Player} that triggered this
     * @param item
     *            The {@link ItemStack} in question
     */
    private void removeGlassBottle(Player p, ItemStack item) {
        if (SlimefunManager.isItemSimilar(item, p.getInventory().getItemInMainHand(), true)) {
            if (p.getInventory().getItemInMainHand().getAmount() == 1) {
                Slimefun.runSync(() -> p.getEquipment().getItemInMainHand().setAmount(0));
            }
            else {
                Slimefun.runSync(() -> p.getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 1)));
            }
        }
        else if (SlimefunManager.isItemSimilar(item, p.getInventory().getItemInOffHand(), true)) {
            if (p.getInventory().getItemInOffHand().getAmount() == 1) {
                Slimefun.runSync(() -> p.getEquipment().getItemInOffHand().setAmount(0));
            }
            else {
                Slimefun.runSync(() -> p.getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 1)));
            }
        }
    }

}
