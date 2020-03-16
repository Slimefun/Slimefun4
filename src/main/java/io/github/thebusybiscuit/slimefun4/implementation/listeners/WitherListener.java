package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class WitherListener implements Listener {

    public WitherListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onWitherDestroy(EntityChangeBlockEvent e) {
        if (e.getEntity().getType() == EntityType.WITHER) {
            String id = BlockStorage.checkID(e.getBlock());

            if (id != null) {
                WitherProof witherproof = SlimefunPlugin.getRegistry().getWitherProofBlocks().get(id);

                if (witherproof != null) {
                    e.setCancelled(true);
                    witherproof.onAttack(e.getBlock(), (Wither) e.getEntity());
                }
            }
        }
    }

}
