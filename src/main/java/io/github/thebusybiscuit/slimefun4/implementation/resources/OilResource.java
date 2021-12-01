package io.github.thebusybiscuit.slimefun4.implementation.resources;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOMiner;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.OilPump;

/**
 * A {@link GEOResource} which consists of buckets of Oil.
 * It cannot be obtained via a {@link GEOMiner} but instead requires
 * and {@link OilPump}.
 * 
 * @author TheBusyBiscuit
 *
 * @see OilPump
 *
 */
class OilResource extends SlimefunResource {

    OilResource() {
        super("oil", "Oil", SlimefunItems.OIL_BUCKET, 8, false);
    }

    @Override
    public int getDefaultSupply(Environment environment, Biome biome) {
        if (environment != Environment.NORMAL) {
            return 0;
        }

        // TODO: Think of a better way to support biomes.
        // Maybe worth making an issue/PR to Spigot in order to add the "biomes" registry.
        if (Slimefun.getMinecraftVersion().isBefore(MinecraftVersion.MINECRAFT_1_18)) {
            switch (biome.name()) {
                case "STONE_SHORE":
                    return 6;
                case "DESERT_HILLS":
                case "DESERT_LAKES":
                    return 45;
                case "MOUNTAINS":
                case "GRAVELLY_MOUNTAINS":
                case "MOUNTAIN_EDGE":
                    return 17;
                case "SNOWY_MOUNTAINS":
                case "SNOWY_TUNDRA":
                    return 14;
                case "BADLANDS_PLATEAU":
                case "WOODED_BADLANDS_PLATEAU":
                case "MODIFIED_BADLANDS_PLATEAU":
                case "MODIFIED_WOODED_BADLANDS_PLATEAU":
                case "MUSHROOM_FIELD_SHORE":
                    return 24;
                case "DEEP_WARM_OCEAN":
                    return 62;
                case "SWAMP_HILLS":
                    return 20;
                default:
                    break;
            }
        }

        switch (biome) {
            case SNOWY_BEACH:
            case STONY_SHORE:
            case BEACH:
                return 6;

            case DESERT:
                return 45;

            case RIVER:
                return 17;

            case ICE_SPIKES:
            case FROZEN_OCEAN:
            case FROZEN_RIVER:
                return 14;

            case BADLANDS:
            case ERODED_BADLANDS:
            case MUSHROOM_FIELDS:
                return 24;

            case DEEP_OCEAN:
            case OCEAN:
            case COLD_OCEAN:
            case DEEP_COLD_OCEAN:
            case DEEP_FROZEN_OCEAN:
            case DEEP_LUKEWARM_OCEAN:
            case LUKEWARM_OCEAN:
            case WARM_OCEAN:
                return 62;

            case SWAMP:
                return 20;

            default:
                return 10;
        }
    }
}
