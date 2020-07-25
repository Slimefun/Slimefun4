package io.github.thebusybiscuit.slimefun4.implementation.resources;

import org.bukkit.NamespacedKey;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class UraniumResource implements GEOResource {

    private final NamespacedKey key = new NamespacedKey(SlimefunPlugin.instance(), "uranium");

    @Override
    public int getDefaultSupply(Environment envionment, Biome biome) {
        if (envionment == Environment.NORMAL) {
            return 5;
        }

        return 0;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public int getMaxDeviation() {
        return 2;
    }

    @Override
    public String getName() {
        return "Small Chunks of Uranium";
    }

    @Override
    public ItemStack getItem() {
        return SlimefunItems.SMALL_URANIUM.clone();
    }

    @Override
    public boolean isObtainableFromGEOMiner() {
        return true;
    }

}
