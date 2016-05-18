package me.mrCookieSlime.Slimefun.GEO;

import org.bukkit.block.Biome;

public interface OreGenResource {
	
	public int getDefaultSupply(Biome biome);
	public String getName();

}
