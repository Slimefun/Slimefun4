package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner;

import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.utils.multiversion.StackResolver;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Our {@link OreDictionary} implementation for MC 1.17 or higher.
 * 
 * @author TheBusyBiscuit
 *
 */
class OreDictionary17 extends OreDictionary16 {

    @Override
    @ParametersAreNonnullByDefault
    public ItemStack getDrops(Material material, Random random) {
        // In 1.17, breaking metal ores should get raw metals. Also support deepslate ores.
        return switch (material) {
            case COAL_ORE,
                DEEPSLATE_COAL_ORE -> StackResolver.of(Material.COAL);
            case DIAMOND_ORE,
                DEEPSLATE_DIAMOND_ORE -> StackResolver.of(Material.DIAMOND);
            case EMERALD_ORE,
                DEEPSLATE_EMERALD_ORE -> StackResolver.of(Material.EMERALD);
            case REDSTONE_ORE,
                DEEPSLATE_REDSTONE_ORE -> StackResolver.of(Material.REDSTONE, 4 + random.nextInt(2));
            case LAPIS_ORE,
                DEEPSLATE_LAPIS_ORE -> StackResolver.of(Material.LAPIS_LAZULI, 4 + random.nextInt(4));
            case COPPER_ORE,
                DEEPSLATE_COPPER_ORE -> StackResolver.of(Material.RAW_COPPER);
            case IRON_ORE,
                DEEPSLATE_IRON_ORE -> StackResolver.of(Material.RAW_IRON);
            case GOLD_ORE,
                DEEPSLATE_GOLD_ORE -> StackResolver.of(Material.RAW_GOLD);
            default -> super.getDrops(material, random);
        };
    }

}
