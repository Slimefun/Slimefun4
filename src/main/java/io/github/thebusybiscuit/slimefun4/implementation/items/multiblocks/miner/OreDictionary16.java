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
class OreDictionary16 implements OreDictionary {

    @Override
    @ParametersAreNonnullByDefault
    public @Nonnull ItemStack getDrops(Material material, Random random) {
        return switch (material) {
            case COAL_ORE -> ItemStack.of(Material.COAL);
            case DIAMOND_ORE -> ItemStack.of(Material.DIAMOND);
            case EMERALD_ORE -> ItemStack.of(Material.EMERALD);
            case REDSTONE_ORE -> ItemStack.of(Material.REDSTONE, 4 + random.nextInt(2));
            case LAPIS_ORE -> ItemStack.of(Material.LAPIS_LAZULI, 4 + random.nextInt(4));
            case NETHER_QUARTZ_ORE -> ItemStack.of(Material.QUARTZ);
            case IRON_ORE -> ItemStack.of(Material.IRON_ORE);
            case GOLD_ORE -> ItemStack.of(Material.GOLD_ORE);
            case NETHER_GOLD_ORE -> ItemStack.of(Material.GOLD_NUGGET, 2 + random.nextInt(4));
            case ANCIENT_DEBRIS -> ItemStack.of(Material.ANCIENT_DEBRIS);
            default -> ItemStack.of(material);
        };
    }

}
