package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Simple interface to map ore blocks to their respective item(s).
 * 
 * @author TheBusyBiscuit
 *
 */
interface OreDictionary {

    @Nonnull
    @ParametersAreNonnullByDefault
    ItemStack forMaterial(Material material, Random random);
}
