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
		case COLD_BEACH:
		case STONE_BEACH:
		case BEACHES: {
			return CSCoreLib.randomizer().nextInt(6) + 2;
		}

		case DESERT:
		case DESERT_HILLS:
		case MUTATED_DESERT: {
			return CSCoreLib.randomizer().nextInt(40) + 19;
		}

		case EXTREME_HILLS:
		case MUTATED_EXTREME_HILLS:
		case SMALLER_EXTREME_HILLS:
		case RIVER: {
			return CSCoreLib.randomizer().nextInt(14) + 13;
		}

		case ICE_MOUNTAINS:
		case ICE_FLATS:
		case MUTATED_ICE_FLATS:
		case FROZEN_OCEAN:
		case FROZEN_RIVER: {
			return CSCoreLib.randomizer().nextInt(11) + 3;
		}

		case SKY:
		case HELL: {
			return 0;
		}


		case MESA:
		case MESA_CLEAR_ROCK:
		case MESA_ROCK:
		case MUTATED_MESA:
		case MUTATED_MESA_CLEAR_ROCK:
		case MUTATED_MESA_ROCK:
		case MUSHROOM_ISLAND:
		case MUSHROOM_ISLAND_SHORE: {
			return CSCoreLib.randomizer().nextInt(24) + 14;
		}

		case DEEP_OCEAN:
		case OCEAN: {
			return CSCoreLib.randomizer().nextInt(62) + 24;
		}

		case SWAMPLAND:
		case MUTATED_SWAMPLAND: {
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
