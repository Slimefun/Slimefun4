package io.github.thebusybiscuit.slimefun4.implementation.resources;

import org.bukkit.World.Environment;
import org.bukkit.block.Biome;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.biomes.BiomeMap;

/**
 * A {@link GEOResource} which consists of nether ice.
 * It can only be found in the nether.
 * 
 * @author TheBusyBiscuit
 *
 */
class NetherIceResource extends AbstractResource {

    private static final int DEFAULT_NETHER_VALUE = 32;

    private final BiomeMap<Integer> biomes;

    NetherIceResource() {
        super("nether_ice", "Nether Ice", SlimefunItems.NETHER_ICE, 6, true);

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            // 1.16+ introduced Nether biomes
            biomes = getBiomeMap(this, "/biome-maps/nether_ice_v1.16.json");
        } else {
            biomes = getBiomeMap(this, "/biome-maps/nether_ice_v1.14.json");
        }
    }

    @Override
    public int getDefaultSupply(Environment environment, Biome biome) {
        if (environment != Environment.NETHER) {
            return 0;
        } else {
            return biomes.getOrDefault(biome, DEFAULT_NETHER_VALUE);
        }
    }

}
