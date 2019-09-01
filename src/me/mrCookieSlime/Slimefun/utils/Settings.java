package me.mrCookieSlime.Slimefun.utils;

import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;

public final class Settings {
	
	public final boolean printOutLoading;
	
	public boolean researchesEnabled;
	public final boolean researchesFreeInCreative;
	public final List<String> researchesTitles;
	
	public final int blocksInfoLoadingDelay;
	public final int blocksAutoSaveDelay;
	
	public final boolean guideShowVanillaRecipes;
	
	public final int emeraldEnchantsLimit;
	
	public final boolean legacyDustWasher;
	public final boolean legacyOreGrinder;
	public final boolean legacyOreWasher;
	
	public int smelteryFireBreakChance;
	
	public Settings(Config cfg) {
		printOutLoading = cfg.getBoolean("options.print-out-loading");
		
		researchesFreeInCreative = cfg.getBoolean("options.allow-free-creative-research");
		researchesTitles = cfg.getStringList("research-ranks");
		
		blocksInfoLoadingDelay = cfg.getInt("URID.info-delay");
		blocksAutoSaveDelay = cfg.getInt("options.auto-save-delay-in-minutes");
		
		guideShowVanillaRecipes = cfg.getBoolean("options.show-vanilla-recipes-in-guide");
		
		emeraldEnchantsLimit = cfg.getInt("options.emerald-enchantment-limit");
		
		legacyDustWasher = cfg.getBoolean("options.legacy-dust-washer");
		legacyOreWasher = cfg.getBoolean("options.legacy-ore-washer");
		legacyOreGrinder = cfg.getBoolean("options.legacy-ore-grinder");
	}

}
