package me.mrCookieSlime.Slimefun.utils;

import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;

public final class Settings {
	
	public boolean researchesEnabled;
	public boolean researchesFreeInCreative;
	public List<String> researchesTitles;
	
	public int blocksInfoLoadingDelay;
	public int blocksAutoSaveDelay;
	
	public boolean guideShowVanillaRecipes;
	
	public int emeraldEnchantsLimit;
	
	public boolean legacyDustWasher;
	public boolean legacyOreGrinder;
	public boolean legacyOreWasher;
	
	public int SMELTERY_FIRE_BREAK_CHANCE;
	
	public Settings(Config cfg) {
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
