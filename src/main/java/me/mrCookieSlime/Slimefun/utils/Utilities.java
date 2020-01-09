package me.mrCookieSlime.Slimefun.utils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.core.guide.ISlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.MultiBlock;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Setup.PostSlimefunLoadingHandler;
import me.mrCookieSlime.Slimefun.ancient_altar.AltarRecipe;
import me.mrCookieSlime.Slimefun.api.BlockInfoConfig;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.GuideHandler;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.CargoTransportEvent;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemRequest;

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
	
	public final List<SlimefunItem> allItems = new ArrayList<>();
	public final List<SlimefunItem> enabledItems = new ArrayList<>();
	public final Map<String, SlimefunItem> itemIDs = new HashMap<>();
	
	public final List<MultiBlock> allMultiblocks = new LinkedList<>();
	public final List<Research> allResearches = new LinkedList<>();
	
	public final Map<SlimefunGuideLayout, ISlimefunGuide> guideLayouts = new EnumMap<>(SlimefunGuideLayout.class);
	
	/**
	 * List of the registered Categories.
	 * @since 4.0
	 * @see Categories
	 */
	public final List<Category> allCategories = new ArrayList<>();
	public final List<Category> enabledCategories = new ArrayList<>();
	public final CategorySorter categorySorter = new CategorySorter();
	
	public final Set<ItemStack> radioactiveItems = new HashSet<>();
	public final Map<String, Set<ItemHandler>> itemHandlers = new HashMap<>();
	public final Map<String, SlimefunBlockHandler> blockHandlers = new HashMap<>();
	public final Set<String> tickers = new HashSet<>();
	
	public final Map<String, Integer> blocksEnergyCapacity = new HashMap<>();
	public final Set<String> rechargeableItems = new HashSet<>();
	public final Set<String> capacitorIDs = new HashSet<>();
	
	public final Set<String> energyNetInput = new HashSet<>();
	public final Set<String> energyNetStorage = new HashSet<>();
	public final Set<String> energyNetOutput = new HashSet<>();
	
	public final Map<Location, Integer> roundRobin = new HashMap<>();
	public final Set<ItemRequest> itemRequests = new HashSet<>();

	public final Map<String, BlockMenuPreset> blockMenuPresets = new HashMap<>();
	
	public final Map<String, ItemStack> automatedCraftingChamberRecipes = new HashMap<>();
	
	public final Map<String, BlockStorage> worlds = new HashMap<>();
	public final Set<String> loadedTickers = new HashSet<>();
	
	public final Map<String, BlockInfoConfig> mapChunks = new HashMap<>();
	public final Map<String, Set<Location>> tickingChunks = new HashMap<>();
	public final Map<String, UniversalBlockMenu> universalInventories = new HashMap<>();
	
	public final Map<UUID, PlayerProfile> profiles = new HashMap<>();
	
	public final Map<Integer, List<GuideHandler>> guideHandlers = new HashMap<>();
	public final List<PostSlimefunLoadingHandler> postHandlers = new ArrayList<>();
	
	public final Map<EntityType, List<ItemStack>> drops = new EnumMap<>(EntityType.class);

	public final Map<UUID, ItemStack> arrows = new HashMap<>();
	public final Map<UUID, Boolean> jumpState = new HashMap<>();
	public final Set<UUID> damage = new HashSet<>();
	public final Map<UUID, Entity[]> remove = new HashMap<>();
	public final Map<UUID, Integer> mode = new HashMap<>();
	
	public final Map<UUID, Integer> enchanting = new HashMap<>();
	public final Map<UUID, ItemStack> backpack = new HashMap<>();
	
	public final Set<AltarRecipe> altarRecipes = new HashSet<>();
	
	public final List<UUID> blocks = new ArrayList<>();
	public final List<UUID> cancelPlace = new ArrayList<>();
	
	public final Set<UUID> elevatorUsers = new HashSet<>();
	public final Set<UUID> teleporterUsers = new HashSet<>();
	
	public final Map<String, OreGenResource> resources = new HashMap<>();
	public final Map<String, Config> resourceConfigs = new HashMap<>();
	
	public final Set<CargoTransportEvent> cargoTransportEvents = new HashSet<>();

	public ItemStack[] oreWasherOutputs;
	
	/**
	 * Contains all the players (UUIDs) that are currently unlocking a research.
	 * @since 4.0
	 */
	public final Set<UUID> researching = new HashSet<>();

}
