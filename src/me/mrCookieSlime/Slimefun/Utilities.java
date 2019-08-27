package me.mrCookieSlime.Slimefun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

/**
 * Really dirty way to store stuff, but you can dump
 * some Objects into here that need to be used throughout
 * multiple Classes.
 * 
 * @author TheBusyBiscuit
 *
 */
public final class Utilities {
	
	public Map<UUID, Boolean> jump_state = new HashMap<>();
	public Set<UUID> damage = new HashSet<>();
	public Map<UUID, Entity[]> remove = new HashMap<>();
	public Map<UUID, Integer> mode = new HashMap<>();
	
	public Map<UUID, Integer> enchanting = new HashMap<>();
	public Map<UUID, ItemStack> backpack = new HashMap<>();
	
	public Set<Location> altarinuse = new HashSet<>();
	
	public Map<UUID, List<ItemStack>> soulbound = new HashMap<>();
	public List<UUID> blocks = new ArrayList<>();
	public List<UUID> cancelPlace = new ArrayList<>();
	public Map<UUID, ItemStack> arrows = new HashMap<>();

}
