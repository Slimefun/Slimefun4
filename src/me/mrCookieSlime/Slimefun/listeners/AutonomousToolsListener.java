package me.mrCookieSlime.Slimefun.listeners;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.AutonomousMachineHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.AutonomousToolHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

public class AutonomousToolsListener implements Listener {
	
	public AutonomousToolsListener(SlimefunStartup plugin) {
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
				for (ItemHandler handler: SlimefunItem.getHandlers("AutonomousMachineHandler")) {
					if (((AutonomousMachineHandler) handler).onBlockDispense(e, dispenser, d, block, chest, machine)) break;
				}
			}
			else {
				for (int i = 0; i < d.getInventory().getContents().length; i++) {
					for (ItemHandler handler: SlimefunItem.getHandlers("AutonomousToolHandler")) {
						if (((AutonomousToolHandler) handler).onBlockDispense(e, dispenser, d, block, chest, i)) break;
					}
				}
			}
		}
	}

}
