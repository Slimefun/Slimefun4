package me.mrCookieSlime.Slimefun.utils;

import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;

public final class Settings {
	
	public boolean RESEARCHES_ENABLED;
	public boolean RESEARCHES_FREE_IN_CREATIVE;
	public List<String> RESEARCHES_TITLES;
	
	public int BLOCK_LOADING_INFO_DELAY;
	public int BLOCK_AUTO_SAVE_DELAY;
	
	public boolean GUIDE_SHOW_VANILLA_RECIPES;
	
	public int EMERALD_ENCHANTS_LIMIT;
	
	public boolean DUST_WASHER_LEGACY;
	public boolean ORE_GRINDER_LEGACY;
	public boolean ORE_WASHER_LEGACY;
	
	public int SMELTERY_FIRE_BREAK_CHANCE;
	
	public Settings(Config cfg) {
		RESEARCHES_FREE_IN_CREATIVE = cfg.getBoolean("options.allow-free-creative-research");
		RESEARCHES_TITLES = cfg.getStringList("research-ranks");
		
		BLOCK_LOADING_INFO_DELAY = cfg.getInt("URID.info-delay");
		BLOCK_AUTO_SAVE_DELAY = cfg.getInt("options.auto-save-delay-in-minutes");
		
		GUIDE_SHOW_VANILLA_RECIPES = cfg.getBoolean("options.show-vanilla-recipes-in-guide");
		
		EMERALD_ENCHANTS_LIMIT = cfg.getInt("options.emerald-enchantment-limit");
		
		DUST_WASHER_LEGACY = cfg.getBoolean("options.legacy-dust-washer");
		ORE_WASHER_LEGACY = cfg.getBoolean("options.legacy-ore-washer");
		ORE_GRINDER_LEGACY = cfg.getBoolean("options.legacy-ore-grinder");
	}

}
