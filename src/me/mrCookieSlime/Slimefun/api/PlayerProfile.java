package me.mrCookieSlime.Slimefun.api;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.Research;

/**
 * A class that can store a Player's Research Profile for caching
 * 
 * @author TheBusyBiscuit
 *
 */
public class PlayerProfile {
	
	public static Map<UUID, PlayerProfile> profiles = new HashMap<>();
	
	private UUID uuid;
	private Config cfg;
	private boolean dirty = false;
	private boolean markedForDeletion = false;
	
	private Set<Research> researches = new HashSet<>();
	
	private PlayerProfile(UUID uuid) {
		this.uuid = uuid;
		cfg = new Config(new File("data-storage/Slimefun/Players/" + uuid.toString() + ".yml"));
		
		for (Research research: Research.list()) {
			if (cfg.contains("researches." + research.getID())) researches.add(research);
		}
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public boolean isMarkedForDeletion() {
		return markedForDeletion;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public void save() {
		cfg.save();
		
		dirty = false;
	}
	
	public void setResearched(Research research, boolean unlock) {
		dirty = true;
		
		if (unlock) {
			cfg.setValue("researches." + research.getID(), true);
			researches.add(research);
		}
		else {
			cfg.setValue("researches." + research.getID(), null);
			researches.remove(research);
		}
	}
	
	public boolean hasUnlocked(Research research) {
		return !research.isEnabled() || researches.contains(research);
	}
	
	/**
	 * 
	 * @return	A Hashset<Research> of all Researches this Player has unlocked
	 */
	public Set<Research> getResearches() {
		return researches;
	}

	public void markForDeletion() {
		this.markedForDeletion = true;
	}
	
	public static PlayerProfile fromUUID(UUID uuid) {
		PlayerProfile profile = profiles.get(uuid);
		
		if (profile == null) {
			profile = new PlayerProfile(uuid);
			profiles.put(uuid, profile);
		}
		else {
			profile.markedForDeletion = false;
		}
		
		return profile;
	}

	public static boolean isLoaded(UUID uuid) {
		return profiles.containsKey(uuid);
	}

	public static Iterator<PlayerProfile> iterator() {
		return profiles.values().iterator();
	}

}
