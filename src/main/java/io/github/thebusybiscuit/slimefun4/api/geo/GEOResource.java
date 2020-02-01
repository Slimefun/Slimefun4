package io.github.thebusybiscuit.slimefun4.api.geo;

import org.bukkit.Chunk;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

public interface GEOResource {
	
	/**
	 *  Returns the default supply of this resource in that biome
	 *  
	 *  @return The default supply found in a {@link Chunk} with the given {@link Biome}
	 */
	int getDefaultSupply(Biome biome);
	
	/**
	 *  Name/ID e.g. "Oil"
	 *  
	 *  @return	The name of this Resource
	 */
	String getName();
	
	/**
	 * This {@link ItemStack} is used for display-purposes in the GEO Scanner.
	 * But will also determine the Output of the GEO Miner, if it is applicable for that.
	 * 
	 * @return The {@link ItemStack} version of this Resource.
	 */
	ItemStack getItem();
	
	/** 
	 * Measurement Unit e.g. "Bucket" / "Buckets".
	 * Use the amount parameter to determine whether to use singular or plural.
	 * 
	 * @return	The Measurement Unit for this resource, will be treated like a suffix.
	 */
	String getMeasurementUnit(int amount);
	
	/**
	 * Returns whether this Resource can be obtained using a GEO Miner.
	 * This will automatically add it to the GEO - Miner.
	 * 
	 * @return Whether you can get obtain this resource using a GEO Miner.
	 */
	boolean isObtainableFromGEOMiner();

}
