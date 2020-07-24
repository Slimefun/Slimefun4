package io.github.thebusybiscuit.slimefun4.implementation.resources;

import org.bukkit.NamespacedKey;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class SaltResource implements GEOResource {

    private final NamespacedKey key = new NamespacedKey(SlimefunPlugin.instance(), "salt");

    @Override
    public int getDefaultSupply(Environment environment, Biome biome) {

        if (environment != Environment.NORMAL) {
            return 0;
        }

        switch (biome) {
        case SNOWY_BEACH:
        case STONE_SHORE:
        case BEACH:
        case DESERT_LAKES:
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
        case DEEP_WARM_OCEAN:
        case FROZEN_OCEAN:
        case LUKEWARM_OCEAN:
        case WARM_OCEAN:
            return 60;

        case SWAMP:
        case SWAMP_HILLS:
            return 20;

        default:
            return 6;
        }
    }

    @Override
    public int getMaxDeviation() {
        return 18;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public String getName() {
        return "Salt";
    }

    @Override
    public ItemStack getItem() {
        return SlimefunItems.SALT.clone();
    }

    @Override
    public boolean isObtainableFromGEOMiner() {
        return true;
    }

}
