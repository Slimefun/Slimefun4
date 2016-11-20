package me.mrCookieSlime.Slimefun.api.inventory;

import java.io.File;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UniversalBlockMenu extends ChestMenu {
	
	BlockMenuPreset preset;
	ItemManipulationEvent event;
	
	public int changes = 0;
	
	public UniversalBlockMenu(BlockMenuPreset preset) {
		super(preset.getTitle());
		this.preset = preset;
		changes = 1;
		
		preset.clone(this);
		
		save();
	}
	
	public UniversalBlockMenu(BlockMenuPreset preset, Config cfg) {
		super(preset.getTitle());
		this.preset = preset;
		
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
	
	public void save() {
		if (changes == 0) return;
		// To force CS-CoreLib to build the Inventory
		this.getContents();
		
		File file = new File("data-storage/Slimefun/universal-inventories/" + preset.getID() + ".sfi");
		Config cfg = new Config(file);
		cfg.setValue("preset", preset.getID());
		for (int slot: preset.getInventorySlots()) {
			cfg.setValue(String.valueOf(slot), getItemInSlot(slot));
		}
		cfg.save();
		
		changes = 0;
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
