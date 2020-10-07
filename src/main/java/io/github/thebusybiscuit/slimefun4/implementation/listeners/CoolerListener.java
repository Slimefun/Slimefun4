package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.Cooler;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.Juice;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This {@link Listener} listens for a {@link FoodLevelChangeEvent} or an {@link EntityDamageEvent} for starvation
 * damage
 * and consumes a {@link Juice} from any {@link Cooler} that can be found in the {@link Inventory} of the given
 * {@link Player}.
 * 
 * @author TheBusyBiscuit
 * @author Linox
 * 
 * @see Cooler
 * @see Juice
 *
 */
public class CoolerListener implements Listener {

    private final Cooler cooler;

    public CoolerListener(@Nonnull SlimefunPlugin plugin, @Nonnull Cooler cooler) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.cooler = cooler;
    }

    @EventHandler
    public void onHungerLoss(FoodLevelChangeEvent e) {
        if (cooler == null || cooler.isDisabled() || !(e.getEntity() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getEntity();

        if (e.getFoodLevel() < p.getFoodLevel()) {
            checkAndConsume(p);
        }
    }

    @EventHandler
    public void onHungerDamage(EntityDamageEvent e) {
        if (cooler == null || cooler.isDisabled() || !(e.getEntity() instanceof Player)) {
            return;
        }

        if (e.getCause() == DamageCause.STARVATION) {
            checkAndConsume((Player) e.getEntity());
        }
    }

    private void checkAndConsume(@Nonnull Player p) {
        for (ItemStack item : p.getInventory().getContents()) {
            if (cooler.isItem(item)) {
                if (Slimefun.hasUnlocked(p, cooler, true)) {
                    takeJuiceFromCooler(p, item);
                } else {
                    return;
                }
            }
        }
    }

    /**
     * This takes a {@link Juice} from the given {@link Cooler} and consumes it in order
     * to restore hunger for the given {@link Player}.
     * 
     * @param p
     *            The {@link Player}
     * @param cooler
     *            The {@link Cooler} {@link ItemStack} to take the {@link Juice} from
     */
    private void takeJuiceFromCooler(@Nonnull Player p, @Nonnull ItemStack cooler) {
        PlayerProfile.getBackpack(cooler, backpack -> {
            if (backpack != null) {
                SlimefunPlugin.runSync(() -> consumeJuice(p, backpack));
            }
        });
    }

    private boolean consumeJuice(@Nonnull Player p, @Nonnull PlayerBackpack backpack) {
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
