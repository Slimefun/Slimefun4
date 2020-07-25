package io.github.thebusybiscuit.slimefun4.implementation.resources;

import org.bukkit.NamespacedKey;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class OilResource implements GEOResource {

    private final NamespacedKey key = new NamespacedKey(SlimefunPlugin.instance(), "oil");

    @Override
    public int getDefaultSupply(Environment environment, Biome biome) {

        if (environment != Environment.NORMAL) {
            return 0;
        }

        switch (biome) {
        case SNOWY_BEACH:
        case STONE_SHORE:
        case BEACH:
            return 6;

        case DESERT:
        case DESERT_HILLS:
        case DESERT_LAKES:
            return 45;

        case MOUNTAINS:
        case GRAVELLY_MOUNTAINS:
        case MOUNTAIN_EDGE:
        case RIVER:
            return 17;

        case SNOWY_MOUNTAINS:
        case SNOWY_TUNDRA:
        case ICE_SPIKES:
        case FROZEN_OCEAN:
        case FROZEN_RIVER:
            return 14;

        case BADLANDS:
        case BADLANDS_PLATEAU:
        case WOODED_BADLANDS_PLATEAU:
        case ERODED_BADLANDS:
        case MODIFIED_BADLANDS_PLATEAU:
        case MODIFIED_WOODED_BADLANDS_PLATEAU:
        case MUSHROOM_FIELDS:
        case MUSHROOM_FIELD_SHORE:
            return 24;

        case DEEP_OCEAN:
        case OCEAN:
        case COLD_OCEAN:
        case DEEP_COLD_OCEAN:
        case DEEP_FROZEN_OCEAN:
        case DEEP_LUKEWARM_OCEAN:
        case DEEP_WARM_OCEAN:
        case LUKEWARM_OCEAN:
        case WARM_OCEAN:
            return 62;

        case SWAMP:
        case SWAMP_HILLS:
            return 20;

        default:
            return 10;
        }
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public int getMaxDeviation() {
        return 8;
    }

    @Override
    public String getName() {
        return "Oil";
    }

    @Override
    public ItemStack getItem() {
        return SlimefunItems.OIL_BUCKET.clone();
    }

    @Override
    public boolean isObtainableFromGEOMiner() {
        return false;
    }

}
