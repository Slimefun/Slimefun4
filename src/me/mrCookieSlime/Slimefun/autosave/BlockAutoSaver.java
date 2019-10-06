package me.mrCookieSlime.Slimefun.autosave;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.Bukkit;
import org.bukkit.World;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class BlockAutoSaver implements Runnable {
	
	@Override
	public void run() {
		Set<BlockStorage> worlds = new HashSet<>();
		
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
            Slimefun.getLogger().log(Level.INFO, "正在自动保存数据... (下一次自动保存将在 " + SlimefunPlugin.getCfg().getInt("options.auto-save-delay-in-minutes") + "分钟后)");
			
			for (BlockStorage storage: worlds) {
				storage.save(false);
			}
		}
	}

}
