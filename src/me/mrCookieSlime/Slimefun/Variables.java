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

public class Variables {
	
	public static Map<UUID, Boolean> jump_state = new HashMap<>();
	public static Set<UUID> damage = new HashSet<>();
	public static Map<UUID, Entity[]> remove = new HashMap<>();
	public static Map<UUID, Integer> mode = new HashMap<>();
	
	public static Map<UUID, Integer> enchanting = new HashMap<>();
	public static Map<UUID, ItemStack> backpack = new HashMap<>();
	public static HashSet<Location> altarinuse = new HashSet<>();
	
	public static Map<UUID, List<ItemStack>> soulbound = new HashMap<>();
	public static List<UUID> blocks = new ArrayList<>();
	public static List<UUID> cancelPlace = new ArrayList<>();
	public static Map<UUID, ItemStack> arrows = new HashMap<>();

}
