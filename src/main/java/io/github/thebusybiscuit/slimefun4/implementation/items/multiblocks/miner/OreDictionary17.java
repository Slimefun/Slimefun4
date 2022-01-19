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
        switch (material) {
            case COAL_ORE:
            case DEEPSLATE_COAL_ORE:
                return new ItemStack(Material.COAL);
            case DIAMOND_ORE:
            case DEEPSLATE_DIAMOND_ORE:
                return new ItemStack(Material.DIAMOND);
            case EMERALD_ORE:
            case DEEPSLATE_EMERALD_ORE:
                return new ItemStack(Material.EMERALD);
            case REDSTONE_ORE:
            case DEEPSLATE_REDSTONE_ORE:
                return new ItemStack(Material.REDSTONE, 4 + random.nextInt(2));
            case LAPIS_ORE:
            case DEEPSLATE_LAPIS_ORE:
                return new ItemStack(Material.LAPIS_LAZULI, 4 + random.nextInt(4));
            case COPPER_ORE:
            case DEEPSLATE_COPPER_ORE:
                return new ItemStack(Material.RAW_COPPER);
            case IRON_ORE:
            case DEEPSLATE_IRON_ORE:
                return new ItemStack(Material.RAW_IRON);
            case GOLD_ORE:
            case DEEPSLATE_GOLD_ORE:
                return new ItemStack(Material.RAW_GOLD);
            default:
                return super.getDrops(material, random);
        }
    }

}
