package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner;

import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

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
                DEEPSLATE_COAL_ORE -> new ItemStack(Material.COAL);
            case DIAMOND_ORE,
                DEEPSLATE_DIAMOND_ORE -> new ItemStack(Material.DIAMOND);
            case EMERALD_ORE,
                DEEPSLATE_EMERALD_ORE -> new ItemStack(Material.EMERALD);
            case REDSTONE_ORE,
                DEEPSLATE_REDSTONE_ORE -> new ItemStack(Material.REDSTONE, 4 + random.nextInt(2));
            case LAPIS_ORE,
                DEEPSLATE_LAPIS_ORE -> new ItemStack(Material.LAPIS_LAZULI, 4 + random.nextInt(4));
            case COPPER_ORE,
                DEEPSLATE_COPPER_ORE -> new ItemStack(Material.RAW_COPPER);
            case IRON_ORE,
                DEEPSLATE_IRON_ORE -> new ItemStack(Material.RAW_IRON);
            case GOLD_ORE,
                DEEPSLATE_GOLD_ORE -> new ItemStack(Material.RAW_GOLD);
            default -> super.getDrops(material, random);
        };
    }

}
