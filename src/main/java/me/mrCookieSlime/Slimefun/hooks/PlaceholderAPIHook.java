package me.mrCookieSlime.Slimefun.hooks;

import java.util.Set;
import java.util.stream.Stream;

import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class PlaceholderAPIHook extends PlaceholderExpansion {

	@Override
	public String getAuthor() {
		return SlimefunPlugin.instance.getDescription().getAuthors().toString();
	}

	@Override
	public String getIdentifier() {
		return "slimefun";
	}

	@Override
	public String getVersion() {
		return SlimefunPlugin.instance.getDescription().getVersion();
	}
	
	@Override
	public boolean persist() {
		return true;
	}
	
	@Override
	public boolean canRegister() {
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String onRequest(OfflinePlayer p, String params) {
		if (params.equals("researches_total_xp_levels_spent")) {
			Stream<Research> stream = PlayerProfile.get(p).getResearches().stream();
			return String.valueOf(stream.mapToInt(Research::getCost).sum());
		}
		
		if (params.equals("researches_total_researches_unlocked")) {
			Set<Research> set = PlayerProfile.get(p).getResearches();
			return String.valueOf(set.size());
		}
		
		if (params.equals("researches_total_researches")) {
			return String.valueOf(Research.list());
		}
		
		if (params.equals("researches_percentage_researches_unlocked")) {
			Set<Research> set = PlayerProfile.get(p).getResearches();
			return String.valueOf(Math.round(((set.size() * 100.0F) / Research.list().size()) * 100.0F) / 100.0F);
		}
		
		if (params.equals("researches_title")) {
			return PlayerProfile.get(p).getTitle();
		}
		
		if (params.equals("gps_complexity")) {
			return String.valueOf(Slimefun.getGPSNetwork().getNetworkComplexity(p.getUniqueId()));
		}
		
		if (params.equals("timings_lag")) {
			return SlimefunPlugin.getTicker().getTime() + "ms";
		}
		
		return null;
	}

}
