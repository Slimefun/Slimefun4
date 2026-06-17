package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.BlockDataCompat;
import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.handlers.BlockDispenseHandler;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.papermc.lib.PaperLib;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * This {@link Listener} listens to the {@link BlockDispenseEvent} and calls the
 * {@link BlockDispenseHandler} as a result of that.
 * 
 * @author TheBusyBiscuit
 * @author MisterErwin
 * 
 * @see BlockDispenseHandler
 *
 */
public class DispenserListener implements Listener {

    public DispenserListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockDispensing(BlockDispenseEvent e) {
        Block b = e.getBlock();

        if (b.getType() == Material.DISPENSER && b.getRelative(BlockFace.DOWN).getType() != Material.HOPPER) {
            SlimefunItem machine = BlockStorage.check(b);

            // Fixes #2959
            if (machine != null && !machine.isDisabledIn(e.getBlock().getWorld())) {
                machine.callItemHandler(BlockDispenseHandler.class, handler -> {
                    BlockState state = PaperLib.getBlockState(b, false).getState();

                    if (state instanceof Dispenser) {
                        Dispenser dispenser = (Dispenser) state;
                        Object facing = BlockDataCompat.get(BlockDataCompat.getBlockData(b), "getFacing");

                        if (!(facing instanceof BlockFace)) {
                            // 1.8-1.12 have no BlockData; the facing lives on the legacy MaterialData.
                            org.bukkit.material.MaterialData data = state.getData();
                            if (data instanceof org.bukkit.material.Directional) {
                                facing = ((org.bukkit.material.Directional) data).getFacing();
                            }
                        }

                        if (facing instanceof BlockFace) {
                            Block block = b.getRelative((BlockFace) facing);
                            handler.onBlockDispense(e, dispenser, block, machine);
                        }
                    }
                });
            }
        }
    }
}

