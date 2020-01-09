package io.github.thebusybiscuit.slimefun4.implementation.geo.resources;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;

public class SaltResource implements OreGenResource {
	
	@Override
	public int getDefaultSupply(Biome biome) {
		Random random = ThreadLocalRandom.current();
		
		switch (biome) {
		case SNOWY_BEACH:
		case STONE_SHORE:
		case BEACH:
		case DESERT_LAKES:
		case RIVER:
		case ICE_SPIKES:
		case FROZEN_RIVER: 
			return random.nextInt(40) + 3;
		
		case DEEP_OCEAN:
		case OCEAN:
		case COLD_OCEAN:
		case DEEP_COLD_OCEAN:
		case DEEP_FROZEN_OCEAN:
		case DEEP_LUKEWARM_OCEAN:
		case DEEP_WARM_OCEAN:
		case FROZEN_OCEAN:
		case LUKEWARM_OCEAN:
		case WARM_OCEAN:
			return random.nextInt(60) + 24;

		case SWAMP:
		case SWAMP_HILLS:
			return random.nextInt(20) + 4;

		case THE_END:
		case END_BARRENS:
		case END_MIDLANDS:
		case SMALL_END_ISLANDS:
		case NETHER:
			return 0;

		default:
			return random.nextInt(6) + 4;
		}
	}

	@Override
	public String getName() {
		return "Salt";
	}

	@Override
	public ItemStack getIcon() {
		return SlimefunItems.SALT.clone();
	}

	@Override
	public String getMeasurementUnit() {
		return "Crystal(s)";
	}

	@Override
	public boolean isLiquid() {
		return false;
	}

}
