package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.utils.compatibility.StackResolver;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Our {@link OreDictionary} implementation for MC 1.16 or higher.
 * 
 * @author TheBusyBiscuit
 *
 */
class OreDictionary16 implements OreDictionary {

    @Override
    @ParametersAreNonnullByDefault
    public @Nonnull ItemStack getDrops(Material material, Random random) {
        return switch (material) {
            case COAL_ORE -> StackResolver.of(Material.COAL);
            case DIAMOND_ORE -> StackResolver.of(Material.DIAMOND);
            case EMERALD_ORE -> StackResolver.of(Material.EMERALD);
            case REDSTONE_ORE -> StackResolver.of(Material.REDSTONE, 4 + random.nextInt(2));
            case LAPIS_ORE -> StackResolver.of(Material.LAPIS_LAZULI, 4 + random.nextInt(4));
            case NETHER_QUARTZ_ORE -> StackResolver.of(Material.QUARTZ);
            case IRON_ORE -> StackResolver.of(Material.IRON_ORE);
            case GOLD_ORE -> StackResolver.of(Material.GOLD_ORE);
            case NETHER_GOLD_ORE -> StackResolver.of(Material.GOLD_NUGGET, 2 + random.nextInt(4));
            case ANCIENT_DEBRIS -> StackResolver.of(Material.ANCIENT_DEBRIS);
            default -> StackResolver.of(material);
        };
    }

}
