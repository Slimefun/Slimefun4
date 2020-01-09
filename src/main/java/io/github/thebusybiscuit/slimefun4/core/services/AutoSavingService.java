package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class AutoSavingService {
	
	private int interval;
	
	public void start(Plugin plugin, int interval) {
		this.interval = interval;
		
		plugin.getServer().getScheduler().runTaskTimer(plugin, this::saveAllPlayers, 2000L, interval * 60L * 20L);
		plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::saveAllBlocks, 2000L, interval * 60L * 20L);
		
	}
	
	public void saveAllPlayers() {
		Iterator<PlayerProfile> iterator = PlayerProfile.iterator();
		int players = 0;
		
		while (iterator.hasNext()) {
			PlayerProfile profile = iterator.next();
			
			if (profile.isDirty()) {
				players++;
				profile.save();
			}
			
			if (profile.isMarkedForDeletion()) iterator.remove();
		}
		
		if (players > 0) {
			Slimefun.getLogger().log(Level.INFO, "Auto-Saved Player Data for {0} Player(s)!", players);
		}
	}
	
	public void saveAllBlocks() {
		Set<BlockStorage> worlds = new HashSet<>();
		
		for (World world : Bukkit.getWorlds()) {
			if (BlockStorage.isWorldRegistered(world.getName())) {
				BlockStorage storage = BlockStorage.getStorage(world);
				storage.computeChanges();
				
				if (storage.getChanges() > 0) {
					worlds.add(storage);
				}
			}
		}
		
		if (!worlds.isEmpty()) {
			Slimefun.getLogger().log(Level.INFO, "Auto-Saving Block Data... (Next Auto-Save: {0}m)", interval);
			
			for (BlockStorage storage : worlds) {
				storage.save(false);
			}
		}
	}

}
