package io.github.thebusybiscuit.slimefun4.implementation.resources;

import org.bukkit.World.Environment;
import org.bukkit.block.Biome;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

/**
 * A {@link GEOResource} which consists of nether ice.
 * It can only be found in the nether.
 * 
 * @author TheBusyBiscuit
 *
 */
class NetherIceResource extends SlimefunResource {

    NetherIceResource() {
        super("nether_ice", "Nether Ice", SlimefunItems.NETHER_ICE, 6, true);
    }

    @Override
    public int getDefaultSupply(Environment environment, Biome biome) {
        return environment == Environment.NETHER ? 32 : 0;
    }

}
