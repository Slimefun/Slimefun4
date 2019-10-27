package me.mrCookieSlime.Slimefun.GEO;

import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

public interface OreGenResource {
	
	/**
	 *  Returns the default supply of this resource in that biome
	 */
	int getDefaultSupply(Biome biome);
	
	/**
	 *  Name/ID e.g. "Oil"
	 */
	String getName();
	
	@Deprecated
	ItemStack getIcon();
	
	default ItemStack getItem() {
		return getIcon();
	}
	
	/** 
	 * Measurement Unit e.g. "Buckets"
	 */
	String getMeasurementUnit();
	
	/**
	 * Returns whether this Resource is considered a fluid
	 */
	boolean isLiquid();

}
