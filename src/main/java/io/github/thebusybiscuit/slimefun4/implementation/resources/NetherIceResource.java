package io.github.thebusybiscuit.slimefun4.implementation.resources;

import org.bukkit.NamespacedKey;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class NetherIceResource implements GEOResource {

    private final NamespacedKey key = new NamespacedKey(SlimefunPlugin.instance(), "nether_ice");

    @Override
    public int getDefaultSupply(Environment environment, Biome biome) {
        return environment == Environment.NETHER ? 32 : 0;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public int getMaxDeviation() {
        return 6;
    }

    @Override
    public String getName() {
        return "Nether Ice";
    }

    @Override
    public ItemStack getItem() {
        return SlimefunItems.NETHER_ICE.clone();
    }

    @Override
    public boolean isObtainableFromGEOMiner() {
        return true;
    }

}
