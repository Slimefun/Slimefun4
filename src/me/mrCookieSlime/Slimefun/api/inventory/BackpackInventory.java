package me.mrCookieSlime.Slimefun.api.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;

public class BackpackInventory {
	
	private int id;
	private int size;
	private PlayerProfile profile;
	private Config cfg;
	private Inventory inventory;
	
	/**
	 * This constructor loads an existing Backpack
	 */
	public BackpackInventory(PlayerProfile profile, int id) {
		this(profile, id, profile.getConfig().getInt("backpacks." + id + ".size"));
		
		for (int i = 0; i < size; i++) {
			inventory.setItem(i, cfg.getItem("backpacks." + id + ".contents." + i));
		}
	}
	
	/**
	 * This constructor creates a new Backpack
	 */
	public BackpackInventory(PlayerProfile profile, int id, int size) {
		this.profile = profile;
		this.id = id;
		this.cfg = profile.getConfig();
		this.size = size;
		
		cfg.setValue("backpacks." + id + ".size", size);
		profile.markDirty();
		
		inventory = Bukkit.createInventory(null, size, "Backpack [" + size + " Slots]");
	}
	
	public int getID() {
		return id;
	}
	
	public int getSize() {
		return size;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public void open(Player... players) {
		for (Player p: players) {
			p.openInventory(inventory);
		}
	}

	public void setSize(int size) {
		this.size = size;
		cfg.setValue("backpacks." + id + ".size", size);
		
		Inventory inventory = Bukkit.createInventory(null, size, "Backpack [" + size + " Slots]");
		
		for (int slot = 0; slot < this.inventory.getSize(); slot++) {
			inventory.setItem(slot, this.inventory.getItem(slot));
		}
		
		this.inventory = inventory;
		
		markDirty();
	}

	public void save() {
		for (int i = 0; i < size; i++) {
			cfg.setValue("backpacks." + id + ".contents." + i, inventory.getItem(i));
		}
	}

	public void markDirty() {
		profile.markDirty();
	}

}
