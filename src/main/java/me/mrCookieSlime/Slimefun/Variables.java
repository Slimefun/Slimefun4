package me.mrCookieSlime.Slimefun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class Variables {
	
	public static Map<UUID, Boolean> jump = new HashMap<UUID, Boolean>();
	public static Map<UUID, Boolean> damage = new HashMap<UUID, Boolean>();
	public static Map<UUID, Entity[]> remove = new HashMap<UUID, Entity[]>();
	public static Map<UUID, Integer> mode = new HashMap<UUID, Integer>();
	
	public static Map<UUID, Integer> enchanting = new HashMap<UUID, Integer>();
	public static Map<UUID, ItemStack> backpack = new HashMap<UUID, ItemStack>();
	
	public static Map<UUID, List<ItemStack>> soulbound = new HashMap<UUID, List<ItemStack>>();
	public static List<UUID> blocks = new ArrayList<UUID>();
	public static List<UUID> cancelPlace = new ArrayList<UUID>();
	public static Map<UUID, ItemStack> arrows = new HashMap<UUID, ItemStack>();

}
