package io.github.thebusybiscuit.slimefun4.core;

import io.github.thebusybiscuit.cscorelib2.collections.KeyMap;
import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlock;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.guide.BookSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.implementation.guide.CheatSheetSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.implementation.guide.ChestSlimefunGuide;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.api.BlockInfoConfig;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class houses a lot of instances of {@link Map} and {@link List} that hold
 * various mappings and collections related to {@link SlimefunItem}.
 *
 * @author TheBusyBiscuit
 *
 */
public class SlimefunRegistry {

    private final Map<String, SlimefunItem> slimefunIds = new HashMap<>();
    private final List<SlimefunItem> slimefunItems = new ArrayList<>();
    private final List<SlimefunItem> enabledItems = new ArrayList<>();

    private final List<Category> categories = new ArrayList<>();
    private final List<MultiBlock> multiblocks = new LinkedList<>();

    private final List<Research> researches = new LinkedList<>();
    private final List<String> researchRanks = new ArrayList<>();
    private final Set<UUID> researchingPlayers = new HashSet<>();

    private boolean backwardsCompatibility;
    private boolean automaticallyLoadItems;
    private boolean enableResearches;
    private boolean freeCreativeResearches;
    private boolean researchFireworks;
    private boolean useMoneyUnlock;

    private final Set<String> tickers = new HashSet<>();
    private final Set<SlimefunItem> radioactive = new HashSet<>();
    private final Set<String> activeChunks = new HashSet<>();

    private final KeyMap<GEOResource> geoResources = new KeyMap<>();

    private final Set<String> energyGenerators = new HashSet<>();
    private final Set<String> energyCapacitors = new HashSet<>();
    private final Set<String> energyConsumers = new HashSet<>();
    private final Set<String> chargeableBlocks = new HashSet<>();
    private final Map<String, WitherProof> witherProofBlocks = new HashMap<>();

    private final Map<UUID, PlayerProfile> profiles = new ConcurrentHashMap<>();
    private final Map<String, BlockStorage> worlds = new ConcurrentHashMap<>();
    private final Map<String, BlockInfoConfig> chunks = new HashMap<>();
    private final Map<SlimefunGuideLayout, SlimefunGuideImplementation> layouts = new EnumMap<>(SlimefunGuideLayout.class);
    private final Map<EntityType, Set<ItemStack>> drops = new EnumMap<>(EntityType.class);
    private final Map<String, Integer> capacities = new HashMap<>();
    private final Map<String, BlockMenuPreset> blockMenuPresets = new HashMap<>();
    private final Map<String, UniversalBlockMenu> universalInventories = new HashMap<>();
    private final Map<Class<? extends ItemHandler>, Set<ItemHandler>> itemHandlers = new HashMap<>();
    private final Map<String, SlimefunBlockHandler> blockHandlers = new HashMap<>();

    private final Map<String, Set<Location>> activeTickers = new HashMap<>();

    private final Map<String, ItemStack> automatedCraftingChamberRecipes = new HashMap<>();

    public void load(Config cfg) {
        boolean showVanillaRecipes = cfg.getBoolean("guide.show-vanilla-recipes");

        layouts.put(SlimefunGuideLayout.CHEST, new ChestSlimefunGuide(showVanillaRecipes));
        layouts.put(SlimefunGuideLayout.CHEAT_SHEET, new CheatSheetSlimefunGuide());
        layouts.put(SlimefunGuideLayout.BOOK, new BookSlimefunGuide());

        researchRanks.addAll(cfg.getStringList("research-ranks"));

        backwardsCompatibility = cfg.getBoolean("options.backwards-compatibility") || SlimefunPlugin.getMinecraftVersion().isBefore(MinecraftVersion.MINECRAFT_1_14);
        freeCreativeResearches = cfg.getBoolean("researches.free-in-creative-mode");
        researchFireworks = cfg.getBoolean("researches.enable-fireworks");

        useMoneyUnlock = cfg.getBoolean("researches.use-money-unlock");
    }

    public boolean isAutoLoadingEnabled() {
        return automaticallyLoadItems;
    }

    /**
     * This method returns whether backwards-compatibility is enabled.
     * Backwards compatibility allows Slimefun to recognize items from older versions but comes
     * at a huge performance cost.
     *
     * @return Whether backwards compatibility is enabled
     */
    public boolean isBackwardsCompatible() {
        return backwardsCompatibility;
    }

    public void setBackwardsCompatible(boolean compatible) {
        backwardsCompatibility = compatible;
    }

    public void setAutoLoadingMode(boolean mode) {
        automaticallyLoadItems = mode;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<SlimefunItem> getAllSlimefunItems() {
        return slimefunItems;
    }

    public List<SlimefunItem> getEnabledSlimefunItems() {
        return enabledItems;
    }

    public List<Research> getResearches() {
        return researches;
    }

    public Set<UUID> getCurrentlyResearchingPlayers() {
        return researchingPlayers;
    }

    public List<String> getResearchRanks() {
        return researchRanks;
    }

    public void setResearchingEnabled(boolean enabled) {
        enableResearches = enabled;
    }

    public boolean isResearchingEnabled() {
        return enableResearches;
    }

    public void setFreeCreativeResearchingEnabled(boolean enabled) {
        freeCreativeResearches = enabled;
    }

    public boolean isFreeCreativeResearchingEnabled() {
        return freeCreativeResearches;
    }

    public boolean isResearchFireworkEnabled() {
        return researchFireworks;
    }

    public List<MultiBlock> getMultiBlocks() {
        return multiblocks;
    }

    public SlimefunGuideImplementation getGuideLayout(SlimefunGuideLayout layout) {
        return layouts.get(layout);
    }

    public Map<EntityType, Set<ItemStack>> getMobDrops() {
        return drops;
    }

    public Set<ItemStack> getMobDrops(EntityType entity) {
        return drops.get(entity);
    }

    public Set<SlimefunItem> getRadioactiveItems() {
        return radioactive;
    }

    public Set<String> getTickerBlocks() {
        return tickers;
    }

    public Set<String> getActiveChunks() {
        return activeChunks;
    }

    public Map<String, SlimefunItem> getSlimefunItemIds() {
        return slimefunIds;
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

    public Map<Class<? extends ItemHandler>, Set<ItemHandler>> getPublicItemHandlers() {
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

    public KeyMap<GEOResource> getGEOResources() {
        return geoResources;
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

    public Map<String, WitherProof> getWitherProofBlocks() {
        return witherProofBlocks;
    }

    public boolean isUseMoneyUnlock() {
        return useMoneyUnlock;
    }
}