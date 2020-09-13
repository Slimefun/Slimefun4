package io.github.thebusybiscuit.slimefun4.implementation.resources;

import org.bukkit.World.Environment;
import org.bukkit.block.Biome;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

class UraniumResource extends SlimefunResource {

    UraniumResource() {
        super("uranium", "Small Chunks of Uranium", SlimefunItems.SMALL_URANIUM, 2, true);
    }

    @Override
    public int getDefaultSupply(Environment environment, Biome biome) {
        if (environment == Environment.NORMAL) {
            return 5;
        }

        return 0;
    }

}
