package io.github.thebusybiscuit.slimefun4.implementation.resources;

import org.bukkit.World.Environment;
import org.bukkit.block.Biome;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.biomes.BiomeMap;

/**
 * A {@link GEOResource} which consists of small chunks of Uranium.
 * 
 * @author TheBusyBiscuit
 *
 */
class UraniumResource extends AbstractResource {

    private static final int DEFAULT_OVERWORLD_VALUE = 4;

    private final BiomeMap<Integer> biomes;

    UraniumResource() {
        super("uranium", "Small Chunks of Uranium", SlimefunItems.SMALL_URANIUM, 2, true);

        MinecraftVersion version = Slimefun.getMinecraftVersion();

        if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_18)) {
            // 1.18+ renamed most biomes
            biomes = getBiomeMap(this, "/biome-maps/uranium_v1.18.json");
        } else if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            // 1.17+ introduced cave biomes
            biomes = getBiomeMap(this, "/biome-maps/uranium_v1.17.json");
        } else if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            // 1.16+ introduced Nether biomes
            biomes = getBiomeMap(this, "/biome-maps/uranium_v1.16.json");
        } else {
            biomes = getBiomeMap(this, "/biome-maps/uranium_v1.14.json");
        }
    }

    @Override
    public int getDefaultSupply(Environment environment, Biome biome) {
        if (environment != Environment.NORMAL) {
            return 0;
        } else {
            return biomes.getOrDefault(biome, DEFAULT_OVERWORLD_VALUE);
        }
    }

}
