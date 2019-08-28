package me.mrCookieSlime.Slimefun.GEO;

import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

public interface OreGenResource {
	
	// Returns the default supply of this resource in that biome
	int getDefaultSupply(Biome biome);
	
	// Name/ID e.g. "Oil"
	String getName();
	
	// For the GEO-Scanner
	ItemStack getIcon();
	
	// Measurement Unit e.g. "Buckets"
	String getMeasurementUnit();

}
