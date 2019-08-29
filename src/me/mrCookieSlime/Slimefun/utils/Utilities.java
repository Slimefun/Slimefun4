package me.mrCookieSlime.Slimefun.utils;

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

import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.ancient_altar.AltarRecipe;
import me.mrCookieSlime.Slimefun.api.item_transport.CargoTransportEvent;
import me.mrCookieSlime.Slimefun.hooks.github.Contributor;
import me.mrCookieSlime.Slimefun.hooks.github.GitHubConnector;

/**
 * Really dirty way to store stuff, but you can dump
 * some Objects into here that need to be used throughout
 * multiple Classes.
 * 
 * @author TheBusyBiscuit
 *
 */
public final class Utilities {
	
	public Map<UUID, Boolean> jumpState = new HashMap<>();
	public Set<UUID> damage = new HashSet<>();
	public Map<UUID, Entity[]> remove = new HashMap<>();
	public Map<UUID, Integer> mode = new HashMap<>();
	
	public Map<UUID, Integer> enchanting = new HashMap<>();
	public Map<UUID, ItemStack> backpack = new HashMap<>();
	
	public Set<Location> altarinuse = new HashSet<>();
	public Set<AltarRecipe> altarRecipes = new HashSet<>();
	
	public Map<UUID, List<ItemStack>> soulbound = new HashMap<>();
	public List<UUID> blocks = new ArrayList<>();
	public List<UUID> cancelPlace = new ArrayList<>();
	public Map<UUID, ItemStack> arrows = new HashMap<>();
	
	public Set<UUID> elevatorUsers = new HashSet<>();
	public Set<UUID> teleporterUsers = new HashSet<>();
	
	public Map<String, OreGenResource> resources = new HashMap<>();
	
	public Set<GitHubConnector> connectors = new HashSet<>();
	public Map<String, String> contributorHeads = new HashMap<>();
	public List<Contributor> contributors = new ArrayList<>();
	
	public List<CargoTransportEvent> cargoTransportEvents = new ArrayList<>();
	
	/**
	 * Contains all the players (UUIDs) that are currently unlocking a research.
	 * @since 4.0
	 */
	public Set<UUID> researching = new HashSet<>();

}
