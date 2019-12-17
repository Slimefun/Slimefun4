package me.mrCookieSlime.Slimefun.listeners;

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
import me.mrCookieSlime.Slimefun.Objects.handlers.AutonomousMachineHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class AutonomousToolsListener implements Listener {
	
	public AutonomousToolsListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onBlockDispensing(final BlockDispenseEvent e) {
		Block dispenser = e.getBlock();
		if (dispenser.getType() == Material.DISPENSER) {
			final Dispenser d = (Dispenser) dispenser.getState();

			BlockFace face = ((Directional)dispenser.getBlockData()).getFacing();

			Block block = dispenser.getRelative(face);
			Block chest = dispenser.getRelative(face.getOppositeFace());
			SlimefunItem machine = BlockStorage.check(dispenser);
			
			if (machine != null) {
				for (ItemHandler handler : SlimefunItem.getHandlers("AutonomousMachineHandler")) {
					if (((AutonomousMachineHandler) handler).onBlockDispense(e, dispenser, d, block, chest, machine)) break;
				}
			}
		}
	}

}
