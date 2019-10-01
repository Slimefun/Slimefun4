package me.mrCookieSlime.Slimefun.api;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.api.inventory.BackpackInventory;

/**
 * A class that can store a Player's Research Profile for caching
 * 
 * @author TheBusyBiscuit
 *
 */
public final class PlayerProfile {
	
	private String name;
	private UUID uuid;
	private Config cfg;
	private boolean dirty = false;
	private boolean markedForDeletion = false;
	
	private Set<Research> researches = new HashSet<>();
	private Map<Integer, BackpackInventory> backpacks = new HashMap<>();
	
	private PlayerProfile(OfflinePlayer p) {
		this.uuid = p.getUniqueId();
		this.name = p.getName();
		
		cfg = new Config(new File("data-storage/Slimefun/Players/" + uuid.toString() + ".yml"));
		
		for (Research research: Research.list()) {
			if (cfg.contains("researches." + research.getID())) researches.add(research);
		}
	}
	
	private PlayerProfile(UUID uuid) {
		this.uuid = uuid;
		this.name = Bukkit.getOfflinePlayer(uuid).getName();
		cfg = new Config(new File("data-storage/Slimefun/Players/" + uuid.toString() + ".yml"));
		
		for (Research research: Research.list()) {
			if (cfg.contains("researches." + research.getID())) researches.add(research);
		}
	}
	
	public Config getConfig() {
		return cfg;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	/**
	 * This method returns whether the Player has logged off.
	 * If this is true, then the Profile can be removed from RAM.
	 * 
	 * @return	Whether the Profile is marked for deletion
	 */
	public boolean isMarkedForDeletion() {
		return markedForDeletion;
	}
	
	/**
	 * This method returns whether the Profile has unsaved changes
	 * 
	 * @return	Whether there are unsaved changes
	 */
	public boolean isDirty() {
		return dirty;
	}
	
	/**
	 * This method will save the Player's Researches and Backpacks to the hard drive
	 */
	public void save() {
		for (BackpackInventory backpack: backpacks.values()) {
			backpack.save();
		}

		cfg.save();
		dirty = false;
	}
	
	/**
	 * This method sets the Player's "researched" status for this Research.
	 * Use the boolean to unlock or lock the Research
	 * 
	 * @param research	The Research that should be unlocked or locked
	 * @param unlock	Whether the Research should be unlocked or locked
	 */
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
	
	/**
	 * This method returns whether the Player has unlocked the given Research
	 * 
	 * @param research	The Research that is being queried
	 * @return			Whether this Research has been unlocked
	 */
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

	public String getTitle() {
		List<String> titles = SlimefunPlugin.getSettings().researchesTitles;
		
		int index = Math.round(Float.valueOf(String.valueOf(Math.round(((researches.size() * 100.0F) / Research.list().size()) * 100.0F) / 100.0F)) / 100.0F) *  titles.size();
		if (index > 0) index--;
		return titles.get(index);
	}
	
	public void sendStats(CommandSender sender) {
		Set<Research> researched = getResearches();
		int levels = researched.stream().mapToInt(Research::getCost).sum();
		
		String progress = String.valueOf(Math.round(((researched.size() * 100.0F) / Research.list().size()) * 100.0F) / 100.0F);
		if (Float.parseFloat(progress) < 16.0F) progress = "&4" + progress + " &r% ";
		else if (Float.parseFloat(progress) < 32.0F) progress = "&c" + progress + " &r% ";
		else if (Float.parseFloat(progress) < 48.0F) progress = "&6" + progress + " &r% ";
		else if (Float.parseFloat(progress) < 64.0F) progress = "&e" + progress + " &r% ";
		else if (Float.parseFloat(progress) < 80.0F) progress = "&2" + progress + " &r% ";
		else progress = "&a" + progress + " &r% ";

		sender.sendMessage("");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Statistics for Player: &b" + name));
		sender.sendMessage("");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Title: &b" + getTitle()));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Research Progress: " + progress + "&e(" + researched.size() + " / " + Research.list().size() + ")"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Total XP Levels spent: &b" + levels));
	}
	
	public static PlayerProfile fromUUID(UUID uuid) {
		PlayerProfile profile = SlimefunPlugin.getUtilities().profiles.get(uuid);
		
		if (profile == null) {
			profile = new PlayerProfile(uuid);
			SlimefunPlugin.getUtilities().profiles.put(uuid, profile);
		}
		else {
			profile.markedForDeletion = false;
		}
		
		return profile;
	}
	
	public static PlayerProfile get(OfflinePlayer p) {
		PlayerProfile profile = SlimefunPlugin.getUtilities().profiles.get(p.getUniqueId());
		
		if (profile == null) {
			profile = new PlayerProfile(p);
			SlimefunPlugin.getUtilities().profiles.put(p.getUniqueId(), profile);
		}
		else {
			profile.markedForDeletion = false;
		}
		
		return profile;
	}

	public static boolean isLoaded(UUID uuid) {
		return SlimefunPlugin.getUtilities().profiles.containsKey(uuid);
	}

	public static Iterator<PlayerProfile> iterator() {
		return SlimefunPlugin.getUtilities().profiles.values().iterator();
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
	
	@Override
	public String toString() {
		return "PlayerProfile {" + uuid + "}";
	}

}
