package me.mrCookieSlime.Slimefun.api.inventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public abstract class BlockMenuPreset extends ChestMenu {
	
	public static Map<String, BlockMenuPreset> presets = new HashMap<String, BlockMenuPreset>();
	
	private String title;
	private Set<Integer> occupied = new HashSet<Integer>();
	private String id;
	private int size = -1;
	private boolean universal;
	
	private ItemManipulationEvent event;
	
	public BlockMenuPreset(String id, String title) {
		super(title);
		this.id = id;
		this.title = title;
		this.init();
		this.universal = false;
		presets.put(id, this);
	}
	
	public void registerEvent(ItemManipulationEvent event) {
		this.event = event;
	}
	
	public BlockMenuPreset(String id, String title, boolean universal) {
		super(title);
		this.id = id;
		this.title = title;
		this.init();
		this.universal = universal;
		presets.put(id, this);
	}
	
	public abstract void init();
	public abstract void newInstance(BlockMenu menu, Block b);
	public abstract boolean canOpen(Block b, Player p);
	public abstract int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow);

	public int[] getSlotsAccessedByItemTransport(BlockMenu menu, ItemTransportFlow flow, ItemStack item) {
		return this.getSlotsAccessedByItemTransport(flow);
	}

	public int[] getSlotsAccessedByItemTransport(UniversalBlockMenu menu, ItemTransportFlow flow, ItemStack item) {
		return this.getSlotsAccessedByItemTransport(flow);
	}
	
	@Override
	public ChestMenu addItem(int slot, ItemStack item) {
		occupied.add(slot);
		return super.addItem(slot, item);
	}
	
	public ChestMenu setSize(int size) {
		this.size = size;
		return this;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public Set<Integer> getPresetSlots() {
		return occupied;
	}
	
	public Set<Integer> getInventorySlots() {
		Set<Integer> empty = new HashSet<Integer>();
		if (size > -1) {
			for (int i = 0; i < size; i++) {
				if (!occupied.contains(i)) empty.add(i);
			}
		}
		else {
			for (int i = 0; i < toInventory().getSize(); i++) {
				if (!occupied.contains(i)) empty.add(i);
			}
		}
		return empty;
	}
	
	public static BlockMenuPreset getPreset(String id) {
		return presets.get(id);
	}
	
	public static boolean isInventory(String id) {
		return presets.containsKey(id);
	}
	
	public static boolean isUniversalInventory(String id) {
		return presets.containsKey(id) && presets.get(id).isUniversal();
	}
	
	public boolean isUniversal() {
		return this.universal;
	}

	public void clone(final BlockMenu menu) {
		menu.setPlayerInventoryClickable(true);
		
		for (int slot: occupied) {
			menu.addItem(slot, getItemInSlot(slot));
		}
		
		if (size > -1) menu.addItem(size - 1, null);

		newInstance(menu, menu.getLocation());
		for (int slot = 0; slot < 54; slot++) {
			if (getMenuClickHandler(slot) != null) menu.addMenuClickHandler(slot, getMenuClickHandler(slot));
		}
		
		menu.addMenuOpeningHandler(getMenuOpeningHandler());
		menu.addMenuCloseHandler(getMenuCloseHandler());
		menu.registerEvent(event);
	}
	
	public void clone(UniversalBlockMenu menu) {
		menu.setPlayerInventoryClickable(true);
		
		for (int slot: occupied) {
			menu.addItem(slot, getItemInSlot(slot));
		}
		
		if (size > -1) menu.addItem(size - 1, null);
		for (int slot = 0; slot < 54; slot++) {
			if (getMenuClickHandler(slot) != null) menu.addMenuClickHandler(slot, getMenuClickHandler(slot));
		}
		
		menu.addMenuOpeningHandler(getMenuOpeningHandler());
		menu.addMenuCloseHandler(getMenuCloseHandler());
		menu.registerEvent(this.event);
	}

	public String getID() {
		return id;
	}
	
	public void newInstance(final BlockMenu menu, final Location l) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {
			public void run() {
				newInstance(menu, l.getBlock());
			}
		});
	}

}
