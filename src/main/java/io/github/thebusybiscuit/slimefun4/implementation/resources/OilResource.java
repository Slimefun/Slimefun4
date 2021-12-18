package io.github.thebusybiscuit.slimefun4.implementation.resources;

import org.bukkit.World.Environment;
import org.bukkit.block.Biome;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOMiner;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.OilPump;
import io.github.thebusybiscuit.slimefun4.utils.biomes.BiomeMap;

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
class OilResource extends AbstractResource {

    private static final int DEFAULT_OVERWORLD_VALUE = 10;

    private final BiomeMap<Integer> biomes;

    OilResource() {
        super("oil", "Oil", SlimefunItems.OIL_BUCKET, 8, false);

        MinecraftVersion version = Slimefun.getMinecraftVersion();

        if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_18)) {
            // 1.18+ renamed most biomes
            biomes = getBiomeMap(this, "/biome-maps/oil_v1.18.json");
        } else {
            biomes = getBiomeMap(this, "/biome-maps/oil_v1.14.json");
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
