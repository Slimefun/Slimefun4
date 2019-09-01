package me.mrCookieSlime.Slimefun.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Setup.PostSlimefunLoadingHandler;
import me.mrCookieSlime.Slimefun.ancient_altar.AltarRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.GuideHandler;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.CargoTransportEvent;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemRequest;
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
	
	public int vanillaItems = 0;

	public Set<ItemStack> radioactiveItems = new HashSet<>();
	public Map<String, Set<ItemHandler>> itemHandlers = new HashMap<>();
	public Map<String, SlimefunBlockHandler> blockHandlers = new HashMap<>();
	public Set<String> tickers = new HashSet<>();
	
	public Map<String, Integer> blocksEnergyCapacity = new HashMap<>();
	public Set<String> rechargeableItems = new HashSet<>();
	public Set<String> capacitorIDs = new HashSet<>();
	
	public Set<String> energyNetInput = new HashSet<>();
	public Set<String> energyNetStorage = new HashSet<>();
	public Set<String> energyNetOutput = new HashSet<>();
	
	public Map<Location, Integer> roundRobin = new HashMap<>();
	public Set<ItemRequest> itemRequests = new HashSet<>();

	public Map<String, BlockMenuPreset> blockMenuPresets = new HashMap<>();
	
	public List<Category> currentlyEnabledCategories = new ArrayList<>();
	
	public Map<String, BlockStorage> worlds = new HashMap<>();
	public Set<String> loadedTickers = new HashSet<>();
	
	public Map<String, String> mapChunks = new HashMap<>();
	public Map<String, Set<Location>> tickingChunks = new HashMap<>();
	public Map<String, UniversalBlockMenu> universalInventories = new HashMap<>();
	
	public Map<UUID, PlayerProfile> profiles = new HashMap<>();
	
	public Map<Integer, List<GuideHandler>> guideHandlers = new HashMap<>();
	public List<PostSlimefunLoadingHandler> postHandlers = new ArrayList<>();
	
	public Map<EntityType, List<ItemStack>> drops = new EnumMap<>(EntityType.class);
	
	public Map<UUID, Boolean> jumpState = new HashMap<>();
	public Set<UUID> damage = new HashSet<>();
	public Map<UUID, Entity[]> remove = new HashMap<>();
	public Map<UUID, Integer> mode = new HashMap<>();
	
	public Map<UUID, Integer> enchanting = new HashMap<>();
	public Map<UUID, ItemStack> backpack = new HashMap<>();
	
	public Set<Location> altarinuse = new HashSet<>();
	public Set<AltarRecipe> altarRecipes = new HashSet<>();
	
	public Map<UUID, Map<Integer, ItemStack>> soulbound = new HashMap<>();
	public List<UUID> blocks = new ArrayList<>();
	public List<UUID> cancelPlace = new ArrayList<>();
	public Map<UUID, ItemStack> arrows = new HashMap<>();
	
	public Set<UUID> elevatorUsers = new HashSet<>();
	public Set<UUID> teleporterUsers = new HashSet<>();
	
	public Map<String, OreGenResource> resources = new HashMap<>();
	
	public Set<GitHubConnector> connectors = new HashSet<>();
	public Map<String, String> contributorHeads = new HashMap<>();
	public List<Contributor> contributors = new ArrayList<>();

	public Map<UUID, List<Object>> guideHistory = new HashMap<>();
	
	public List<CargoTransportEvent> cargoTransportEvents = new ArrayList<>();
	
	/**
	 * Contains all the players (UUIDs) that are currently unlocking a research.
	 * @since 4.0
	 */
	public Set<UUID> researching = new HashSet<>();
	
	/**
	 * Represents the current month of the year
	 */
	public int month = 0;
	
	/**
	 * Represents the number of unresolved Issues on the Slimefun4 GitHub repository.
	 * @since 4.1.13
	 */
	public int issues = 0;
	
	/**
	 * Represents the number of Forks of the Slimefun4 GitHub repository.
	 * @since 4.1.13
	 */
	public int forks = 0;
	
	/**
	 * Represents the number of stars on the Slimefun4 GitHub repository.
	 * @since 4.1.13
	 */
	public int stars = 0;
	
	public int codeBytes = 0;
	public Date lastUpdate = new Date();

}
