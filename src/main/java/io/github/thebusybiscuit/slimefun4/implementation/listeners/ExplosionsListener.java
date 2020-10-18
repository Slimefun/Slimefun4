package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Iterator;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class ExplosionsListener implements Listener {

    public ExplosionsListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e) {
        Iterator<Block> blocks = e.blockList().iterator();

        while (blocks.hasNext()) {
            Block block = blocks.next();

            SlimefunItem item = BlockStorage.check(block);
            if (item != null) {
                blocks.remove();

                if (!(item instanceof WitherProof)) {
                    SlimefunBlockHandler blockHandler = SlimefunPlugin.getRegistry().getBlockHandlers().get(item.getId());
                    boolean success = true;

                    if (blockHandler != null) {
                        success = blockHandler.onBreak(null, block, item, UnregisterReason.EXPLODE);
                    }

                    if (success) {
                        BlockStorage.clearBlockInfo(block);
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
}
