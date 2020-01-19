package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class ExplosionsListener implements Listener {

	public ExplosionsListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent e) {
		Iterator<Block> blocks = e.blockList().iterator();
		
		while (blocks.hasNext()) {
			Block block = blocks.next();
			String id = BlockStorage.checkID(block);
    		if (id != null) {
    			blocks.remove();
    			
    			if (!id.equalsIgnoreCase("HARDENED_GLASS") && !id.equalsIgnoreCase("WITHER_PROOF_OBSIDIAN") && !id.equalsIgnoreCase("WITHER_PROOF_GLASS") && !id.equalsIgnoreCase("FORCEFIELD_PROJECTOR") && !id.equalsIgnoreCase("FORCEFIELD_RELAY")) {
    				boolean success = true;
    				SlimefunItem sfItem = SlimefunItem.getByID(id);
    				
    				SlimefunBlockHandler blockHandler = SlimefunPlugin.getUtilities().blockHandlers.get(sfItem.getID());
    				
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
