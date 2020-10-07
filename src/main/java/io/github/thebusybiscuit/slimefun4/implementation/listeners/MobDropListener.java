package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.RandomMobDrop;
import io.github.thebusybiscuit.slimefun4.core.handlers.EntityKillHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.BasicCircuitBoard;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This {@link Listener} is responsible for handling any custom mob drops.
 * These drops can also be randomized using the interface {@link RandomMobDrop}, otherwise
 * they will be handled via {@link RecipeType}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see RandomMobDrop
 *
 */
public class MobDropListener implements Listener {

    public MobDropListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            Player p = e.getEntity().getKiller();
            ItemStack item = p.getInventory().getItemInMainHand();

            Set<ItemStack> customDrops = SlimefunPlugin.getRegistry().getMobDrops().get(e.getEntityType());

            if (customDrops != null && !customDrops.isEmpty()) {
                for (ItemStack drop : customDrops) {
                    if (canDrop(p, drop)) {
                        e.getDrops().add(drop.clone());
                    }
                }
            }

            if (item.getType() != Material.AIR) {
                SlimefunItem sfItem = SlimefunItem.getByItem(item);

                if (sfItem != null && Slimefun.hasUnlocked(p, sfItem, true)) {
                    sfItem.callItemHandler(EntityKillHandler.class, handler -> handler.onKill(e, e.getEntity(), p, item));
                }
            }
        }
    }

    private boolean canDrop(@Nonnull Player p, @Nonnull ItemStack item) {
        SlimefunItem sfi = SlimefunItem.getByItem(item);

        if (sfi == null) {
            return true;
        } else if (Slimefun.hasUnlocked(p, sfi, true)) {
            if (sfi instanceof RandomMobDrop) {
                int random = ThreadLocalRandom.current().nextInt(100);

                if (((RandomMobDrop) sfi).getMobDropChance() <= random) {
                    return false;
                }
            }

            if (sfi instanceof BasicCircuitBoard) {
                return ((BasicCircuitBoard) sfi).isDroppedFromGolems();
            }

            return true;
        } else {
            return false;
        }
    }
}
