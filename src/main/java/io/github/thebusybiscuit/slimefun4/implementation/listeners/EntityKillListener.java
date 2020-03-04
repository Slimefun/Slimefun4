package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.EntityKillHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class EntityKillListener implements Listener {

    public EntityKillListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            Player p = e.getEntity().getKiller();
            ItemStack item = p.getInventory().getItemInMainHand();

            Set<ItemStack> customDrops = SlimefunPlugin.getRegistry().getMobDrops(e.getEntityType());
            if (customDrops != null && !customDrops.isEmpty()) {
                for (ItemStack drop : customDrops) {
                    if (Slimefun.hasUnlocked(p, drop, true)) {
                        if (SlimefunManager.isItemSimilar(drop, SlimefunItems.BASIC_CIRCUIT_BOARD, true) && !((boolean) Slimefun.getItemValue("BASIC_CIRCUIT_BOARD", "drop-from-golems"))) {
                            continue;
                        }

                        e.getDrops().add(drop);
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
}
