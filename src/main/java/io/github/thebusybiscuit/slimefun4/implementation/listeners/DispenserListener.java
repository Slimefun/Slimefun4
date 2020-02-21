package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockDispenseHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class DispenserListener implements Listener {
	
    public DispenserListener(SlimefunPlugin plugin) {
         plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockDispensing(BlockDispenseEvent e) {
        Block dispenser = e.getBlock();
        
        if (dispenser.getType() == Material.DISPENSER) {
            Dispenser d = (Dispenser) dispenser.getState();

            BlockFace face = ((Directional) dispenser.getBlockData()).getFacing();

            Block block = dispenser.getRelative(face);
            SlimefunItem machine = BlockStorage.check(dispenser);

            if (machine != null) {
                if (machine.isItem(SlimefunItems.BLOCK_PLACER) && dispenser.getRelative(BlockFace.DOWN).getType() == Material.HOPPER){
                    e.setCancelled(true);
                } 
                else {
                    machine.callItemHandler(BlockDispenseHandler.class, handler -> handler.onBlockDispense(e, d, block, machine));
                }
            }
        }
    }
}
