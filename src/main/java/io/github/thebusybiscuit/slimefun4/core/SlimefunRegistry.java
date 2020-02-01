package io.github.thebusybiscuit.slimefun4.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.core.guide.BookSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.ChestSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.ISlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.MultiBlock;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Setup.PostSlimefunLoadingHandler;
import me.mrCookieSlime.Slimefun.api.BlockInfoConfig;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.GuideHandler;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

public class SlimefunRegistry {

	private final Map<String, SlimefunItem> slimefunIds = new HashMap<>();
	private final List<SlimefunItem> slimefunItems = new ArrayList<>();
	private final List<SlimefunItem> enabledItems = new ArrayList<>();

	private final List<Category> categories = new ArrayList<>();
	private final List<Research> researches = new LinkedList<>();
	private final List<MultiBlock> multiblocks = new LinkedList<>();
	
	private final Set<String> tickers = new HashSet<>();
	private final Set<ItemStack> radioactive = new HashSet<>();
	private final Set<String> activeChunks = new HashSet<>();
	private final Set<UUID> researchingPlayers = new HashSet<>();

	private final Map<String, OreGenResource> geoResources = new HashMap<>();
	private final Map<String, Config> geoResourcesConfigs = new HashMap<>();
	
	private final Set<String> energyGenerators = new HashSet<>();
	private final Set<String> energyCapacitors = new HashSet<>();
	private final Set<String> energyConsumers = new HashSet<>();
	private final Set<String> chargeableBlocks = new HashSet<>();

	private final Map<String, BlockStorage> worlds = new HashMap<>();
	private final Map<String, BlockInfoConfig> chunks = new HashMap<>();
	private final Map<UUID, PlayerProfile> profiles = new HashMap<>();
	private final Map<SlimefunGuideLayout, ISlimefunGuide> layouts = new EnumMap<>(SlimefunGuideLayout.class);
	private final Map<EntityType, Set<ItemStack>> drops = new EnumMap<>(EntityType.class);
	private final Map<String, Integer> capacities = new HashMap<>();
	private final Map<String, BlockMenuPreset> blockMenuPresets = new HashMap<>();
	private final Map<String, UniversalBlockMenu> universalInventories = new HashMap<>();
	private final Map<Class<? extends ItemHandler>, Set<ItemHandler>> itemHandlers = new HashMap<>();
	private final Map<String, SlimefunBlockHandler> blockHandlers = new HashMap<>();

	private final Map<String, Set<Location>> activeTickers = new HashMap<>();
	
	private final Map<Integer, List<GuideHandler>> guideHandlers = new HashMap<>();
	private final List<PostSlimefunLoadingHandler> postHandlers = new ArrayList<>();
	private final Map<String, ItemStack> automatedCraftingChamberRecipes = new HashMap<>();

	public SlimefunRegistry() {
		ISlimefunGuide chestGuide = new ChestSlimefunGuide();
		layouts.put(SlimefunGuideLayout.CHEST, chestGuide);
		layouts.put(SlimefunGuideLayout.CHEAT_SHEET, chestGuide);
		layouts.put(SlimefunGuideLayout.BOOK, new BookSlimefunGuide());
	}
	
	public List<Category> getEnabledCategories() {
		return categories;
	}
	
	public List<SlimefunItem> getAllSlimefunItems() {
		return slimefunItems;
	}
	
	public List<SlimefunItem> getEnabledSlimefunItems() {
		return enabledItems;
	}

	public int countVanillaItems() {
		return (int) getEnabledSlimefunItems().stream().filter(item -> !item.isAddonItem()).count();
	}
	
	public List<Research> getResearches() {
		return researches;
	}
	
	public List<MultiBlock> getMultiBlocks() {
		return multiblocks;
	}
	
	public ISlimefunGuide getGuideLayout(SlimefunGuideLayout layout) {
		return layouts.get(layout);
	}
	
	public Set<ItemStack> getMobDrops(EntityType entity) {
		return drops.get(entity);
	}

	public Set<ItemStack> getRadioactiveItems() {
		return radioactive;
	}
	
	public Set<String> getTickerBlocks() {
		return tickers;
	}
	
	public Set<String> getActiveChunks() {
		return activeChunks;
	}
	
	public Set<UUID> getCurrentlyResearchingPlayers() {
		return researchingPlayers;
	}
	
	public Map<String, SlimefunItem> getSlimefunItemIds() {
		return slimefunIds;
	}
	
	public Map<EntityType, Set<ItemStack>> getMobDrops() {
		return drops;
	}
	
	public Map<String, Integer> getEnergyCapacities() {
		return capacities;
	}

	public Map<String, BlockMenuPreset> getMenuPresets() {
		return blockMenuPresets;
	}

	public Map<String, UniversalBlockMenu> getUniversalInventories() {
		return universalInventories;
	}
	
	public Map<UUID, PlayerProfile> getPlayerProfiles() {
		return profiles;
	}
	
	public Map<Class<? extends ItemHandler>, Set<ItemHandler>> getItemHandlers() {
		return itemHandlers;
	}
	
	public Map<String, SlimefunBlockHandler> getBlockHandlers() {
		return blockHandlers;
	}
	
	public Map<String, BlockStorage> getWorlds() {
		return worlds;
	}
	
	public Map<String, BlockInfoConfig> getChunks() {
		return chunks;
	}
	
	public Map<String, Set<Location>> getActiveTickers() {
		return activeTickers;
	}
	
	public Map<String, OreGenResource> getGEOResources() {
		return geoResources;
	}
	
	public Map<String, Config> getGEOResourceConfigs() {
		return geoResourcesConfigs;
	}
	
	@Deprecated
	public Map<Integer, List<GuideHandler>> getGuideHandlers() {
		return guideHandlers;
	}
	
	@Deprecated
	public List<PostSlimefunLoadingHandler> getPostHandlers() {
		return postHandlers;
	}
	
	@Deprecated
	public Map<String, ItemStack> getAutomatedCraftingChamberRecipes() {
		return automatedCraftingChamberRecipes;
	}
	
	public Set<String> getEnergyGenerators() {
		return energyGenerators;
	}
	
	public Set<String> getEnergyCapacitors() {
		return energyCapacitors;
	}
	
	public Set<String> getEnergyConsumers() {
		return energyConsumers;
	}
	
	public Set<String> getChargeableBlocks() {
		return chargeableBlocks;
	}
	
}
