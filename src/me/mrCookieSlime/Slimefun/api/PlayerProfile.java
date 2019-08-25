package me.mrCookieSlime.Slimefun.api;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

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
	private Map<Integer, BackpackInventory> backpacks = new HashMap<>();
	
	private PlayerProfile(UUID uuid) {
		this.uuid = uuid;
		cfg = new Config(new File("data-storage/Slimefun/Players/" + uuid.toString() + ".yml"));
		
		for (Research research: Research.list()) {
			if (cfg.contains("researches." + research.getID())) researches.add(research);
		}
	}
	
	protected Config getConfig() {
		return cfg;
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
		for (BackpackInventory backpack: backpacks.values()) {
			backpack.save();
		}

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
	 * This Method will return all Researches that this Player has unlocked
	 * 
	 * @return	A Hashset<Research> of all Researches this Player has unlocked
	 */
	public Set<Research> getResearches() {
		return researches;
	}
	
	/**
	 * Call this method if the Player has left.
	 * The profile can then be removed from RAM.
	 */
	public void markForDeletion() {
		this.markedForDeletion = true;
	}
	
	/**
	 * Call this method if this Profile has unsaved changes.
	 */
	public void markDirty() {
		this.dirty = true;
	}
	
	public BackpackInventory createBackpack(int size) {
		IntStream stream = IntStream.iterate(0, i -> i + 1).filter(i -> !cfg.contains("backpacks." + i + ".size"));
		int id = stream.findFirst().getAsInt();
		
		BackpackInventory backpack = new BackpackInventory(this, id, size);
		backpacks.put(id, backpack);
		
		return backpack;
	}
	
	public BackpackInventory getBackpack(int id) {
		BackpackInventory backpack = backpacks.get(id);
		
		if (backpack != null) return backpack;
		else {
			backpack = new BackpackInventory(this, id);
			backpacks.put(id, backpack);
			return backpack;
		}
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
	
	public static BackpackInventory getBackpack(ItemStack item) {
		if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) return null;
		
		Optional<Integer> id = Optional.empty();
		String uuid = "";
		
		for (String line: item.getItemMeta().getLore()) {
			if (line.startsWith(ChatColor.translateAlternateColorCodes('&', "&7ID: ")) && line.contains("#")) {
				try {
					id = Optional.of(Integer.parseInt(line.split("#")[1]));
					uuid = line.split("#")[0].replace(ChatColor.translateAlternateColorCodes('&', "&7ID: "), "");
				} catch(NumberFormatException x) {
					return null;
				}
			}
		}
		
		if (id.isPresent()) {
			return PlayerProfile.fromUUID(UUID.fromString(uuid)).getBackpack(id.get());
		}
		else {
			return null;
		}
	}

}
