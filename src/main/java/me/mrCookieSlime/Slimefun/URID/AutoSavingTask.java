package me.mrCookieSlime.Slimefun.URID;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class AutoSavingTask implements Runnable {
	
	@Override
	public void run() {
		Set<BlockStorage> worlds = new HashSet<BlockStorage>();
		
		for (World world: Bukkit.getWorlds()) {
			if (BlockStorage.isWorldRegistered(world.getName())) {
				BlockStorage storage = BlockStorage.getStorage(world);
				storage.computeChanges();
				
				if (storage.getChanges() > 0) {
					worlds.add(storage);
				}
			}
		}
		
		if (!worlds.isEmpty()) {
			System.out.println("[Slimefun] Auto-Saving Data... (Next Auto-Save: " + SlimefunStartup.getCfg().getInt("options.auto-save-delay-in-minutes") + "m)");
			
			for (BlockStorage storage: worlds) {
				storage.save(false);
			}
		}
		
	}

}
