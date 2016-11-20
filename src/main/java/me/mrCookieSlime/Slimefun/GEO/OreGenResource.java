package me.mrCookieSlime.Slimefun.GEO;

import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

public interface OreGenResource {
	
	// Returns the default supply of this resource in that biome
	public int getDefaultSupply(Biome biome);
	
	// Name/ID e.g. "Oil"
	public String getName();
	
	// For the GEO-Scanner
	public ItemStack getIcon();
	
	// Measurement Unit e.g. "Buckets"
	public String getMeasurementUnit();

}
