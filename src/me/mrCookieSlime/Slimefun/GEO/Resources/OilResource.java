package me.mrCookieSlime.Slimefun.GEO.Resources;

import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;

public class OilResource implements OreGenResource {
	
	@Override
	public int getDefaultSupply(Biome biome) {
		switch (biome) {
		case SNOWY_BEACH:
		case STONE_SHORE:
		case BEACH: {
			return CSCoreLib.randomizer().nextInt(6) + 2;
		}

		case DESERT:
		case DESERT_HILLS:
		case DESERT_LAKES: {
			return CSCoreLib.randomizer().nextInt(40) + 19;
		}

		case MOUNTAINS:
		case GRAVELLY_MOUNTAINS:
		case MOUNTAIN_EDGE:
		case RIVER: {
			return CSCoreLib.randomizer().nextInt(14) + 13;
		}

		case SNOWY_MOUNTAINS:
		case SNOWY_TUNDRA:
		case ICE_SPIKES:
		case FROZEN_OCEAN:
		case FROZEN_RIVER: {
			return CSCoreLib.randomizer().nextInt(11) + 3;
		}

		case THE_END:
		case NETHER: {
			return 0;
		}


		case BADLANDS:
		case BADLANDS_PLATEAU:
		case WOODED_BADLANDS_PLATEAU:
		case ERODED_BADLANDS:
		case MODIFIED_BADLANDS_PLATEAU:
		case MODIFIED_WOODED_BADLANDS_PLATEAU:
		case MUSHROOM_FIELDS:
		case MUSHROOM_FIELD_SHORE: {
			return CSCoreLib.randomizer().nextInt(24) + 14;
		}

		case DEEP_OCEAN:
		case OCEAN: {
			return CSCoreLib.randomizer().nextInt(62) + 24;
		}

		case SWAMP:
		case SWAMP_HILLS: {
			return CSCoreLib.randomizer().nextInt(20) + 4;
		}

		default: {
			return CSCoreLib.randomizer().nextInt(10) + 6;
		}
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
		return "Buckets";
	}

}
