package me.mrCookieSlime.Slimefun.api.inventory;

import java.io.File;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlockMenu extends ChestMenu {
	
	BlockMenuPreset preset;
	Location l;
	
	public int changes = 0;
	
	private ItemManipulationEvent event;
	
	private static String serializeLocation(Location l) {
		return l.getWorld().getName() + ";" + l.getBlockX() + ";" + l.getBlockY() + ";" + l.getBlockZ();
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
		
		if (preset.getSize() > -1 && !preset.getPresetSlots().contains(preset.getSize() - 1)) {
			if (cfg.contains(String.valueOf(preset.getSize() - 1))) addItem(preset.getSize() - 1, cfg.getItem(String.valueOf(preset.getSize() - 1)));
		}
		
		this.getContents();
	}
	
	public void registerEvent(ItemManipulationEvent event) {
		this.event = event;
	}
	
	public void save(Location l) {
		if (changes == 0) return;
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
	
	public void move(Block b) {
		this.delete(this.l);
		this.l = b.getLocation();
		this.preset.newInstance(this, b);
		this.save(b.getLocation());
	}
	
	public Block getBlock() {
		return this.l.getBlock();
	}

	public Location getLocation() {
		return l;
	}
	
	public void delete(Location l) {
		new File("data-storage/Slimefun/stored-inventories/" + serializeLocation(l) + ".sfi").delete();
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
		super.replaceExistingItem(slot, item);
		
		if (event && this.event != null) this.event.onEvent(slot, previous, item);
		changes++;
	}
	
	public void close() {
		for (HumanEntity human: toInventory().getViewers()) {
			human.closeInventory();
		}
	}
	
}
