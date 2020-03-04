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
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockDispenseHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class DispenserListener implements Listener {

    public DispenserListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockDispensing(BlockDispenseEvent e) {
        Block b = e.getBlock();

        if (b.getType() == Material.DISPENSER && b.getRelative(BlockFace.DOWN).getType() != Material.HOPPER) {
            SlimefunItem machine = BlockStorage.check(b);

            if (machine != null) {
                Dispenser dispenser = (Dispenser) b.getState();
                BlockFace face = ((Directional) b.getBlockData()).getFacing();
                Block block = b.getRelative(face);
                machine.callItemHandler(BlockDispenseHandler.class, handler -> handler.onBlockDispense(e, dispenser, block, machine));
            }
        }
    }
}
