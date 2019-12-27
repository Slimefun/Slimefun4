package me.mrCookieSlime.Slimefun.utils;

import java.util.List;

import io.github.thebusybiscuit.cscorelib2.config.Config;

public final class ConfigCache {
	
	public final boolean printOutLoading;
	
	public boolean researchesEnabled;
	public final boolean researchesFreeInCreative;
	public final boolean researchFireworksEnabled;
	public final List<String> researchesTitles;
	
	public final int blocksInfoLoadingDelay;
	
	public final boolean guideShowVanillaRecipes;
	
	public final int emeraldEnchantsLimit;
	
	public final boolean legacyDustWasher;
	public final boolean legacyOreGrinder;
	public final boolean legacyOreWasher;
	
	public int smelteryFireBreakChance;
	
	public ConfigCache(Config cfg) {
		printOutLoading = cfg.getBoolean("options.print-out-loading");
		
		researchesFreeInCreative = cfg.getBoolean("options.allow-free-creative-research");
		researchesTitles = cfg.getStringList("research-ranks");
		researchFireworksEnabled = cfg.getBoolean("options.research-unlock-fireworks");
		
		blocksInfoLoadingDelay = cfg.getInt("URID.info-delay");
		
		guideShowVanillaRecipes = cfg.getBoolean("options.show-vanilla-recipes-in-guide");
		
		emeraldEnchantsLimit = cfg.getInt("options.emerald-enchantment-limit");
		
		legacyDustWasher = cfg.getBoolean("options.legacy-dust-washer");
		legacyOreWasher = cfg.getBoolean("options.legacy-ore-washer");
		legacyOreGrinder = cfg.getBoolean("options.legacy-ore-grinder");
	}

}
