package io.github.thebusybiscuit.slimefun4.implementation.geo.resources;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;

public class OilResource implements OreGenResource {
	
	@Override
	public int getDefaultSupply(Biome biome) {
		Random random = ThreadLocalRandom.current();
		
		switch (biome) {
		case SNOWY_BEACH:
		case STONE_SHORE:
		case BEACH:
			return random.nextInt(6) + 2;

		case DESERT:
		case DESERT_HILLS:
		case DESERT_LAKES: 
			return random.nextInt(40) + 19;

		case MOUNTAINS:
		case GRAVELLY_MOUNTAINS:
		case MOUNTAIN_EDGE:
		case RIVER: 
			return random.nextInt(14) + 13;

		case SNOWY_MOUNTAINS:
		case SNOWY_TUNDRA:
		case ICE_SPIKES:
		case FROZEN_OCEAN:
		case FROZEN_RIVER: 
			return random.nextInt(11) + 3;

		case THE_END:
		case END_BARRENS:
		case END_MIDLANDS:
		case SMALL_END_ISLANDS:
		case NETHER:
			return 0;

		case BADLANDS:
		case BADLANDS_PLATEAU:
		case WOODED_BADLANDS_PLATEAU:
		case ERODED_BADLANDS:
		case MODIFIED_BADLANDS_PLATEAU:
		case MODIFIED_WOODED_BADLANDS_PLATEAU:
		case MUSHROOM_FIELDS:
		case MUSHROOM_FIELD_SHORE:
			return random.nextInt(24) + 14;

		case DEEP_OCEAN:
		case OCEAN:
		case COLD_OCEAN:
		case DEEP_COLD_OCEAN:
		case DEEP_FROZEN_OCEAN:
		case DEEP_LUKEWARM_OCEAN:
		case DEEP_WARM_OCEAN:
		case LUKEWARM_OCEAN:
		case WARM_OCEAN:
			return random.nextInt(62) + 24;

		case SWAMP:
		case SWAMP_HILLS:
			return random.nextInt(20) + 4;

		default:
			return random.nextInt(10) + 6;
		}
	}

	@Override
	public String getName() {
		return "Oil";
	}

	@Override
	public ItemStack getIcon() {
		return SlimefunItems.BUCKET_OF_OIL.clone();
	}

	@Override
	public String getMeasurementUnit() {
		return "Bucket(s)";
	}

	@Override
	public boolean isLiquid() {
		return true;
	}

}
