package me.mrCookieSlime.Slimefun.GEO;

import java.util.HashMap;
import java.util.Map;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.Chunk;
import org.bukkit.block.Biome;

public class OilFields {
	
	public static Map<Biome, Integer> defaults = new HashMap<Biome, Integer>();
	
	public static void init() {
		Config cfg = new Config("plugins/Slimefun/generators/oil_fields.cfg");
		for (Biome biome: Biome.values()) {
			switch (biome) {
			case COLD_BEACH:
			case STONE_BEACH:
			case BEACHES: {
				cfg.setDefaultValue(biome.toString(), CSCoreLib.randomizer().nextInt(6) + 2);
				break;
			}
			
			case DESERT:
			case DESERT_HILLS:
			case MUTATED_DESERT: {
				cfg.setDefaultValue(biome.toString(), CSCoreLib.randomizer().nextInt(40) + 19);
				break;
			}
			
			case EXTREME_HILLS:
			case MUTATED_EXTREME_HILLS:
			case SMALLER_EXTREME_HILLS:
			case RIVER: {
				cfg.setDefaultValue(biome.toString(), CSCoreLib.randomizer().nextInt(14) + 13);
				break;
			}
			
			case ICE_MOUNTAINS:
			case ICE_FLATS:
			case MUTATED_ICE_FLATS:
			case FROZEN_OCEAN:
			case FROZEN_RIVER: {
				cfg.setDefaultValue(biome.toString(), CSCoreLib.randomizer().nextInt(11) + 3);
				break;
			}
			
			case SKY:
			case HELL: {
				cfg.setDefaultValue(biome.toString(), 0);
				break;
			}
			
			
			case MESA:
			case MESA_CLEAR_ROCK:
			case MESA_ROCK:
			case MUTATED_MESA:
			case MUTATED_MESA_CLEAR_ROCK:
			case MUTATED_MESA_ROCK:
			case MUSHROOM_ISLAND:
			case MUSHROOM_ISLAND_SHORE: {
				cfg.setDefaultValue(biome.toString(), CSCoreLib.randomizer().nextInt(24) + 14);
				break;
			}

			case DEEP_OCEAN:
			case OCEAN: {
				cfg.setDefaultValue(biome.toString(), CSCoreLib.randomizer().nextInt(62) + 24);
				break;
			}
			
			case SWAMPLAND:
			case MUTATED_SWAMPLAND: {
				cfg.setDefaultValue(biome.toString(), CSCoreLib.randomizer().nextInt(20) + 4);
				break;
			}
			
			default: {
				cfg.setDefaultValue(biome.toString(), CSCoreLib.randomizer().nextInt(10) + 6);
				break;
			}
			}
			defaults.put(biome, cfg.getInt(biome.toString()));
		}
		cfg.save();
	}

	public static int getSupplies(Chunk chunk, boolean generate) {
		if (BlockStorage.hasChunkInfo(chunk, "resources_OIL")) return Integer.parseInt(BlockStorage.getChunkInfo(chunk, "resources_OIL"));
		else if (!generate) return 0;
		else {
			int supplies = defaults.get(chunk.getBlock(5, 50, 5).getBiome());
			BlockStorage.setChunkInfo(chunk, "resources_OIL", String.valueOf(supplies));
			return supplies;
		}
	}
	
	public static boolean isScanned(Chunk chunk) {
		return BlockStorage.hasChunkInfo(chunk, "resources_OIL");
	}

}
