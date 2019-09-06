package me.mrCookieSlime.Slimefun.api.inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class BlockMenu extends DirtyChestMenu {
	
	private BlockMenuPreset preset;
	private Location l;
	
	private ItemManipulationEvent event;
	
	private static String serializeLocation(Location l) {
		return l.getWorld().getName() + ';' + l.getBlockX() + ';' + l.getBlockY() + ';' + l.getBlockZ();
	}
	
	public BlockMenu(BlockMenuPreset preset, Location l) {
		super(preset.getTitle());
		this.preset = preset;
		this.l = l;
		changes = 1;
		
		preset.clone(this);
		
		this.getContents();
	}
	
	public BlockMenu(BlockMenuPreset preset, Location l, Config cfg) {
		super(preset.getTitle());
		this.preset = preset;
		this.l = l;
		
		for (int i = 0; i < 54; i++) {
			if (cfg.contains(String.valueOf(i))) addItem(i, cfg.getItem(String.valueOf(i)));
		}
		
		preset.clone(this);
		
		if (preset.getSize() > -1 && !preset.getPresetSlots().contains(preset.getSize() - 1) && cfg.contains(String.valueOf(preset.getSize() - 1))) addItem(preset.getSize() - 1, cfg.getItem(String.valueOf(preset.getSize() - 1)));
		
		this.getContents();
	}
	
	public void registerEvent(ItemManipulationEvent event) {
		this.event = event;
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
		for (int slot: preset.getInventorySlots()) {
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
		this.delete(this.l);
		this.l = l;
		this.preset.newInstance(this, l);
		this.save(l);
	}
	
	public Block getBlock() {
		return this.l.getBlock();
	}

	public Location getLocation() {
		return l;
	}
	
	public void delete(Location l) {
		File file = new File("data-storage/Slimefun/stored-inventories/" + serializeLocation(l) + ".sfi");
		
		if (file.exists() && !file.delete()) {
			Slimefun.getLogger().log(Level.WARNING, "Could not delete File: " + file.getName());
		}
	}
	
	public BlockMenuPreset getPreset() {
		return this.preset;
	}
	
	public boolean canOpen(Block b, Player p) {
		return this.preset.canOpen(b, p);
	}
	
	@Override
	public void replaceExistingItem(int slot, ItemStack item) {
		this.replaceExistingItem(slot, item, true);
	}
	
	public void replaceExistingItem(int slot, ItemStack item, boolean event) {
		final ItemStack previous = getItemInSlot(slot);
		
		if (event && this.event != null) {
			item = this.event.onEvent(slot, previous, item);
		}
		super.replaceExistingItem(slot, item);
		markDirty();
	}
	
	public void close() {
		for (HumanEntity human: new ArrayList<>(toInventory().getViewers())) {
			human.closeInventory();
		}
	}
}
