package me.mrCookieSlime.Slimefun.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

	public final Map<UUID, Boolean> jumpState = new HashMap<>();
	public final Set<UUID> damage = new HashSet<>();
	public final Map<UUID, Entity[]> remove = new HashMap<>();
	
	public final List<UUID> blocks = new ArrayList<>();
	public final List<UUID> cancelPlace = new ArrayList<>();

	public ItemStack[] oreWasherOutputs;

}
