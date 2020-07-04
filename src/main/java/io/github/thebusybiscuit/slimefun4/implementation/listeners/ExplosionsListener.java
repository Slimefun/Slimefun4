package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;

public class ExplosionsListener implements Listener {

    public ExplosionsListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e) {
        Iterator<Block> blocks = e.blockList().iterator();

        while (blocks.hasNext()) {
            Block block = blocks.next();

            String id = BlockStorage.checkID(block);
            if (id != null) {
                blocks.remove();

                if (!id.equals(SlimefunItems.HARDENED_GLASS.getItemId()) && !SlimefunPlugin.getRegistry().getWitherProofBlocks().containsKey(id)) {
                    boolean success = true;
                    SlimefunItem sfItem = SlimefunItem.getByID(id);

                    SlimefunBlockHandler blockHandler = SlimefunPlugin.getRegistry().getBlockHandlers().get(sfItem.getID());

                    if (blockHandler != null) {
                        success = blockHandler.onBreak(null, block, sfItem, UnregisterReason.EXPLODE);
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