package io.github.thebusybiscuit.slimefun4.implementation.resources;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

/**
 * A {@link GEOResource} which consists of Salt.
 * 
 * @author TheBusyBiscuit
 *
 */
class SaltResource extends SlimefunResource {

    SaltResource() {
        super("salt", "Salt", SlimefunItems.SALT, 18, true);
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
                case "DESERT_LAKES":
                    return 40;
                case "DEEP_WARM_OCEAN":
                    return 60;
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
            case RIVER:
            case ICE_SPIKES:
            case FROZEN_RIVER:
                return 40;

            case DEEP_OCEAN:
            case OCEAN:
            case COLD_OCEAN:
            case DEEP_COLD_OCEAN:
            case DEEP_FROZEN_OCEAN:
            case DEEP_LUKEWARM_OCEAN:
            case FROZEN_OCEAN:
            case LUKEWARM_OCEAN:
            case WARM_OCEAN:
                return 60;

            case SWAMP:
                return 20;

            default:
                return 6;
        }
    }

}
