package me.mrCookieSlime.Slimefun.URID;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class AutoSavingTask implements Runnable {
	
	@Override
	public void run() {
		System.out.println("[Slimefun] Auto-Saving Data... (Next Auto-Save: " + SlimefunStartup.getCfg().getInt("options.auto-save-delay-in-minutes") + "m)");
		for (World world: Bukkit.getWorlds()) {
			if (BlockStorage.isWorldRegistered(world.getName())) BlockStorage.getStorage(world).save(false);
		}
	}

}
