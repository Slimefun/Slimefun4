package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Our {@link OreDictionary} implementation for MC 1.16 or higher.
 * 
 * @author TheBusyBiscuit
 *
 */
class OreDictionary16 extends OreDictionary14 {

    @Override
    @ParametersAreNonnullByDefault
    public @Nonnull ItemStack getDrops(Material material, Random random) {
        return switch (material) {
            // In 1.16, breaking nether gold ores should get gold nuggets
            case NETHER_GOLD_ORE -> new ItemStack(Material.GOLD_NUGGET, 2 + random.nextInt(4));
            case ANCIENT_DEBRIS -> new ItemStack(Material.ANCIENT_DEBRIS);
            default -> super.getDrops(material, random);
        };
    }

}
