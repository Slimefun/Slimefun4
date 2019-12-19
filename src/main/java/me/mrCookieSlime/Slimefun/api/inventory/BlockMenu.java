package me.mrCookieSlime.Slimefun.api.inventory;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.block.Block;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class BlockMenu extends DirtyChestMenu {
	
	private Location location;
	
	private static String serializeLocation(Location l) {
		return l.getWorld().getName() + ';' + l.getBlockX() + ';' + l.getBlockY() + ';' + l.getBlockZ();
	}
	
	public BlockMenu(BlockMenuPreset preset, Location l) {
		super(preset);
		this.location = l;

		preset.clone(this);
		this.getContents();
	}
	
	public BlockMenu(BlockMenuPreset preset, Location l, Config cfg) {
		super(preset);
		this.location = l;
		
		for (int i = 0; i < 54; i++) {
			if (cfg.contains(String.valueOf(i))) addItem(i, cfg.getItem(String.valueOf(i)));
		}
		
		preset.clone(this);
		
		if (preset.getSize() > -1 && !preset.getPresetSlots().contains(preset.getSize() - 1) && cfg.contains(String.valueOf(preset.getSize() - 1))) {
			addItem(preset.getSize() - 1, cfg.getItem(String.valueOf(preset.getSize() - 1)));
		}
		
		this.getContents();
	}
	
	public void save(Location l) {
		if (!isDirty()) {
			return;
		}
		
		// To force CS-CoreLib to build the Inventory
		this.getContents();
		
		File file = new File("data-storage/Slimefun/stored-inventories/" + serializeLocation(l) + ".sfi");
		Config cfg = new Config(file);
		cfg.setValue("preset", preset.getID());
		
		for (int slot : preset.getInventorySlots()) {
			cfg.setValue(String.valueOf(slot), getItemInSlot(slot));
		}
		
		cfg.save();
		
		changes = 0;
	}

	@Deprecated
	public void move(Block b) {
		move(b.getLocation());
	}

	public void move(Location l) {
		this.delete(this.location);
		this.location = l;
		this.preset.newInstance(this, l);
		this.save(l);
	}
	
	public Block getBlock() {
		return this.location.getBlock();
	}

	public Location getLocation() {
		return location;
	}
	
	public void delete(Location l) {
		File file = new File("data-storage/Slimefun/stored-inventories/" + serializeLocation(l) + ".sfi");
		
		if (file.exists() && !file.delete()) {
			Slimefun.getLogger().log(Level.WARNING, "Could not delete File: " + file.getName());
		}
	}
}
